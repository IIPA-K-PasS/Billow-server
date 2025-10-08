package com.k_passs.backend.domain.user.entity;

import com.k_passs.backend.domain.bill.entity.Bill;
import com.k_passs.backend.domain.model.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Setter
    private String nickname;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column
    private String profileImageUrl;

    @Column
    @Builder.Default
    private Long point = 0L; // 포인트 필드에 기본값 설정

    // User는 여러 Bill을 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Bill> bills = new ArrayList<>();

    // User는 여러 PointHistory를 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PointHistory> pointHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserChallenge> userChallenges = new ArrayList<>();
}