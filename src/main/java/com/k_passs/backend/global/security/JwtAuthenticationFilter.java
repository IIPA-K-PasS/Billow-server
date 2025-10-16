package com.k_passs.backend.global.security;

import com.k_passs.backend.domain.user.repository.UserRepository;
import com.k_passs.backend.global.jwt.JwtProvider;
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
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.trim().toLowerCase().startsWith("bearer ")) {
            String token = authHeader.trim().substring(7).trim();

            if (jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserId(token);

                // ifPresent를 사용하면 null 체크를 더 깔끔하게 할 수 있습니다.
                userRepository.findById(userId).ifPresent(user -> {
                    // Principal로 CustomUserDetails 객체를 생성할 때, User 엔티티 객체 자체를 전달.
                    CustomUserDetails principal = new CustomUserDetails(user);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, null); // 권한이 필요 없다면 세 번째 인자는 비워둡니다.

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }
        filterChain.doFilter(request, response);
    }
}
