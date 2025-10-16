package com.k_passs.backend.domain.term.controller;

import com.k_passs.backend.domain.term.dto.request.TermRequestDTO;
import com.k_passs.backend.domain.term.dto.response.TermResponseDTO;
import com.k_passs.backend.domain.term.service.TermService;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/term")
@Validated
public class TermRestController {
    private final TermService termService;

    @GetMapping
    @Operation(summary = "전체 약관 목록과 사용자의 동의 여부를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON_200", description = "약관 목록 조회가 성공적으로 완료되었습니다.")
    })
    public BaseResponse<TermResponseDTO.GetAgreeResult> getAllTerms(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        TermResponseDTO.GetAgreeResult result = termService.getAllTerms(user);
        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    // [추가] 약관 동의
    @PostMapping("")
    @Operation(summary = "회원가입 시 약관에 동의합니다.", description = "약관 ID와 동의 여부(true/false)를 배열로 POST합니다. 필수 약관 미동의 및 잘못된 약관 ID는 에러 처리됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "MEMBER_201", description = "약관 동의 처리가 성공적으로 완료되었습니다."),
            @ApiResponse(responseCode = "MEMBER_4001", description = "존재하지 않는 약관 ID입니다."),
            @ApiResponse(responseCode = "MEMBER_4002", description = "필수 약관에 동의하지 않았습니다."),
    })
    public BaseResponse<TermResponseDTO.TermAgreeResult> termAgree(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestBody @Valid TermRequestDTO.TermAgreeRequest request
    ) {
        TermResponseDTO.TermAgreeResult result = termService.termAgree(user, request);
        return BaseResponse.onSuccess(SuccessStatus.USER_AGREE_TERMS, result);
    }
}
