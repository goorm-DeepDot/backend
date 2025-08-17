package project.deepdot.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeeklyAlarmRepository extends JpaRepository<WeeklyAlarm, Long> {
    Optional<WeeklyAlarm> findByUserIdAndBaseNotificationId(Long userId, Integer baseNotificationId);
    List<WeeklyAlarm> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
