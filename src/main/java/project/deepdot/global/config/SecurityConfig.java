package project.deepdot.global.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.deepdot.global.jwt.JwtFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import project.deepdot.global.jwt.TokenProvider;
import project.deepdot.user.domain.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // JwtFilter를 빈으로 등록해서 재사용
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(tokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(configurationSource()))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                                          res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((req, res, e) ->
                                                     res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )
                .authorizeHttpRequests(auth -> auth
                        // CORS 프리플라이트
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 완전 공개
                        .requestMatchers("/", "/error", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 이메일/비번 재설정 관련 공개 엔드포인트
                        .requestMatchers("/api/email/**").permitAll()
                        .requestMatchers("/api/password-reset/**").permitAll() // password-reset 경로 추가
                        // (기존 "/api/password/**"를 쓰고 있다면 둘 다 열어두세요)
                        .requestMatchers("/api/password/**").permitAll()

                        // 필요 시 공개할 사용자 API만 선택적으로 열기 (지금은 전체 열려있음)
                        .requestMatchers("/api/user/**").permitAll()

                        // 메인 조회만 공개
                        .requestMatchers(HttpMethod.GET, "/api/mainpage/**").permitAll()

                        // 인증 필요 구간
                        // 토큰에 ROLE_USER/ROLE_ADMIN을 싣는다면 hasAnyRole 사용, 아니면 authenticated() 사용
                        .requestMatchers("/api/medication/**", "/api/schedule/**").hasAnyRole("USER","ADMIN")
                        // .requestMatchers("/api/medication/**", "/api/schedule/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "https://deepdot.zapto.org"
        ));
        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList(
                "Authorization","Cache-Control","Content-Type","X-Requested-With","Origin","Accept"
        ));
        cfg.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        // cfg.setAllowCredentials(true); // 쿠키/자격증명 필요 시만 활성화

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}