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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private static final Set<String> EXCLUDE = Set.of(
            "/api/email/send-code",
            "/api/email/verify-signup",
            "/api/email/verify-id",
            "/swagger-ui",
            "/v3/api-docs"
    );

    private boolean shouldSkip(HttpServletRequest req) {
        String uri = req.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true;
        // prefix 매칭으로 느슨하게
        return EXCLUDE.stream().anyMatch(uri::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (shouldSkip(request)) {
            chain.doFilter(request, response);
            return;
        }

        String bearer = request.getHeader("Authorization");
        String token = (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;

        if (token == null || token.isBlank()) {
            // 토큰 없으면 그냥 통과 (permitAll 경로/익명 접근 허용을 위해)
            chain.doFilter(request, response);
            return;
        }

        try {
            if (tokenProvider.validateToken(token)) {
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                // 명확히 틀린 토큰이면 401 (원하면 그냥 통과시켜도 됨)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        } catch (Exception e) {
            // 예외도 401로
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token error");
            return;
        }

        chain.doFilter(request, response);
    }
}