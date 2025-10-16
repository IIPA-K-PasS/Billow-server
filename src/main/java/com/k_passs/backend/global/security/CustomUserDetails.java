package com.k_passs.backend.global.security;

import com.k_passs.backend.domain.user.entity.User; // User 엔티티 import
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter // Lombok을 사용하여 getUserId()와 getUser() 메소드를 자동으로 생성
public class CustomUserDetails implements UserDetails {

    // User 엔티티 객체를 저장으로 수정
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 편의상 userId를 반환하는 메소드 (Controller에서 @AuthenticationPrincipal 대신 사용 가능)
    public Long getUserId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // JWT 방식에서는 비밀번호를 사용하지 않으므로 null 반환.
        return null;
    }

    @Override
    public String getUsername() {
        // Spring Security에서 사용자를 식별하는 기본값. 우리 서비스의 userId를 문자열로 변환하여 반환.
        return String.valueOf(user.getId());
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