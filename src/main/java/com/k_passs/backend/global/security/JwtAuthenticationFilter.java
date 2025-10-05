package com.k_passs.backend.global.security;

import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import com.k_passs.backend.global.jwt.JwtProvider;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // 매 요청마다 JWT 유효성 검증 및 인증 정보를 SecurityContext에 설정
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
        // [핵심 수정!] /actuator/** 하위 경로에 대한 요청은 JWT 검증을 건너뛰도록 설정합니다.
        // 이것은 쿠버네티스의 헬스 체크(livenessProbe, readinessProbe)가 정상적으로 작동하기 위함입니다.
        if (request.getRequestURI().startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

        System.out.println("JwtAuthenticationFilter 작동 시작");

        // Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization 헤더 수신값: [" + authHeader + "]");

        // 헤더가 존재하고 Bearer 토큰 형식인지 확인
        if (authHeader != null && authHeader.trim().toLowerCase().startsWith("bearer ")) {
            String token = authHeader.trim().substring(7).trim();
            System.out.println("받은 토큰: " + token);

            // 토큰 유효성 검증
            if (jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserId(token);
                System.out.println("토큰 유효, userId: " + userId);

                // DB에서 사용자 조회
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    // CustomOAuth2User 생성 및 Spring Security 인증 객체 구성
                    CustomOAuth2User principal = new CustomOAuth2User(user);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    // 인증 세부 정보 설정 및 SecurityContext에 저장
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("SecurityContext에 인증 저장 완료");
                } else {
                    System.out.println("유저 없음");
                }
            } else {
                System.out.println("유효하지 않은 JWT");
            }
        } else {
            System.out.println("Authorization 헤더 없음 또는 형식 오류");
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
