package project.deepdot.alarm.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MedicationAlarmResponse {
    private Long id;
    private String medicationName;
    private boolean active;
    private List<String> times; // "08:00", "12:00", ...
}