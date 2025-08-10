package project.deepdot.global.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private static final AntPathMatcher matcher = new AntPathMatcher();

    //  패턴으로 전부 허용
    private static final List<String> EXCLUDE_PATTERNS = List.of(
            "/api/email/**",
            "/api/password/**",   //  비번 재설정 전체
            "/api/user/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/"                    // 필요 시
    );

    private boolean isExcluded(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true; // CORS preflight
        String uri = request.getRequestURI();
        for (String p : EXCLUDE_PATTERNS) {
            if (matcher.match(p, uri)) return true;   // equals → 패턴 매칭
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (isExcluded(request)) {
            chain.doFilter(request, response); //  화이트리스트면 토큰 검사 자체 스킵
            return;
        }

        String auth = request.getHeader("Authorization");
        String jwt = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;

        if (jwt != null && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}