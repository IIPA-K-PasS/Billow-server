package com.k_passs.backend.domain.term.converter;

import com.k_passs.backend.domain.term.dto.request.TermRequestDTO;
import com.k_passs.backend.domain.term.dto.response.TermResponseDTO;
import com.k_passs.backend.domain.term.entity.Term;

import java.util.List;
import java.util.Map;

public class TermConverter {

    public static TermResponseDTO.GetAgreeResult toGetAllTermsResult(
            List<Term> allTerms,
            Map<Integer, Boolean> userAgreements
    ) {
        List<TermResponseDTO.AllAgreement> results = allTerms.stream()
                .map(term -> TermResponseDTO.AllAgreement.builder()
                        .termId(term.getId())
                        .title(term.getTitle())
                        .content(term.getContent())
                        // 동의 정보가 Map에 없으면 (즉, 한 번도 동의/비동의 기록이 없으면) false로 간주
                        .agreed(userAgreements.getOrDefault(term.getId(), false))
                        .build())
                .toList();

        return TermResponseDTO.GetAgreeResult.builder()
                .terms(results)
                .build();
    }

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
