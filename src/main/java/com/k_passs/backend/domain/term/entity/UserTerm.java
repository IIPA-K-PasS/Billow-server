package com.k_passs.backend.domain.term.entity;

import com.k_passs.backend.domain.model.entity.BaseEntity;
import com.k_passs.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

@Entity
@Table(name = "user_terms", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "term_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTerm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term; // 동의한 약관

    @Column(nullable = false)
    private Boolean agreed;

    @Builder
    public UserTerm(User user, Term term, Boolean agreed) {
        this.user = user;
        this.term = term;
        this.agreed = agreed;
    }

    public void updateAgreed(Boolean agreed) {
        this.agreed = agreed;
    }
}
