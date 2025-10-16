package com.k_passs.backend.domain.term.entity;

import com.k_passs.backend.domain.model.entity.BaseEntity;
import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "terms")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title; // 약관 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 약관 내용

    @Column(nullable = false)
    private Boolean isRequired; // 필수 동의 여부

    @Column(nullable = false)
    private Integer version; // 약관 버전
}