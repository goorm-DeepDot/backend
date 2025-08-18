package project.deepdot.setting.api.dto;

import lombok.Getter;
import lombok.Setter;

/** 부분 업데이트용: null 필드는 변경하지 않음 */
@Getter
@Setter
public class AlarmSettingsUpdateRequest {
    private Boolean routineEnabled;     // [NEW]
    private Boolean taskEnabled;        // [NEW]
    private Boolean medicationEnabled;  // [NEW]
}