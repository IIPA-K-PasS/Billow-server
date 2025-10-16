package com.k_passs.backend.domain.term.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * 서비스 약관 엔티티를 관리하는 Repository
 */
public interface TermRepository extends JpaRepository<Term, Integer> { // Integer는 Term의 ID 타입

    // 모든 필수 약관을 조회
    List<Term> findByIsRequired(Boolean isRequired);

    // 주어진 ID 목록에 해당하는 모든 약관을 조회
    List<Term> findByIdIn(Set<Integer> termIds);

    // 특정 ID 목록과 필수 여부가 일치하는 약관들을 조회
    @Query("SELECT t.id FROM Term t WHERE t.isRequired = TRUE")
    Set<Integer> findAllRequiredTermIds();
}
