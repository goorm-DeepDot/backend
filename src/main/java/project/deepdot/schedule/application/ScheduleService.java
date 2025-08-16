package project.deepdot.schedule.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.medication.medication.domain.Medication;
import project.deepdot.schedule.api.dto.ScheduleRequest;
import project.deepdot.schedule.api.dto.ScheduleResponse;
import project.deepdot.schedule.domain.Schedule;
import project.deepdot.schedule.domain.ScheduleRepository;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 일정 추가
    @Transactional
    public Long create(ScheduleRequest request, User user) {
        Schedule schedule = Schedule.builder()
                .user(user)
                .title(request.getTitle())
                .time(request.getTime())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .type(request.getType())
                .location(request.getLocation())
                .memo(request.getMemo())
                .image(request.getImage())
                .alarm30Before(request.isAlarm30Before())
                .alarm60Before(request.isAlarm60Before())
                .alarm120Before(request.isAlarm120Before())
                .isRecurring(request.isRecurring())
                .build();

        return scheduleRepository.save(schedule).getScheduleId();
    }

    // 일정 단일 조회
    public ScheduleResponse findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정이 존재하지 않습니다."));
        return ScheduleResponse.of(schedule);
    }

    // 특정 날짜에 "해야 하는" 전체 일정 (startDate ≤ date ≤ endDate)
    // 시간 오름차순(시간 없으면 뒤로), 같은 시간이면 제목순
    @Transactional(readOnly = true)
    public List<ScheduleResponse> findByDate(User user, LocalDate date) {
        return scheduleRepository.findAllByUser(user).stream()
                .filter(s -> !s.getStartDate().isAfter(date) && !s.getEndDate().isBefore(date))
                .sorted(
                        Comparator.comparing(Schedule::getTime, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(Schedule::getTitle, Comparator.nullsLast(String::compareTo))
                )
                .map(ScheduleResponse::of)
                .toList();
    }
    // 기간 [from, to]과 겹치는 일정 모두, 시작날짜 → 시간 순
    @Transactional(readOnly = true)
    public List<ScheduleResponse> findInRange(User user, LocalDate from, LocalDate to) {
        return scheduleRepository.findAllByUser(user).stream()
                .filter(s -> !s.getStartDate().isAfter(to) && !s.getEndDate().isBefore(from))
                .sorted(
                        Comparator.comparing(Schedule::getStartDate)
                                .thenComparing(Schedule::getTime, Comparator.nullsLast(Comparator.naturalOrder()))
                )
                .map(ScheduleResponse::of)
                .toList();
    }


    // 전체 일정 조회 (사용자별)
    public List<ScheduleResponse> findAllByUser(User user) {
        return scheduleRepository.findAllByUser(user)
                .stream()
                .map(ScheduleResponse::of)
                .toList();
    }

    // 일정 수정
    @Transactional
    public void update(Long id, ScheduleRequest request, User user) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정이 존재하지 않습니다."));
        if (!Objects.equals(schedule.getUser().getUserId(), user.getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        schedule.update(
                request.getTitle(),
                request.getTime(),
                request.getStartDate(),
                request.getEndDate(),
                request.getType(),
                request.getLocation(),
                request.getMemo(),
                request.getImage(),
                request.isAlarm30Before(),
                request.isAlarm60Before(),
                request.isAlarm120Before(),
                request.isRecurring()
        );
    }

    // 일정 삭제
    @Transactional
    public void delete(Long id, User user) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        scheduleRepository.delete(schedule);
    }
}