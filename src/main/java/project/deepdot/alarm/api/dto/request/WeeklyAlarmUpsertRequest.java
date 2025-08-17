package project.deepdot.alarm.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeeklyAlarmUpsertRequest {
    // 선택: 클라가 baseId 제안 가능(없으면 서버가 생성)
    private Integer baseNotificationId;

    @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$")
    @NotBlank
    private String time; // "HH:mm"

    @NotEmpty
    private List<@Min(1) @Max(7) Integer> weekdays; // 1=월..7=일

    @NotBlank private String title;
    @NotBlank private String body;

    @NotNull private Boolean active;

    private String zoneId; // default Asia/Seoul
}
