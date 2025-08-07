package project.deepdot.aa.signup.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.deepdot.global.jwt.JwtFilter;
import project.deepdot.global.jwt.TokenProvider;
import project.deepdot.aa.signup.api.auth.*;
import project.deepdot.aa.signup.api.auth.response.auth.*;
import project.deepdot.aa.signup.api.dto.TokenDto;
import project.deepdot.user.login.api.UserLoginRequestDto;
import project.deepdot.aa.signup.application.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;


    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody UserLoginRequestDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(
            @RequestBody @Valid IdCheckRequestDto requestBody
            ){
        ResponseEntity<? super IdCheckResponseDto> response=authService.idCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
    ){
        ResponseEntity<? super EmailCertificationResponseDto> response=authService.emailCertfication(requestBody);
        return response;

    }

    @PostMapping("/email-certification2")
    public ResponseEntity<? super EmailCertification2ResponseDto> emailCertification2(
            @RequestBody @Valid EmailCertification2RequestDto requestBody
    ){
        ResponseEntity<? super EmailCertification2ResponseDto> response=authService.emailCertfication2(requestBody);
        return response;

    }


    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationRequestDto requestBody
    ){
        ResponseEntity<? super CheckCertificationResponseDto> response=authService.checkCertification(requestBody);
        return response;
    }


    @PostMapping("/search-id")
    public ResponseEntity<? super IdSearchResponseDto> searchId(
            @RequestBody @Valid IdSearchRequestDto requestBody
    ) {
        ResponseEntity<? super IdSearchResponseDto> response=authService.idSearch(requestBody);
        return response;
    }

    @PostMapping("/search-password")
    public ResponseEntity<? super SearchPasswordResponseDto> searchPassword(
            @RequestBody @Valid SearchPasswordRequestDto requestBody
    ){
        ResponseEntity<? super SearchPasswordResponseDto> response=authService.searchPassword(requestBody);

        return response;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<? super ResetPasswordResponseDto> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDto requestBody
    ){
        ResponseEntity<? super ResetPasswordResponseDto> response=authService.resetPassword(requestBody);
        return response;
    }


}