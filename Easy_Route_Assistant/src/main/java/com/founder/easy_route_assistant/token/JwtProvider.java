package com.founder.easy_route_assistant.token;

import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.security.Role;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private String secretKey = "ERA jwt secret key";
    private final long tokenValidTime = 60 * 60 * 1000L; // 유효 시간 60분

    private final UserDetailsService userDetailsService;

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
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserID(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // token에서 userID 뽑기
    public String getUserID(String token) {
        // return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return extractClaims(token, secretKey).get("userID").toString();
    }
    public String getRole(String token) {
        return extractClaims(token, secretKey).get("role").toString();
    }
    private Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // 토큰 유효성, 만료 일자 확인
    public boolean validateToken(String jwt) {
        /*try {
            if (!jwt.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                jwt = jwt.split(" ")[1].trim();
            }

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }*/

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return true;
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

    // request의 header에서 token 가져오기 -> ⭐여기 더 봐야 함
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /*public String resolveToken(HttpServletRequest request) {
        return request.getHeader("jwt");
    }*/

}
