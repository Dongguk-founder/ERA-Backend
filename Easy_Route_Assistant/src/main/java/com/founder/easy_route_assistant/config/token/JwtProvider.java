package com.founder.easy_route_assistant.config.token;

import com.founder.easy_route_assistant.config.PrincipalDetails;
import com.founder.easy_route_assistant.config.PrincipalDetailsService;
import com.founder.easy_route_assistant.config.Role;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private String secretKey = "ERA jwt secret key";
    private final long tokenValidTime = 60 * 60 * 1000L; // 유효 시간 60분

    private final PrincipalDetailsService principalDetailsService;

    private final RedisTemplate<String, String> redisTemplate;

    // 객체 초기화 시 secretKey를 Base64로 encoding
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 생성
    public String createToken(String userID, Role role) { // List<String> roles
        Claims claims = Jwts.claims(); // JWT payload에 저장되는 정보 단위
        claims.put("userID", userID);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 인증 정보 조회
    public Authentication getAuthentication(String jwt) {
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(this.getUserID(jwt));
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    // token에서 userID, role 뽑기
    public String getUserID(String jwt) {
        return extractClaims(jwt, secretKey).get("userID").toString();
    }
    public String getRole(String jwt) {
        return extractClaims(jwt, secretKey).get("role").toString();
    }
    private Claims extractClaims(String jwt, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }

    public Long getExpiration(String jwt) {
        Date expiration = extractClaims(jwt, secretKey).getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    // 토큰 유효성, 만료 일자 확인
    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);

            String black = redisTemplate.opsForValue().get(jwt);

            if (StringUtils.hasText(black)) {
                throw new IllegalAccessException();
            }

            return true;
        } catch (IllegalAccessException e) {
            logger.info("로그아웃한 사용자");
        } catch (MalformedJwtException e) {
            logger.info("잘못된 JWT 서명");
        } catch(ExpiredJwtException e) {
            logger.info("만료된 JWT");
        } catch (UnsupportedJwtException e) {
            logger.info("지원하지 않는 JWT");
        } catch (IllegalArgumentException e) {
            logger.info("잘못된 JWT");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("jwt");
    }

}