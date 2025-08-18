package project.deepdot.alarm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.alarm.api.dto.MedicationAlarmResponse;
import project.deepdot.alarm.api.dto.MedicationAlarmUpsertRequest;
import project.deepdot.alarm.domain.MedicationAlarm;
import project.deepdot.alarm.domain.MedicationAlarmRepository;
import project.deepdot.alarm.domain.MedicationAlarmTime;
import project.deepdot.setting.domain.AlarmSettings;
import project.deepdot.setting.domain.AlarmSettingsRepository;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationAlarmService {

    private static final int MAX_TIME_PER_ALARM = 3;

    private final MedicationAlarmRepository alarmRepo;
    private final AlarmSettingsRepository settingsRepo; // [NEW]

    /**
     * 약 이름 기준으로 업서트 (있으면 갱신, 없으면 생성)
     */
    @Transactional
    public MedicationAlarmResponse upsert(Long userId, MedicationAlarmUpsertRequest req) {
        // -------- 입력 정리/검증 --------
        String name = req.getMedicationName().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("약 이름은 필수입니다.");
        }

        List<String> timesStr = Optional.ofNullable(req.getTimes()).orElseGet(List::of);

        // 중복 제거 + 순서 보존
        LinkedHashSet<String> dedup = new LinkedHashSet<>(timesStr);
        if (dedup.size() > MAX_TIME_PER_ALARM) {
            throw new IllegalArgumentException("복용 시간은 최대 " + MAX_TIME_PER_ALARM + "개까지 등록 가능합니다.");
        }

        // 문자열 → LocalTime 파싱
        List<LocalTime> sortedTimes = dedup.stream()
                .map(LocalTime::parse)
                .sorted()
                .collect(Collectors.toList());

        // LocalTime → 자식 엔티티
        List<MedicationAlarmTime> newTimes = sortedTimes.stream()
                .map(t -> MedicationAlarmTime.builder().time(t).build())
                .collect(Collectors.toList());

        // [NEW] 토글에 완전 종속: medicationEnabled면 true, 아니면 false (요청 active 무시)
        boolean medicationEnabled = settingsRepo.findByUserId(userId)
                .map(AlarmSettings::isMedicationEnabled).orElse(true);
        boolean effectiveActive = medicationEnabled; // [NEW]

        // -------- upsert 처리 --------
        MedicationAlarm alarm = alarmRepo.findByUserIdAndMedicationName(userId, name)
                .orElseGet(() -> MedicationAlarm.builder()
                        .userId(userId)
                        .medicationName(name)
                        .active(Boolean.TRUE.equals(req.getActive()))
                        .build()
                );

        // 통째로 갱신 (이 메서드가 양방향 세팅/클리어 처리)
        alarm.update(name, req.getActive(), newTimes);

        // 저장
        alarmRepo.save(alarm);

        return toResponse(alarm);
    }

    @Transactional(readOnly = true)
    public List<MedicationAlarmResponse> list(Long userId) {
        return alarmRepo.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * alarmId로 삭제 (본인 것만)
     */
    @Transactional
    public void delete(Long userId, Long alarmId) {
        MedicationAlarm alarm = alarmRepo.findById(alarmId)
                .orElseThrow(() -> new IllegalArgumentException("알람을 찾을 수 없습니다."));

        if (!Objects.equals(alarm.getUserId(), userId)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        alarmRepo.delete(alarm);
    }

    private MedicationAlarmResponse toResponse(MedicationAlarm a) {
        // times가 null이면 빈 리스트
        List<String> times = Optional.ofNullable(a.getTimes()).orElseGet(List::of)
                .stream()
                .map(t -> t.getTime().toString())
                .sorted()
                .collect(Collectors.toList());

        return MedicationAlarmResponse.builder()
                .id(a.getId())
                .medicationName(a.getMedicationName())
                .active(a.isActive())
                .times(times)
                .build();
    }
}
