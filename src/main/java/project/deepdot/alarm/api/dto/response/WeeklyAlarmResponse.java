package project.deepdot.alarm.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class WeeklyAlarmResponse {
    private Integer baseNotificationId;
    private String time; // "HH:mm"
    private List<Integer> weekdays; // [1..7]
    private String title;
    private String body;
    private boolean active;
    private String zoneId;
}
