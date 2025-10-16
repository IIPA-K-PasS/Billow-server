package com.k_passs.backend.domain.term.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TermRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAgreeRequest {
        @Valid
        @NotEmpty(message = "약관 목록을 포함해야 합니다.")
        private List<TermAgreement> terms;
    }

    /**
     * 개별 약관 동의 정보
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAgreement {
        @NotNull(message = "termId는 필수입니다.")
        private Integer termId;

        @NotNull(message = "agreed 여부는 필수입니다.")
        private Boolean agreed;
    }
}
