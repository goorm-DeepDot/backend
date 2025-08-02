package project.deepdot.medication.medication.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.deepdot.medication.medication.domain.Medication;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByUser_UserId(Long userId);
}