package project.deepdot.alarm.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleAlarmUpsertRequest {
    // 선택: 클라가 직접 id를 제안하고 싶으면 허용(중복 시 409)
    private Integer notificationId;

    @NotBlank
    private String scheduledTimeIso; // "2025-08-14T09:00:00Z" (UTC ISO 8601)

    @NotBlank private String title;
    @NotBlank private String body;

    @NotNull
    private Boolean active;

    private String zoneId; // default Asia/Seoul
}
