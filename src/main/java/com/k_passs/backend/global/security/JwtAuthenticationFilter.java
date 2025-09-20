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
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.trim().toLowerCase().startsWith("bearer ")) {
            String token = authHeader.trim().substring(7).trim();

            if (jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserId(token);

                // ifPresent를 사용하면 null 체크를 더 깔끔하게 할 수 있습니다.
                userRepository.findById(userId).ifPresent(user -> {
                    // [수정] Principal로 CustomUserDetails 객체를 생성합니다.
                    CustomUserDetails principal = new CustomUserDetails(user.getId());

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