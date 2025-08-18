package project.deepdot.routine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.deepdot.routine.api.common.ResponseCode;
import project.deepdot.routine.api.common.ResponseDto;
import project.deepdot.routine.api.common.ResponseMessage;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoutineResponseDto extends ResponseDto {

    private Long routineId;
    private Long goalId;
    private String goalName;

    // [NEW] 응답에 active 포함(선호 시)
    private Boolean active;

    public RoutineResponseDto(String code, String message, Long routineId, Long goalId, String goalName, Boolean active) {
        super(code, message);
        this.routineId = routineId;
        this.goalId = goalId;
        this.goalName = goalName;
        this.active = active;
    }

    public static RoutineResponseDto success(Long routineId, Long goalId, String goalName, Boolean active) {
        return new RoutineResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, routineId, goalId, goalName, active);
    }
}