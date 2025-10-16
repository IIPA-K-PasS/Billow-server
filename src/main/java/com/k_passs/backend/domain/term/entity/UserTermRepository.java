package com.k_passs.backend.domain.term.entity;

import com.k_passs.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자의 약관 동의 상태를 관리하는 Repository
 */
public interface UserTermRepository extends JpaRepository<UserTerm, Long> {
    Optional<UserTerm> findByUserAndTerm(User user, Term term);

    List<UserTerm> findByUser(User user);

    void deleteByUser(User user);
}
