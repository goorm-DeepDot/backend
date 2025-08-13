package project.deepdot.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.deepdot.user.domain.User;
import project.deepdot.user.application.CustomUserDetailsService;
import io.jsonwebtoken.security.SecurityException;
import project.deepdot.user.domain.UserPrincipal;
import project.deepdot.user.domain.repository.UserRepository;

import java.security.Key;
import java.util.Date;

// jwt를 사용하여 인증 토큰을 생성, 파싱, 검증하는 클래스
@Slf4j
@Component
public class TokenProvider {
    private final Key key; // jwt 서명을 위한 비밀 키. 토큰을 생성하고 검증할 때 사용
    private final long accessTokenValidityTime; // 액세스 토큰의 유효 시간 정의
    private final long refreshTokenValidityTime;
    private UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime,
                         @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityTime,
                         CustomUserDetailsService customUserDetailsService,
                         UserRepository userRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);    // secretKey를 Base64 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    //토큰 발급 시 sub를 userId로
    public String createAccessToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setExpiration(new Date(now + accessTokenValidityTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())   // 똑같이 userId -> username
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + refreshTokenValidityTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String sub = getUserPk(token); // sub = userId
        Long userId = Long.parseLong(sub);

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        UserPrincipal userPrincipal = new UserPrincipal(userEntity);

        return new UsernamePasswordAuthenticationToken(userPrincipal, "", userPrincipal.getAuthorities());
    }


    private String getUserPk(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // HTTP 요청에서 토큰 추출(Bearer 라는 접두사 제거하고 실제 토큰 반환)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // HTTP 헤더에서 토큰 추출

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 문자열을 제거하고 토큰 반환
        }

        // Bearer 토큰이란: Authorization 헤더에 포함되어 사용자의 인증 상태를 서버에 전달하기 위해 사용됨.
        // 즉, 토큰이 인증 수단으로 사용됨을 나타냄.

        return null;
    }

    // 토큰 검증(토큰의 유효기간이 지났는지, 구조가 올바른지 등 체크)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {              // 서명 불일치/위조
            log.warn("JWT signature invalid", e);
        } catch (MalformedJwtException e) {          // 포맷 오류
            log.warn("JWT malformed", e);
        } catch (ExpiredJwtException e) {            // 만료
            log.warn("JWT expired", e);
        } catch (UnsupportedJwtException e) {
            log.warn("JWT unsupported", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT illegal argument (empty or null)", e);
        }
        return false;
    }

    // 클레임 파싱
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Claims getClaimsFromToken(String token) {
        return parseClaims(token);
    }
}