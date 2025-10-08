package com.k_passs.backend.domain.bookmark.api;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.k_passs.backend.domain.bookmark.dto.BookmarkRequestDTO;
import com.k_passs.backend.domain.bookmark.service.BookmarkService;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.service.UserService;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.ErrorStatus;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
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
@RequestMapping("/bookmark")
@Validated
public class BookmarkRestController {

    private final BookmarkService bookMarkService;
    private final UserService userService;

    @PostMapping("")
    @Operation(summary = "특정 꿀팁에 북마크 요청 또는 취소하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "BOOKMARK_200", description = "북마크 상태가 성공적으로 변경되었습니다.")
    })
    public BaseResponse<String> bookmark(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody @Valid BookmarkRequestDTO.Bookmark request
    ) {
        if (user == null || user.getUser() == null) {
            return BaseResponse.onFailure(ErrorStatus._USER_UNAUTHORIZED, "유저 인증에 실패했습니다.");
        }

        bookMarkService.updateBookmarkStatus(user.getUser(), request);
        return BaseResponse.onSuccess(SuccessStatus.BOOKMARK_STATUS_CHANGED, "북마크 상태가 변경되었습니다.");
    }
}
