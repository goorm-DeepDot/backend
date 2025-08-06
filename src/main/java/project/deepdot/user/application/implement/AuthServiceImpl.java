package project.deepdot.user.application.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.user.api.common.CertificationNumber;
import project.deepdot.user.api.dto.request.auth.*;
import project.deepdot.user.api.dto.response.ResponseDto;
import project.deepdot.user.api.dto.response.auth.*;
import project.deepdot.user.application.AuthService;
import project.deepdot.user.domain.Certification;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.CertificationRepository;
import project.deepdot.user.domain.repository.UserRepository;
import project.deepdot.user.provider.EmailProvider;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;
    private final PasswordEncoder passwordEncoder;




    @Override
    @Transactional
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try{
            String userId=dto.getUsername();
            boolean isExistId=userRepository.existsByUsername(userId);
            if(isExistId)return IdCheckResponseDto.duplicateId();

        } catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return IdCheckResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertfication(EmailCertificationRequestDto dto) {
        try{
            String userId=dto.getUsername();
            String email=dto.getEmail();

            //boolean isExistId=userRepository.existsByUsername(userId);
            //if(isExistId) return EmailCertificationResponseDto.duplicateId();

            String certificationNumber= CertificationNumber.getCertificationNumber();
            boolean isSuccessed=emailProvider.sendCertificationMail(email,certificationNumber);
            if(!isSuccessed)return EmailCertificationResponseDto.mailSendFail();
            Certification certification=new Certification(userId,email,certificationNumber);
            certificationRepository.save(certification);

        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super EmailCertification2ResponseDto> emailCertfication2(EmailCertification2RequestDto dto) {
        try{
            String email=dto.getEmail();
            String certificationNumber= CertificationNumber.getCertificationNumber();
            boolean isSuccessed=emailProvider.sendCertificationMail(email,certificationNumber);
            if(!isSuccessed)return EmailCertificationResponseDto.mailSendFail();



        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();

        }
        return EmailCertification2ResponseDto.success();

    }

    @Override
    @Transactional
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try{
            String username=dto.getUsername();
            String email=dto.getEmail();
            String certificationNumber= dto.getCertificationNumber();

            Certification certification=certificationRepository.findByUserId(username);
            if(certification==null)return CheckCertificationResponseDto.certificationFail();

            boolean isMatched=certification.getEmail().equals(email)&&certification.getCertificationNumber().equals(certificationNumber);
            if(!isMatched) return CheckCertificationResponseDto.certificationFail();

            return CheckCertificationResponseDto.success();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<? super IdSearchResponseDto> idSearch(IdSearchRequestDto dto) {
        try {
            String email = dto.getEmail();

            // 이메일로 사용자 조회
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return IdSearchResponseDto.notFound();
            }
            return IdSearchResponseDto.success(user.get().getUsername());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }


    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<? super SearchPasswordResponseDto> searchPassword(SearchPasswordRequestDto dto) {
        try{
            String username=dto.getUsername();
            String email=dto.getEmail();
            // 사용자 조회
            Optional<User> userOptional = userRepository.findByUsernameAndEmail(username, email);
            // 존재하지 않으면 실패 응답
            if (userOptional.isEmpty()) {
                return SearchPasswordResponseDto.fail();
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SearchPasswordResponseDto.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<? super ResetPasswordResponseDto> resetPassword(ResetPasswordRequestDto dto) {
        try{
            // 1. 사용자 조회
            User user = userRepository.findByUsername(dto.getUsername());
            if (user == null) {
                throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
            }

            // 2. 기존 비밀번호와 새 비밀번호가 같은지 비교
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return ResetPasswordResponseDto.fail();
            }

            // 3. 새 비밀번호 암호화 및 저장
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.save(user);

            return ResetPasswordResponseDto.success();

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

    }




}
