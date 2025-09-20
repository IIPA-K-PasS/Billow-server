package com.k_passs.backend.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter // Lombok을 사용하여 getUserId() 메소드를 자동으로 생성
public class CustomUserDetails implements UserDetails {

    // 우리 애플리케이션에서 사용할 고유한 사용자 정보
    private final Long userId;

    public CustomUserDetails(Long userId) {
        this.userId = userId;
    }

    // --- UserDetails 인터페이스의 메소드 구현 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자 권한(Role) 목록을 반환하는 곳. 지금은 사용하지 않으므로 null 반환.
        return null;
    }

    @Override
    public String getPassword() {
        // JWT 방식에서는 비밀번호를 사용하지 않으므로 null 반환.
        return null;
    }

    @Override
    public String getUsername() {
        // Spring Security에서 사용자를 식별하는 기본값. 우리 서비스의 userId를 문자열로 변환하여 반환.
        return String.valueOf(userId);
    }

    // 아래 4개 메소드는 계정 상태를 관리하는 곳. 지금은 모두 true로 설정하여 항상 활성화 상태로 둡니다.
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호 등)이 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화됨
    }
}