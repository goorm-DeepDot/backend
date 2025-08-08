package project.deepdot.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.deepdot.global.jwt.JwtFilter;
import project.deepdot.global.jwt.TokenProvider;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // 비밀번호 암호화 방식: BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 기본 로그인, csrf, form, logout 등 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않음 (JWT 사용 → STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(configurationSource()))

                // 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/user/**",            // 회원가입, 이메일 인증 등
                                "/auth/**",                // 소셜 로그인
                                "/logOut",                 // 로그아웃
                                "/swagger-ui/**",          // Swagger
                                "/v3/api-docs/**",
                                "/public/**",
                                "/"
                        ).permitAll()

                        // 나머지 API는 인증 필요
                        .requestMatchers("/api/**").authenticated()

                        // 혹시 누락된 경로가 있다면 여기서 인증 필요로 묶임
                        .anyRequest().authenticated()
                )

                // JWT 필터 등록 (UsernamePasswordAuthenticationFilter 전에 동작)
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // CORS 설정 (프론트엔드 도메인 허용)
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",     // 로컬 개발 환경
                "https://deepdot.zapto.org"  // 배포된 프론트엔드
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Cache-Control", "Content-Type",
                "X-Requested-With", "Origin", "Accept"
        ));
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true); // 쿠키 허용 (필요 시)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
