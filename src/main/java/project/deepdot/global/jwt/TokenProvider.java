package project.deepdot.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.deepdot.user.userglobal.CustomUserDetailsService;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

// jwt를 사용하여 인증 토큰을 생성, 파싱, 검증하는 클래스
@Slf4j
@Component
public class TokenProvider {

    private final Key key;
    private final long accessTokenValidityInMillis;
    private final long refreshTokenValidityInMillis;
    private final CustomUserDetailsService customUserDetailsService;

    private static final String AUTHORITIES_KEY = "auth";

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-in-milliseconds}") String accessTokenValidityTime,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") String refreshTokenValidityTime,
            CustomUserDetailsService customUserDetailsService) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityInMillis = Long.parseLong(accessTokenValidityTime);
        this.refreshTokenValidityInMillis = Long.parseLong(refreshTokenValidityTime);
        this.customUserDetailsService = customUserDetailsService;
    }

    // access token 생성
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date expiry = new Date(now + accessTokenValidityInMillis);

        return Jwts.builder()
                .setSubject(authentication.getName()) // username
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refresh token 생성
    // username만 포함.권한 정보 없음
    public String createRefreshToken(Authentication authentication) {
        long now = new Date().getTime();
        Date expiry = new Date(now + refreshTokenValidityInMillis);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 username 추출
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // header에서 bearer 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰");
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 비어 있음");
        }
        return false;
    }

    // 클레임 파싱
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료되어도 claims는 꺼낼 수 있음
        }
    }

    //토큰 만료 여부 반환

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = parseClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}