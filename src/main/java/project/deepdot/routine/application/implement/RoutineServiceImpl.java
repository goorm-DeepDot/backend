package project.deepdot.routine.application.implement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.routine.api.dto.request.routine.RoutineRequestDto;
import project.deepdot.routine.api.dto.request.routine.TaskRequestDto;
import project.deepdot.routine.api.dto.response.routine.RoutineResponseDto;
import project.deepdot.routine.api.dto.response.routine.TaskResponseDto;
import project.deepdot.routine.application.RoutineService;
import project.deepdot.routine.domain.Routine;
import project.deepdot.routine.domain.Task;
import project.deepdot.user.user.domain.User;
import project.deepdot.routine.domain.repository.RoutineRepository;
import project.deepdot.routine.domain.repository.TaskRepository;
import project.deepdot.user.user.domain.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public RoutineResponseDto createRoutine(Long userId, RoutineRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Routine routine = new Routine();
        routine.setUser(user);
        routine.setName(dto.getName());
        routine.setStartTime(dto.getStartTime());
        routine.setAlarm(dto.getAlarm());
        routine.setMon(dto.getMon());
        routine.setTue(dto.getTue());
        routine.setWed(dto.getWed());
        routine.setThu(dto.getThu());
        routine.setFri(dto.getFri());
        routine.setSat(dto.getSat());
        routine.setSun(dto.getSun());

        routineRepository.save(routine);

        for (TaskRequestDto t : dto.getTasks()) {
            Task task = new Task();
            task.setRoutine(routine);
            task.setName(t.getName());
            taskRepository.save(task);
        }

        // routineId 포함한 성공 응답 반환
        return RoutineResponseDto.success(routine.getRoutineId());
    }

    @Transactional
    @Override
    public RoutineResponseDto updateRoutine(Long routineId, RoutineRequestDto dto) {
        // 1. 기존 루틴 조회
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));

        // 2. Routine 필드 업데이트
        routine.setName(dto.getName());
        routine.setStartTime(dto.getStartTime());
        routine.setAlarm(dto.getAlarm());
        routine.setMon(dto.getMon());
        routine.setTue(dto.getTue());
        routine.setWed(dto.getWed());
        routine.setThu(dto.getThu());
        routine.setFri(dto.getFri());
        routine.setSat(dto.getSat());
        routine.setSun(dto.getSun());

        // 3. 기존 Task 삭제
        List<Task> existingTasks = taskRepository.findByRoutine(routine);
        for (Task task : existingTasks) {
            taskRepository.delete(task);
        }

        // 4. 새 Task 저장
        for (TaskRequestDto t : dto.getTasks()) {
            Task task = new Task();
            task.setRoutine(routine);
            task.setName(t.getName());
            taskRepository.save(task);
        }

        // 5. 업데이트된 routineId 반환
        return RoutineResponseDto.success(routine.getRoutineId());
    }

    @Transactional
    @Override
    public void deleteRoutine(Long routineId) {

        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));
        routineRepository.delete(routine); // Task도 자동 삭제

    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDto> getAllTasks(Long userId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2. 유저의 모든 Task 조회
        List<Task> tasks = taskRepository.findByRoutine_User(user);

        // 3. Task -> TaskResponseDto 변환
        return tasks.stream()
                .map(task -> {
                    Routine routine = task.getRoutine();
                    return new TaskResponseDto(
                            task.getTaskId(),
                            task.getName(),
                            routine.getRoutineId(),
                            routine.getMon(),
                            routine.getTue(),
                            routine.getWed(),
                            routine.getThu(),
                            routine.getFri(),
                            routine.getSat(),
                            routine.getSun()
                    );
                })
                .toList();
    }


}

