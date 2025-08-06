package project.deepdot.medication.medication.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByUser_UserId(Long userId);
}