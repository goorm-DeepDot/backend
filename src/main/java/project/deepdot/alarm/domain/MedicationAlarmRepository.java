package project.deepdot.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicationAlarmRepository extends JpaRepository<MedicationAlarm, Long> {
    List<MedicationAlarm> findByUserId(Long userId);
    Optional<MedicationAlarm> findByUserIdAndMedicationName(Long userId, String medicationName);

    void deleteByUserId(Long userId);
}
