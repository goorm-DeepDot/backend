package project.deepdot.medication.medication.api;

import lombok.Getter;
import project.deepdot.medication.medication.domain.Medication;

import java.time.LocalTime;
import java.util.List;

//조회용
@Getter
public class MedicationResponse {
    private Long medicationId;
    private Long userId;
    private String name;
    private boolean alarm;
    private List<LocalTime> times;

    public MedicationResponse(Medication medication, List<LocalTime> times) {
        this.medicationId = medication.getId();
        this.userId = medication.getUser().getUserId();
        this.name = medication.getName();
        this.alarm = medication.isAlarm();
        this.times = times;
    }
}
