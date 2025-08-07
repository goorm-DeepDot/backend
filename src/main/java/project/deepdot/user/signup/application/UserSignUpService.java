package project.deepdot.user.signup.application;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.user.signup.api.UserSignUpRequestDto;
import project.deepdot.user.user.domain.User;
import project.deepdot.user.user.domain.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public void signUp(UserSignUpRequestDto dto) {
        if (userRepository.existsByUsername(dto.toEntity().getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.existsByEmail(dto.toEntity().getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = dto.toEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
    }

    //로그인
}