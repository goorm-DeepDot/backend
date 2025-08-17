package project.deepdot.alarm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.alarm.api.dto.AlarmSyncItem;
import project.deepdot.alarm.domain.SingleAlarmRepository;
import project.deepdot.alarm.domain.WeeklyAlarmRepository;
import project.deepdot.alarm.util.WeekdayMask;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmSyncService {
    private final SingleAlarmRepository singleRepo;
    private final WeeklyAlarmRepository weeklyRepo;

    @Transactional(readOnly = true)
    public List<AlarmSyncItem> all(Long userId) {
        List<AlarmSyncItem> singles = singleRepo.findByUserId(userId).stream().map(a ->
                AlarmSyncItem.builder()
                        .type("SINGLE")
                        .notificationId(a.getNotificationId())
                        .scheduledTimeIso(a.getScheduledAt().toString())
                        .title(a.getTitle())
                        .body(a.getBody())
                        .active(a.isActive())
                        .zoneId(a.getZoneId())
                        .build()
        ).collect(Collectors.toList());

        List<AlarmSyncItem> weekly = weeklyRepo.findByUserId(userId).stream().map(a ->
                AlarmSyncItem.builder()
                        .type("WEEKLY")
                        .baseNotificationId(a.getBaseNotificationId())
                        .time(a.getTimeLocal().toString())
                        .weekdays(WeekdayMask.toList(a.getWeekdayMask()))
                        .title(a.getTitle())
                        .body(a.getBody())
                        .active(a.isActive())
                        .zoneId(a.getZoneId())
                        .build()
        ).collect(Collectors.toList());

        ArrayList<AlarmSyncItem> all = new ArrayList<>(singles.size()+weekly.size());
        all.addAll(singles); all.addAll(weekly);
        return all;
    }
}