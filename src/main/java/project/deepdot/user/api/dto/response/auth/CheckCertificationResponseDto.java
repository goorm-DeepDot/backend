package project.deepdot.user.api.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.deepdot.user.api.common.ResponseCode;
import project.deepdot.user.api.common.ResponseMessage;
import project.deepdot.user.api.dto.response.ResponseDto;

@Getter
public class CheckCertificationResponseDto extends ResponseDto {

    private CheckCertificationResponseDto(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<CheckCertificationResponseDto> success() {
        return ResponseEntity.ok(new CheckCertificationResponseDto(
                ResponseCode.SUCCESS, ResponseMessage.SUCCESS
        ));
    }

    public static ResponseEntity<CheckCertificationResponseDto> certificationFail() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CheckCertificationResponseDto(
                        ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL
                ));
    }





}
