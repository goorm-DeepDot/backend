package project.deepdot.setting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.setting.api.dto.EmailUpdateRequest;
import project.deepdot.setting.api.dto.ProfileResponse;
import project.deepdot.setting.api.dto.UsernameUpdateRequest;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public ProfileResponse me(Long userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new ProfileResponse(u.getUsername(), u.getEmail());
    }

    @Transactional
    public ProfileResponse updateUsername(Long userId, UsernameUpdateRequest req) {
        String newUsername = req.getUsername().trim();

        // 같은 username을 가진 다른 유저가 있는지 확인
        userRepo.findByUsername(newUsername).ifPresent(other -> {
            if (!other.getUserId().equals(userId)) {
                throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
            }
        });

        User me = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 변경 없음 최적화(선택)
        if (newUsername.equalsIgnoreCase(me.getUsername())) {
            return new ProfileResponse(me.getUsername(), me.getEmail());
        }

        me.changeUsername(newUsername);
        return new ProfileResponse(me.getUsername(), me.getEmail());
    }

    @Transactional
    public ProfileResponse updateEmail(Long userId, EmailUpdateRequest req) {
        String newEmail = req.getEmail().trim();

        // 같은 email을 가진 다른 유저가 있는지 확인
        userRepo.findByEmail(newEmail).ifPresent(other -> {
            if (!other.getUserId().equals(userId)) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
        });

        User me = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (newEmail.equalsIgnoreCase(me.getEmail())) {
            return new ProfileResponse(me.getUsername(), me.getEmail());
        }

        me.changeEmail(newEmail);
        return new ProfileResponse(me.getUsername(), me.getEmail());
    }
}
