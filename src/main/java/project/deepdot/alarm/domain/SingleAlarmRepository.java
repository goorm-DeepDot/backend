package project.deepdot.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SingleAlarmRepository extends JpaRepository<SingleAlarm, Long> {
    Optional<SingleAlarm> findByUserIdAndNotificationId(Long userId, Integer notificationId);
    List<SingleAlarm> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
