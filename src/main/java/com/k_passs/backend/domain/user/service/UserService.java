package com.k_passs.backend.domain.user.service;

import com.k_passs.backend.domain.user.dto.UserRequestDTO;
import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;

import java.util.List;

public interface UserService {
    // 현재 로그인된 유저 정보 조회
    UserResponseDTO.GetUserInfo getUserInfo(User user);

    UserResponseDTO.UpdateNicknameResult updateNickname(User user, UserRequestDTO.UpdateNickname request);

    List<UserResponseDTO.GetMyBookmarkTipInfo> getUserBookmarks(User user);

    List<UserResponseDTO.GetMyCompletedChallengeInfo> getMyCompletedChallenges(User user);
}
