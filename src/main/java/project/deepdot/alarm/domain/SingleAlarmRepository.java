package project.deepdot.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SingleAlarmRepository extends JpaRepository<SingleAlarm, Long> {
    Optional<SingleAlarm> findByUserIdAndNotificationId(Long userId, Integer notificationId);
    List<SingleAlarm> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    // [NEW] 토글 OFF 시 기존 알림 일괄 비활성화
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update SingleAlarm s set s.active = false where s.userId = :userId and s.active = true")
    int disableAllByUserId(@Param("userId") Long userId);

    // [NEW] 토글 ON 시 기존 알림 일괄 활성화
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update SingleAlarm s set s.active = true where s.userId = :userId and s.active = false")
    int enableAllByUserId(@Param("userId") Long userId);

    // [NEW] active=true만 조회할 때 사용(동기화 등)
    List<SingleAlarm> findByUserIdAndActiveTrue(Long userId);
}
