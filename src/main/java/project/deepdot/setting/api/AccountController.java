package project.deepdot.setting.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.setting.application.AccountService;
import project.deepdot.setting.application.TokenBlacklistService;
import project.deepdot.user.domain.UserPrincipal;

@RestController
@RequestMapping("/api/settings/account")
@RequiredArgsConstructor
public class AccountController {


    private final TokenBlacklistService blacklist;

    /**
     * 액세스 토큰을 블랙리스트에 넣어 남은 시간 동안 무효화.
     * 클라이언트는 버튼 누르면 로컬 저장소의 토큰도 삭제해야 함.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        String token = extractBearer(authHeader);
        // 액세스 토큰 TTL에 맞게 조정 (예: 15분 = 900초)
        if (token != null) {
            blacklist.blacklist(token, 900);
        }
        return ResponseEntity.noContent().build();
    }

    private String extractBearer(String header) {
        if (header == null) return null;
        if (header.startsWith("Bearer ")) return header.substring(7);
        return null;
    }

    private final AccountService svc;

    /** 회원 탈퇴 (내 계정 + 연관 데이터 삭제) */
    @DeleteMapping
    public ResponseEntity<Void> deleteMyAccount(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Long userId = principal.getUser().getUserId();
        svc.deleteAccount(userId, authHeader);
        return ResponseEntity.noContent().build();
    }


}
