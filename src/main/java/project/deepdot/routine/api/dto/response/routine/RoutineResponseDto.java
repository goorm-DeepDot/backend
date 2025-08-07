package project.deepdot.routine.api.dto.response.routine;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.deepdot.aa.signup.api.common.ResponseCode;
import project.deepdot.aa.signup.api.common.ResponseMessage;
import project.deepdot.aa.signup.api.auth.response.ResponseDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoutineResponseDto extends ResponseDto {


    private Long routineId;   // 생성된 루틴 PK

    public RoutineResponseDto(String code, String message, Long routineId) {
        super(code, message);
        this.routineId = routineId;
    }

    public static RoutineResponseDto success(Long routineId) {
        return new RoutineResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, routineId);
    }

}
