package project.deepdot.aa.signup.api.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.aa.signup.api.common.ResponseCode;
import project.deepdot.aa.signup.api.common.ResponseMessage;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private String code;
    private String message;

    public ResponseDto(){
        this.code= ResponseCode.SUCCESS;
        this.message= ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDto> databaseError(){
        ResponseDto resonseBody=new ResponseDto(ResponseCode.DATABASE_ERROR,ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resonseBody);
    }

    public static ResponseEntity<ResponseDto> validationFail(){
        ResponseDto resonseBody=new ResponseDto(ResponseCode.VALIDATION_FAIL,ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resonseBody);
    }

}
