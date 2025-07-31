package project.deepdot.user.api.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.user.api.common.ResponseCode;
import project.deepdot.user.api.common.ResponseMessage;
import project.deepdot.user.api.dto.response.ResponseDto;

@Getter
public class IdSearchResponseDto extends ResponseDto {
    private IdSearchResponseDto(String username){
        this.username = username;
    }

    private String username;


    public static ResponseEntity<IdSearchResponseDto> success(String username){
        IdSearchResponseDto responseBody=new IdSearchResponseDto(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


    public static ResponseEntity<ResponseDto> notVerified() {
        ResponseDto responseBody=new ResponseDto(ResponseCode.MAIL_NOT_VERIFIED, ResponseMessage.MAIL_NOT_VERIFIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notFound() {
        ResponseDto responseBody=new ResponseDto(ResponseCode.MAIL_NOT_FOUND, ResponseMessage.MAIL_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBody);
    }





}
