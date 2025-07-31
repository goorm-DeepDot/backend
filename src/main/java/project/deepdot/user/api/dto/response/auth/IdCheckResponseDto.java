package project.deepdot.user.api.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.user.api.common.ResponseCode;
import project.deepdot.user.api.common.ResponseMessage;
import project.deepdot.user.api.dto.response.ResponseDto;

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
