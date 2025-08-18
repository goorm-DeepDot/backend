package project.deepdot.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicationAlarmRepository extends JpaRepository<MedicationAlarm, Long> {
    List<MedicationAlarm> findByUserId(Long userId);
    Optional<MedicationAlarm> findByUserIdAndMedicationName(Long userId, String medicationName);

    void deleteByUserId(Long userId);

    // [NEW]
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MedicationAlarm m set m.active = false where m.userId = :userId and m.active = true")
    int disableAllByUserId(@Param("userId") Long userId);

    // [NEW]
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MedicationAlarm m set m.active = true where m.userId = :userId and m.active = false")
    int enableAllByUserId(@Param("userId") Long userId);

    // [NEW]
    List<MedicationAlarm> findByUserIdAndActiveTrue(Long userId);
}
