package project.deepdot.setting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.alarm.domain.MedicationAlarmRepository;
import project.deepdot.alarm.domain.SingleAlarmRepository;
import project.deepdot.alarm.domain.WeeklyAlarmRepository;
import project.deepdot.setting.api.dto.AlarmSettingsResponse;
import project.deepdot.setting.api.dto.AlarmSettingsUpdateRequest;
import project.deepdot.setting.domain.AlarmSettings;
import project.deepdot.setting.domain.AlarmSettingsRepository;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AlarmSettingsService {

    private final AlarmSettingsRepository repo;            // [NEW]
    private final SingleAlarmRepository singleRepo;        // [NEW]
    private final WeeklyAlarmRepository weeklyRepo;        // [NEW]
    private final MedicationAlarmRepository medicationRepo;// [NEW]

    private AlarmSettings getOrCreate(Long userId) {
        return repo.findByUserId(userId).orElseGet(() ->
                repo.save(AlarmSettings.builder()
                        .userId(userId)
                        .routineEnabled(true)     // [NEW] 기본 ON
                        .taskEnabled(true)        // [NEW] 기본 ON
                        .medicationEnabled(true)  // [NEW] 기본 ON
                        .updatedAt(Instant.now())
                        .build())
        );
    }

    @Transactional(readOnly = true)
    public AlarmSettingsResponse get(Long userId) {
        return toResponse(getOrCreate(userId));
    }

    /**
     * 요구사항:
     *  - false → 기존/신규 모두 inactive
     *  - true  → 기존/신규 모두 active
     */
    @Transactional
    public AlarmSettingsResponse update(Long userId, AlarmSettingsUpdateRequest req) {
        AlarmSettings s = getOrCreate(userId);

        boolean wasRoutine    = s.isRoutineEnabled();
        boolean wasTask       = s.isTaskEnabled();
        boolean wasMedication = s.isMedicationEnabled();

        s.apply(req.getRoutineEnabled(), req.getTaskEnabled(), req.getMedicationEnabled());
        repo.save(s);

        // ON → OFF : 기존 일괄 비활성화
        if (wasTask && !s.isTaskEnabled())                 singleRepo.disableAllByUserId(userId);      // [NEW]
        if (wasRoutine && !s.isRoutineEnabled())           weeklyRepo.disableAllByUserId(userId);      // [NEW]
        if (wasMedication && !s.isMedicationEnabled())     medicationRepo.disableAllByUserId(userId);  // [NEW]

        // OFF → ON : 기존 일괄 활성화
        if (!wasTask && s.isTaskEnabled())                 singleRepo.enableAllByUserId(userId);       // [NEW]
        if (!wasRoutine && s.isRoutineEnabled())           weeklyRepo.enableAllByUserId(userId);       // [NEW]
        if (!wasMedication && s.isMedicationEnabled())     medicationRepo.enableAllByUserId(userId);   // [NEW]

        return toResponse(s);
    }

    public boolean isRoutineEnabled(Long userId)    { return getOrCreate(userId).isRoutineEnabled(); }
    public boolean isTaskEnabled(Long userId)       { return getOrCreate(userId).isTaskEnabled(); }
    public boolean isMedicationEnabled(Long userId) { return getOrCreate(userId).isMedicationEnabled(); }

    private AlarmSettingsResponse toResponse(AlarmSettings s) {
        return AlarmSettingsResponse.builder()
                .routineEnabled(s.isRoutineEnabled())
                .taskEnabled(s.isTaskEnabled())
                .medicationEnabled(s.isMedicationEnabled())
                .updatedAtIso(DateTimeFormatter.ISO_INSTANT.format(s.getUpdatedAt()))
                .build();
    }
}
