package project.deepdot.routine.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponseDto {
    private Long goalId;
    private String name;
    private boolean inUse; // 하나라도 루틴이 참조 중이면 true
}