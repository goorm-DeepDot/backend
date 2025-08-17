package project.deepdot.alarm.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationAlarmUpsertRequest {

    @NotBlank
    private String medicationName;

    // "HH:mm" 형식, 최대 3개. (null 이면 서비스에서 빈 리스트로 처리)
    @Size(max = 3)
    private List<
                @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
                        message = "time은 HH:mm 형식이어야 합니다")
                        String
                > times;

    @NotNull
    private Boolean active;
}
