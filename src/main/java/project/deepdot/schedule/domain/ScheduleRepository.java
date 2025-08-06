package project.deepdot.schedule.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import project.deepdot.user.domain.User;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 로그인한 사용자의 모든 일정
    List<Schedule> findAllByUser(User user);

    // 로그인한 사용자의 특정 날짜 일정 (시간순 정렬)
    List<Schedule> findAllByUserAndCalendarDateOrderByTimeAsc(User user, LocalDate calendarDate);
}