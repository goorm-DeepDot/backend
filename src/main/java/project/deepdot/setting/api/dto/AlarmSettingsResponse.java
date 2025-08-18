package project.deepdot.setting.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AlarmSettingsResponse {
    private boolean routineEnabled;
    private boolean taskEnabled;
    private boolean medicationEnabled;
    private String updatedAtIso; // ISO-8601
}
