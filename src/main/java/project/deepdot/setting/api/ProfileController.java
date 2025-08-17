package project.deepdot.setting.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.setting.api.dto.EmailUpdateRequest;
import project.deepdot.setting.api.dto.ProfileResponse;
import project.deepdot.setting.api.dto.UsernameUpdateRequest;
import project.deepdot.setting.application.ProfileService;
import project.deepdot.user.domain.UserPrincipal;

@RestController
@RequestMapping("/api/settings/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService svc;

    /** 현재 내 프로필(아이디/이메일) 조회 */
    @GetMapping
    public ResponseEntity<ProfileResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(svc.me(userId));
    }

    /** 아이디만 변경 */
    @PatchMapping("/username")
    public ResponseEntity<ProfileResponse> updateUsername(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UsernameUpdateRequest req) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(svc.updateUsername(userId, req));
    }

    /** 이메일만 변경 */
    @PatchMapping("/email")
    public ResponseEntity<ProfileResponse> updateEmail(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody EmailUpdateRequest req) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(svc.updateEmail(userId, req));
    }
}