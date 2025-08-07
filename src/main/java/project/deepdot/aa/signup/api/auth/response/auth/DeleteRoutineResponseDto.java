package project.deepdot.aa.signup.api.auth.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.aa.signup.api.common.ResponseCode;
import project.deepdot.aa.signup.api.common.ResponseMessage;
import project.deepdot.aa.signup.api.auth.response.ResponseDto;


@Getter
public class DeleteRoutineResponseDto extends ResponseDto {
    private DeleteRoutineResponseDto(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<ResponseDto> success(){
        ResponseDto responseBody=new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> fail() {
        ResponseDto responseBody=new ResponseDto(ResponseCode.ROUTINE_DELETE_FAIL, ResponseMessage.ROUTINE_DELETE_FAIL);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBody);
    }

}
