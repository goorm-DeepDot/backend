package project.deepdot.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WeeklyAlarmRepository extends JpaRepository<WeeklyAlarm, Long> {
    Optional<WeeklyAlarm> findByUserIdAndBaseNotificationId(Long userId, Integer baseNotificationId);
    List<WeeklyAlarm> findByUserId(Long userId);

    void deleteByUserId(Long userId);
    // [NEW]
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update WeeklyAlarm w set w.active = false where w.userId = :userId and w.active = true")
    int disableAllByUserId(@Param("userId") Long userId);

    // [NEW]
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update WeeklyAlarm w set w.active = true where w.userId = :userId and w.active = false")
    int enableAllByUserId(@Param("userId") Long userId);

    // [NEW]
    List<WeeklyAlarm> findByUserIdAndActiveTrue(Long userId);
}
