package project.deepdot.medication.medicationtime.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.medication.medication.domain.Medication;
import project.deepdot.medication.medication.domain.repository.MedicationRepository;
import project.deepdot.medication.medicationtime.api.MedicationTimeRequest;
import project.deepdot.medication.medicationtime.api.MedicationTimeResponse;
import project.deepdot.medication.medicationtime.domain.MedicationTime;
import project.deepdot.medication.medicationtime.domain.MedicationTimeRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MedicationTimeService {
    private final MedicationTimeRepository timeRepository;
    private final MedicationRepository medicationRepository;

    private static final int MAX_TIME_PER_MEDICATION = 3;

    @Transactional
    public void addTime(Long medicationId, MedicationTimeRequest request) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("복용약이 존재하지 않습니다."));

        int count = timeRepository.countByMedicationId(medicationId);
        if (count >= MAX_TIME_PER_MEDICATION) {
            throw new IllegalStateException("복용시간은 최대 3개까지 등록 가능합니다.");
        }

        MedicationTime time = MedicationTime.builder()
                .time(request.time())
                .medication(medication)
                .build();

        timeRepository.save(time);
    }

    @Transactional(readOnly = true)
    public List<MedicationTimeResponse> getTimes(Long medicationId) {
        return timeRepository.findByMedicationId(medicationId).stream()
                .map(MedicationTimeResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTime(Long timeId) {
        timeRepository.deleteById(timeId);
    }
}
