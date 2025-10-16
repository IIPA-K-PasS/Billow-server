package com.k_passs.backend.domain.term.converter;

import com.k_passs.backend.domain.term.dto.request.TermRequestDTO;
import com.k_passs.backend.domain.term.dto.response.TermResponseDTO;

import java.util.List;

public class TermConverter {
    /**
     * 약관 동의 요청 DTO를 응답 DTO로 변환
     * @param userId 동의를 처리한 사용자 ID
     * @param request 약관 동의 요청 목록
     * @return 약관 동의 처리 결과를 포함하는 응답 DTO
     */
    public static TermResponseDTO.TermAgreeResult toTermAgreeResult(
            Long userId,
            TermRequestDTO.TermAgreeRequest request
    ) {
        List<TermResponseDTO.TermAgreementResult> results = request.getTerms().stream()
                .map(term -> TermResponseDTO.TermAgreementResult.builder()
                        .termId(term.getTermId())
                        .agreed(term.getAgreed())
                        .build())
                .toList();

        return TermResponseDTO.TermAgreeResult.builder()
                .userId(userId)
                .terms(results)
                .build();
    }
}
