package project.deepdot.medication.medication.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

//등록, 수정용
public class MedicationRequest {
    private String name;
    private boolean alarm;
    private Long userId;
}