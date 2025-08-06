package project.deepdot.medication.medication.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.medication.medication.api.MedicationRequest;
import project.deepdot.medication.medication.api.MedicationResponse;
import project.deepdot.medication.medication.domain.Medication;
import project.deepdot.medication.medication.domain.MedicationRepository;
import project.deepdot.medication.medicationtime.domain.MedicationTimeRepository;
import project.deepdot.medication.medicationtime.domain.MedicationTime;
import project.deepdot.user.domain.User;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationTimeRepository medicationTimeRepository;

    @Transactional
    public Long create(MedicationRequest request, User user) {
        Medication medication = Medication.builder()
                .user(user)
                .name(request.getName())
                .alarm(request.isAlarm())
                .build();
        return medicationRepository.save(medication).getId();
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> findAllByUser(Long userId) {
        List<Medication> medications = medicationRepository.findByUser_UserId(userId);
        return medications.stream()
                .map(med -> {
                    List<LocalTime> times = med.getMedicationTimes().stream()
                            .map(MedicationTime::getTime)
                            .collect(Collectors.toList());
                    return new MedicationResponse(med, times);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicationResponse findById(Long id, User user) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));
        if (!medication.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("조회 권한이 없습니다.");
        }
        List<LocalTime> times = medicationTimeRepository.findByMedicationId(id)
                .stream()
                .map(MedicationTime::getTime)
                .collect(Collectors.toList());
        return new MedicationResponse(medication, times);
    }

    @Transactional
    public void update(Long id, MedicationRequest request, User user) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));
        if (!medication.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        medication.update(request.getName(), request.isAlarm());
    }

    @Transactional
    public void delete(Long id, User user) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));
        if (!medication.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        medicationRepository.delete(medication);
    }
}