package project.deepdot.routine.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.routine.api.dto.*;
import project.deepdot.routine.domain.Goal;
import project.deepdot.routine.domain.GoalRepository;
import project.deepdot.routine.domain.Routine;
import project.deepdot.routine.domain.RoutineRepository;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService { // ← 인터페이스/Impl 통합

    private final RoutineRepository routineRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    private static final int MAX_GOALS_PER_USER = 5;

    // ---------- 루틴 생성 ----------
    @Transactional
    public RoutineResponseDto createRoutine(Long userId, RoutineRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Goal goal = resolveGoalForCreate(user, dto); // goalId / newGoalName 처리

        Routine routine = new Routine();
        routine.setUser(user);
        routine.setName(dto.getName());
        routine.setStartTime(dto.getStartTime());
        routine.setActive(dto.getActive());   // alarm → active
        routine.setMon(dto.getMon());
        routine.setTue(dto.getTue());
        routine.setWed(dto.getWed());
        routine.setThu(dto.getThu());
        routine.setFri(dto.getFri());
        routine.setSat(dto.getSat());
        routine.setSun(dto.getSun());
        routine.setMemo(dto.getMemo());
        routine.setGoal(goal);                // Goal 1 : N Routine

        routineRepository.save(routine);

        return RoutineResponseDto.success(
                routine.getRoutineId(),
                goal.getGoalId(),
                goal.getName(),
                routine.getActive()
        );
    }

    // ---------- 루틴 수정 ----------
    @Transactional
    public RoutineResponseDto updateRoutine(Long routineId, RoutineRequestDto dto) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));

        // 목표 교체 요청이 있는 경우(같은 목표 재사용 가능)
        if (dto.getGoalId() != null || (dto.getNewGoalName() != null && !dto.getNewGoalName().isBlank())) {
            User user = routine.getUser();
            Goal newGoal = resolveGoalForCreate(user, dto);
            routine.setGoal(newGoal);
        }

        routine.setName(dto.getName());
        routine.setStartTime(dto.getStartTime());
        routine.setActive(dto.getActive());
        routine.setMon(dto.getMon());
        routine.setTue(dto.getTue());
        routine.setWed(dto.getWed());
        routine.setThu(dto.getThu());
        routine.setFri(dto.getFri());
        routine.setSat(dto.getSat());
        routine.setSun(dto.getSun());
        routine.setMemo(dto.getMemo());

        Routine saved = routineRepository.save(routine);
        return RoutineResponseDto.success(
                saved.getRoutineId(),
                saved.getGoal().getGoalId(),
                saved.getGoal().getName(),
                saved.getActive()
        );
    }

    // ---------- 루틴 삭제 ----------
    @Transactional
    public void deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));
        routineRepository.delete(routine); // Goal은 카탈로그라 유지
    }

    // ---------- 목표 목록 ----------
    @Transactional(readOnly = true)
    public List<GoalResponseDto> listGoals(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return goalRepository.findByUser(user).stream()
                .map(g -> GoalResponseDto.builder()
                        .goalId(g.getGoalId())
                        .name(g.getName())
                        .inUse(routineRepository.existsByGoal_GoalId(g.getGoalId()))
                        .build())
                .toList();
    }

    // ---------- 목표 추가 ----------
    @Transactional
    public GoalResponseDto addGoal(Long userId, GoalRequestDto req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String name = (req.getName() == null) ? "" : req.getName().trim();
        if (name.isBlank()) throw new IllegalArgumentException("목표 이름은 필수입니다.");

        long cnt = goalRepository.countByUser(user);
        if (cnt >= MAX_GOALS_PER_USER) {
            throw new IllegalStateException("목표는 최대 " + MAX_GOALS_PER_USER + "개까지 등록할 수 있습니다.");
        }

        goalRepository.findByUserAndName(user, name).ifPresent(x -> {
            throw new IllegalStateException("이미 존재하는 목표 이름입니다.");
        });

        Goal g = goalRepository.save(Goal.builder().user(user).name(name).build());
        return GoalResponseDto.builder().goalId(g.getGoalId()).name(g.getName()).inUse(false).build();
    }

    // ---------- [NEW] 특정 목표의 루틴 목록 ----------
    @Transactional(readOnly = true)
    public List<RoutineReadDto> listRoutinesByGoal(Long userId, Long goalId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));

        if (!goal.getUser().getUserId().equals(userId)) {
            throw new SecurityException("다른 사용자의 목표는 조회할 수 없습니다.");
        }

        return routineRepository.findByUserAndGoal(user, goal).stream()
                .map(r -> RoutineReadDto.of(
                        r.getRoutineId(),
                        r.getName(),
                        goal.getGoalId(),
                        goal.getName(),
                        r.getActive(),
                        r.getStartTime(),
                        r.getMon(), r.getTue(), r.getWed(), r.getThu(), r.getFri(), r.getSat(), r.getSun(),
                        r.getMemo()
                ))
                .toList();
    }

    // ---------- 목표 수정(이름 변경) ----------
    @Transactional
    public GoalResponseDto updateGoal(Long userId, Long goalId, GoalRequestDto req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));

        if (!goal.getUser().getUserId().equals(userId)) {
            throw new SecurityException("다른 사용자의 목표는 수정할 수 없습니다.");
        }

        String newName = (req.getName() == null) ? "" : req.getName().trim();
        if (newName.isBlank()) throw new IllegalArgumentException("목표 이름은 필수입니다.");

        if (!newName.equals(goal.getName())) {
            goalRepository.findByUserAndName(user, newName).ifPresent(x -> {
                throw new IllegalStateException("이미 존재하는 목표 이름입니다.");
            });
            goal.setName(newName);
            goalRepository.save(goal);
        }

        return GoalResponseDto.builder()
                .goalId(goal.getGoalId())
                .name(goal.getName())
                .inUse(routineRepository.existsByGoal_GoalId(goal.getGoalId()))
                .build();
    }

    // ---------- 목표 삭제 ----------
    @Transactional
    public void deleteGoal(Long userId, Long goalId) {
        Goal g = goalRepository.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));

        if (!g.getUser().getUserId().equals(userId)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        // 하나라도 루틴에서 사용 중이면 삭제 불가
        if (routineRepository.existsByGoal_GoalId(goalId)) {
            throw new IllegalStateException("해당 목표를 사용하는 루틴이 있어 삭제할 수 없습니다.");
        }

        goalRepository.delete(g);
    }

    // ---------- 내부 유틸 ----------
    private Goal resolveGoalForCreate(User user, RoutineRequestDto dto) {
        if (dto.getGoalId() != null) {
            Goal g = goalRepository.findById(dto.getGoalId())
                    .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
            if (!g.getUser().getUserId().equals(user.getUserId())) {
                throw new SecurityException("다른 사용자의 목표는 선택할 수 없습니다.");
            }
            // 1:N 이므로 같은 목표를 여러 루틴이 써도 허용
            return g;
        }

        String newGoalName = (dto.getNewGoalName() == null) ? "" : dto.getNewGoalName().trim();
        if (!newGoalName.isBlank()) {
            long cnt = goalRepository.countByUser(user);
            if (cnt >= MAX_GOALS_PER_USER) {
                throw new IllegalStateException("목표는 최대 " + MAX_GOALS_PER_USER + "개까지 등록할 수 있습니다.");
            }
            goalRepository.findByUserAndName(user, newGoalName).ifPresent(x -> {
                throw new IllegalStateException("이미 존재하는 목표 이름입니다.");
            });
            return goalRepository.save(Goal.builder().user(user).name(newGoalName).build());
        }

        throw new IllegalArgumentException("goalId 또는 newGoalName 중 하나는 반드시 필요합니다.");
    }
}