package com.k_passs.backend.domain.term.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TermResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAgreeResult {
        private Long userId;
        private List<TermAgreementResult> terms;
    }

    /**
     * 개별 약관 동의 결과
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAgreementResult {
        private Integer termId;
        private Boolean agreed;
    }
}
