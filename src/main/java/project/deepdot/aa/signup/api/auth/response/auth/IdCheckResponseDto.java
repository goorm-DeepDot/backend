package project.deepdot.aa.signup.api.auth.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.aa.signup.api.auth.response.ResponseDto;
import project.deepdot.aa.signup.api.common.ResponseCode;
import project.deepdot.aa.signup.api.common.ResponseMessage;

@Getter
public class IdCheckResponseDto extends ResponseDto {
    private IdCheckResponseDto(){
        super();
    }

    public static ResponseEntity<IdCheckResponseDto> success(){
        IdCheckResponseDto responseBody=new IdCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);

    }
    public static ResponseEntity<ResponseDto> duplicateId(){
        ResponseDto responseBody=new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
