package com.k_passs.backend.domain.user.controller;

import com.k_passs.backend.domain.user.dto.UserRequestDTO;
import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.service.UserService;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // 현재 로그인된 유저 정보 조회
    @GetMapping
    @Operation(summary = "현재 로그인된 유저 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "USER_200", description = "현재 로그인된 유저를 성공적으로 조회했습니다.")
    })
    public BaseResponse<UserResponseDTO.GetUserInfo> getUserInfo(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        UserResponseDTO.GetUserInfo result= userService.getUserInfo(user);
        return BaseResponse.onSuccess(SuccessStatus.USER_GET_SUCCESS,result);
    }

    // 회원 이름(닉네임) 수정
    @PatchMapping("/profile")
    @Operation(summary = "회원 닉네임을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "USER_201", description = "회원 닉네임을 성공적으로 수정했습니다.")
    })
    public BaseResponse<UserResponseDTO.UpdateNicknameResult> updateNickname(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestBody UserRequestDTO.UpdateNickname request
    ) {
        UserResponseDTO.UpdateNicknameResult result = userService.updateNickname(user, request);
        return BaseResponse.onSuccess(SuccessStatus.USER_UPDATE_NAME, result);
    }

    @GetMapping("/bookmarks")
    @Operation(summary = "내가 찜한 꿀팁 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "USER_202", description = "내가 찜한 꿀팁 목록을 성공적으로 조회했습니다.")
    })
    public BaseResponse<List<UserResponseDTO.GetMyBookmarkTipInfo>> getMyBookmarks(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        List<UserResponseDTO.GetMyBookmarkTipInfo> result = userService.getUserBookmarks(user);
        return BaseResponse.onSuccess(SuccessStatus.USER_GET_TIPS, result);
    }

    @GetMapping("/challenges")
    @Operation(summary = "내가 완료한 챌린지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "USER_203", description = "완료한 챌린지 목록을 성공적으로 조회했습니다.")
    })
    public BaseResponse<List<UserResponseDTO.GetMyCompletedChallengeInfo>> getMyCompletedChallenges(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        List<UserResponseDTO.GetMyCompletedChallengeInfo> result = userService.getMyCompletedChallenges(user);
        return BaseResponse.onSuccess(SuccessStatus.USER_GET_CHALLENGE, result);
    }
}
