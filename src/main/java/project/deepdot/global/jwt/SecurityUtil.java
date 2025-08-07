package project.deepdot.global.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityUtil {

    private SecurityUtil() {}

    // 현재 로그인한 사용자의 username (또는 email)을 가져옴
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("현재 로그인한 사용자가 없습니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername(); // username(email이 될 수도 있음)
        } else {
            return principal.toString(); // JWT에서 바로 username이 들어오는 경우
        }
    }
}