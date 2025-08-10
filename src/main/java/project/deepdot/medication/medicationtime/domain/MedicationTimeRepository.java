package project.deepdot.medication.medicationtime.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationTimeRepository extends JpaRepository<MedicationTime, Long> {
    List<MedicationTime> findByMedicationId(Long medicationId);
    int countByMedicationId(Long medicationId);
}