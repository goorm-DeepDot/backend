package project.deepdot.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> EXCLUDE = List.of(
            "/", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/email/**", "/api/password/**",
            "/api/user/**",
            "/api/authenticate", "/api/signup" // 모바일네트워크오류 ★ 추가: 토큰 없이 접근해야 하는 엔드포인트
    );

    private boolean shouldSkip(HttpServletRequest req) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true;
        String path = req.getServletPath();
        return EXCLUDE.stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String bearer = request.getHeader("Authorization");
        log.debug("Authorization header = {}", bearer);  //dj 실제로 오는지 확인

        if (shouldSkip(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;

        if (token == null || token.isBlank()) {
            log.debug("No token -> proceed anonymous");
            chain.doFilter(request, response);
            return;
        }

        try {
            if (tokenProvider.validateToken(token)) {
                var auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("JWT validated, authenticated as {}", auth.getName());
            } else {
                log.debug("JWT invalid -> anonymous");
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            log.warn("JWT processing error", e);
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}