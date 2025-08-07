package project.deepdot.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

//자체로그인관련 enable추가하였음
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;

    // 비밀번호 암호화 방식: BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // securityfilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                //기본 로그인, csrf,form,logout 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않음 (JWT 사용 → STATELESS)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(configurationSource()))

                // 인가 규칙
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/auth/**",                    // 소셜 로그인
                                "/logOut",                     // 로그아웃
                                "/swagger-ui/**",              // Swagger UI
                                "/v3/api-docs/**",             // Swagger Docs
                                "/api/hello",                  // 테스트
                                "/api/authenticate",           // 로그인
                                "/api/signup",                 // 회원가입
                                "/api/id-check",               // 아이디 중복확인
                                "/api/email-certification",    // 이메일 인증 요청
                                "/api/email-certification2",   // 이메일 인증 재요청
                                "/api/check-certification",    // 인증코드 확인
                                "/api/search-id",              // 아이디 찾기
                                "/api/search-password",        // 비밀번호 찾기
                                "/api/reset-password",         // 비밀번호 재설정
                                "/public/**",                  // 기타 공개 API
                                "/"                             // 루트 페이지 허용 (Postman 등)
                        ).permitAll()
                        // .requestMatchers().hasRole("ADMIN") // 관리자만 해당 URL에 접근 가능

                        // 특정 요청 인증 필요
                        .requestMatchers("/api/routine").authenticated()

                        // 나머지 요청은 모두 인증 필요
                        .anyRequest()
                        .authenticated()
                )
                // JWT 필터 등록 (UsernamePasswordAuthenticationFilter 전에 동작)
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",     // 프론트
                "https://deepdot.zapto.org"  // 백엔드 배포 서버
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