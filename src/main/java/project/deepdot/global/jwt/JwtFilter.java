package project.deepdot.global.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> EXCLUDE = List.of(
            "/api/password/**",
            "/api/email/**",
            "/api/user/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/"
    );

    private boolean shouldSkip(HttpServletRequest req) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true;
        String uri = req.getRequestURI();
        return EXCLUDE.stream().anyMatch(p -> matcher.match(p, uri)); // 패턴 매칭
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (shouldSkip(request)) {
            chain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String bearer = request.getHeader("Authorization");
        String token = (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;

        // 토큰 없으면 그대로 통과 (permitAll/익명 접근 고려)
        if (token == null || token.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        // 토큰이 있으면: 유효하면 인증 세팅, 유효하지 않으면 즉시 401
        try {
            if (tokenProvider.validateToken(token)) {
                var auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token error");
            return;
        }

        chain.doFilter(request, response);
    }
}