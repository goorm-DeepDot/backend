package project.deepdot.alarm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AlarmSyncItem {
    private String type; // "SINGLE" | "WEEKLY"
    private Integer notificationId;   // SINGLE
    private Integer baseNotificationId; // WEEKLY
    private String scheduledTimeIso;  // SINGLE
    private String time;              // WEEKLY "HH:mm"
    private List<Integer> weekdays;   // WEEKLY
    private String title;
    private String body;
    private boolean active;
    private String zoneId;
}
