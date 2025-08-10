package project.deepdot.schedule.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.deepdot.schedule.api.dto.ScheduleRequest;
import project.deepdot.schedule.api.dto.ScheduleResponse;
import project.deepdot.schedule.domain.Schedule;
import project.deepdot.schedule.domain.ScheduleRepository;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 일정 추가
    public Long create(ScheduleRequest request, User user) {
        Schedule schedule = Schedule.builder()
                .user(user)
                .title(request.getTitle())
                .time(request.getTime())
                .calendarDate(request.getCalendarDate())
                .type(request.getType())
                .location(request.getLocation())
                .memo(request.getMemo())
                .image(request.getImage())
                .alarm30Before(request.isAlarm30Before())
                .alarm60Before(request.isAlarm60Before())
                .alarm120Before(request.isAlarm120Before())
                .build();

        return scheduleRepository.save(schedule).getScheduleId();
    }

    // 일정 단일 조회
    public ScheduleResponse findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정이 존재하지 않습니다."));
        return ScheduleResponse.of(schedule);
    }

    // 특정 날짜의 일정 전체 조회
    public List<ScheduleResponse> findByDate(User user, LocalDate date) {
        return scheduleRepository.findAllByUserAndCalendarDateOrderByTimeAsc(user, date)
                .stream()
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

    // 일정 삭제
    public void delete(Long scheduleId, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("일정이 존재하지 않습니다."));
        if (!schedule.getUser().equals(user)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        scheduleRepository.delete(schedule);
    }

    // 일정 수정
    public void update(Long id, ScheduleRequest request, User user) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정이 존재하지 않습니다."));
        if (!schedule.getUser().equals(user)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        schedule.update(
                request.getTitle(),
                request.getTime(),
                request.getCalendarDate(),
                request.getType(),
                request.getLocation(),
                request.getMemo(),
                request.getImage(),
                request.isAlarm30Before(),
                request.isAlarm60Before(),
                request.isAlarm120Before()
        );
    }
}