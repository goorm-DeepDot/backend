package project.deepdot.alarm.application;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.alarm.api.dto.response.WeeklyAlarmResponse;
import project.deepdot.alarm.api.dto.request.WeeklyAlarmUpsertRequest;
import project.deepdot.alarm.domain.WeeklyAlarm;
import project.deepdot.alarm.domain.WeeklyAlarmRepository;
import project.deepdot.alarm.util.WeekdayMask;
import project.deepdot.setting.domain.AlarmSettings;
import project.deepdot.setting.domain.AlarmSettingsRepository;

import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeeklyAlarmService {
    private final WeeklyAlarmRepository repo;
    private final SecureRandom random = new SecureRandom();
    private final AlarmSettingsRepository settingsRepo; // [NEW]

    private int newBaseId(Long userId) {
        while (true) {
            int id = 200000 + random.nextInt(800000000);
            boolean exists = repo.findByUserIdAndBaseNotificationId(userId, id).isPresent();
            if (!exists) return id;
        }
    }

    @Transactional
    public WeeklyAlarmResponse upsert(Long userId, WeeklyAlarmUpsertRequest req) {
        String zone = (req.getZoneId() == null || req.getZoneId().isBlank()) ? "Asia/Seoul" : req.getZoneId();

        int mask = WeekdayMask.toMask(req.getWeekdays());
        LocalTime timeLocal = LocalTime.parse(req.getTime()); // "HH:mm"

        Integer baseId = (req.getBaseNotificationId() == null)
                ? newBaseId(userId) : req.getBaseNotificationId();

        // [NEW] 토글에 완전 종속: routineEnabled면 true, 아니면 false (요청 active 무시)
        boolean routineEnabled = settingsRepo.findByUserId(userId)
                .map(AlarmSettings::isRoutineEnabled).orElse(true);
        boolean effectiveActive = routineEnabled; // [NEW]


        WeeklyAlarm entity = repo.findByUserIdAndBaseNotificationId(userId, baseId)
                .map(a -> { a.update(timeLocal, mask, req.getTitle(), req.getBody(), req.getActive(), zone); return a; })
                .orElseGet(() -> repo.save(WeeklyAlarm.builder()
                        .userId(userId)
                        .baseNotificationId(baseId)
                        .timeLocal(timeLocal)
                        .weekdayMask(mask)
                        .title(req.getTitle())
                        .body(req.getBody())
                        //.active(req.getActive())
                        .active(effectiveActive) // [MODIFIED]
                        .zoneId(zone)
                        .build()));

        return WeeklyAlarmResponse.builder()
                .baseNotificationId(entity.getBaseNotificationId())
                .time(entity.getTimeLocal().toString())
                .weekdays(WeekdayMask.toList(entity.getWeekdayMask()))
                .title(entity.getTitle())
                .body(entity.getBody())
                .active(entity.isActive())
                .zoneId(entity.getZoneId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<WeeklyAlarmResponse> list(Long userId) {
        return repo.findByUserId(userId).stream().map(a ->
                WeeklyAlarmResponse.builder()
                        .baseNotificationId(a.getBaseNotificationId())
                        .time(a.getTimeLocal().toString())
                        .weekdays(WeekdayMask.toList(a.getWeekdayMask()))
                        .title(a.getTitle())
                        .body(a.getBody())
                        .active(a.isActive())
                        .zoneId(a.getZoneId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Integer baseId) {
        WeeklyAlarm a = repo.findByUserIdAndBaseNotificationId(userId, baseId)
                .orElseThrow(() -> new IllegalArgumentException("주간 알람이 없습니다"));
        repo.delete(a);
    }
}
