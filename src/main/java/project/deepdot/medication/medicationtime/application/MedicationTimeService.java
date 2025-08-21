package project.deepdot.medication.medicationtime.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.medication.medication.domain.Medication;
import project.deepdot.medication.medication.domain.MedicationRepository;
import project.deepdot.medication.medicationtime.api.MedicationTimeRequest;
import project.deepdot.medication.medicationtime.api.MedicationTimeResponse;
import project.deepdot.medication.medicationtime.domain.MedicationTime;
import project.deepdot.medication.medicationtime.domain.MedicationTimeRepository;
import project.deepdot.user.domain.User;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MedicationTimeService {
    private final MedicationTimeRepository timeRepository;
    private final MedicationRepository medicationRepository;

    private static final int MAX_TIME_PER_MEDICATION = 3;

    //시간 추가
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

    //조회
    @Transactional(readOnly = true)
    public List<LocalTime> getTimes(Long medicationId, User user) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));

        if (!medication.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("조회 권한이 없습니다.");
        }

        return timeRepository.findByMedicationId(medicationId)
                .stream()
                .map(MedicationTime::getTime)
                .collect(Collectors.toList());
    }

    //삭제
    @Transactional
    public void deleteTime(Long timeId) {
        timeRepository.deleteById(timeId);
    }

    //수정
    @Transactional
    public void updateTime(Long timeId, User user, LocalTime newTime) {
        MedicationTime mt = timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("복용시간을 찾을 수 없습니다."));

        // 소유자 검증
        if (!mt.getMedication().getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        // 단순 시간 수정
        mt.changeTime(newTime);
    }

    // time 전체 삭제
    @Transactional
    public List<MedicationTimeResponse> deleteAllTimesOfMedication(Long medicationId, User user) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("복용약을 찾을 수 없습니다."));

        // 소유자 검증
        if (!medication.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        // 삭제 전 응답용 스냅샷 생성
        List<MedicationTimeResponse> deleted =
                timeRepository.findByMedicationId(medicationId).stream()
                        .map(MedicationTimeResponse::from)
                        .toList();

        // 벌크 삭제
        timeRepository.deleteByMedicationId(medicationId);

        return deleted; // 기존 응답 DTO 재사용
    }

}
