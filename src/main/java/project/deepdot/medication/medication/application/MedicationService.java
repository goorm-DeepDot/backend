package project.deepdot.medication.medication.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.medication.medication.api.MedicationRequest;
import project.deepdot.medication.medication.api.MedicationResponse;
import project.deepdot.medication.medication.domain.Medication;
import project.deepdot.medication.medication.domain.repository.MedicationRepository;
import project.deepdot.medication.medicationtime.domain.MedicationTimeRepository;
import project.deepdot.medication.medicationtime.domain.MedicationTime;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final UserRepository userRepository;
    private final MedicationTimeRepository medicationTimeRepository;
    private final MedicationRepository medicationRepository;

    // 등록
    public Long create(MedicationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Medication medication = Medication.builder()
                .user(user)
                .name(request.getName())
                .alarm(request.isAlarm())
                .build();

        return medicationRepository.save(medication).getId();
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<MedicationResponse> findAllByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId);
        }

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

    // 단건 조회
    @Transactional(readOnly = true)
    public MedicationResponse findById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));

        List<LocalTime> times = medicationTimeRepository.findByMedicationId(id)
                .stream()
                .map(MedicationTime::getTime)
                .collect(Collectors.toList());

        return new MedicationResponse(medication, times);
    }

    // 수정
    @Transactional
    public void update(Long id, MedicationRequest request) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));
        medication.update(request.getName(), request.isAlarm());
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new IllegalArgumentException("복용약을 찾을 수 없습니다.");
        }
        medicationRepository.deleteById(id);
    }
}