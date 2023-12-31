package com.founder.easy_route_assistant.config.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final RedisTemplate<String, String> redisTemplate;

    // Http 요청이 들어오면 가장 먼저 거치는 filter
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // Header에서 Token 가져오기
        String jwt = jwtProvider.resolveToken(request);

        // 토큰이 유효하다면,
        if ((jwt != null) && jwtProvider.validateToken(jwt)) {
            // 토큰으로부터 인증 정보를 받아
            Authentication authentication = jwtProvider.getAuthentication(jwt);
            // SecurityContext 객체에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response); // 다음 filter 실행
    }
}
