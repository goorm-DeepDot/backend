package project.deepdot.user.application;

import org.springframework.http.ResponseEntity;
import project.deepdot.user.api.dto.request.auth.*;
import project.deepdot.user.api.dto.response.auth.*;

public interface AuthService {

    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertfication(EmailCertificationRequestDto dto);
    ResponseEntity<? super EmailCertification2ResponseDto> emailCertfication2(EmailCertification2RequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super IdSearchResponseDto> idSearch(IdSearchRequestDto dto);
    ResponseEntity<? super SearchPasswordResponseDto> searchPassword(SearchPasswordRequestDto dto);
    ResponseEntity<? super ResetPasswordResponseDto> resetPassword(ResetPasswordRequestDto dto);


}
