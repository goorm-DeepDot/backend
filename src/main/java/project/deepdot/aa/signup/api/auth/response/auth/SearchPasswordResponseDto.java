package project.deepdot.aa.signup.api.auth.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.aa.signup.api.common.ResponseCode;
import project.deepdot.aa.signup.api.common.ResponseMessage;
import project.deepdot.aa.signup.api.auth.response.ResponseDto;

@Getter
public class SearchPasswordResponseDto extends ResponseDto {

    public SearchPasswordResponseDto() {

    }

    public static ResponseEntity<ResponseDto> success(){
        ResponseDto responseBody=new ResponseDto(ResponseCode.SUCCESS,ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> fail() {
        ResponseDto responseBody=new ResponseDto(ResponseCode.MAIL_USER_NOT_EQUAL, ResponseMessage.MAIL_USER_NOT_EQUAL);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBody);
    }


}
