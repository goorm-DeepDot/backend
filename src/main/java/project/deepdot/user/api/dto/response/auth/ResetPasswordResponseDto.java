package project.deepdot.user.api.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.user.api.common.ResponseCode;
import project.deepdot.user.api.common.ResponseMessage;
import project.deepdot.user.api.dto.response.ResponseDto;

@Getter
public class ResetPasswordResponseDto extends ResponseDto {
    public ResetPasswordResponseDto() {

    }


    public static ResponseEntity<ResponseDto> success(){
        ResponseDto responseBody=new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> fail() {
        ResponseDto responseBody=new ResponseDto(ResponseCode.NEW_OLD_PASSWORD_SAME, ResponseMessage.NEW_OLD_PASSWORD_SAME);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBody);
    }


}
