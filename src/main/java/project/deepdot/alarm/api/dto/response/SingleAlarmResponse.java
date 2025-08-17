package project.deepdot.alarm.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SingleAlarmResponse {
    private Integer notificationId;
    private String scheduledTimeIso; // UTC ISO
    private String title;
    private String body;
    private boolean active;
    private String zoneId;
}
