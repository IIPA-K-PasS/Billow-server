package com.k_passs.backend.domain.user.converter;

import com.k_passs.backend.domain.challenge.entity.Challenge;
import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;

public class UserConverter {

    public static UserResponseDTO.GetUserInfo toGetUserInfo(User user) {
        return UserResponseDTO.GetUserInfo.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .point(user.getPoint())
                .build();
    }

    public static UserResponseDTO.UpdateNicknameResult toUpdateNicknameResult(User user) {
        return UserResponseDTO.UpdateNicknameResult.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
    }

    public static UserResponseDTO.GetMyBookmarkTipInfo toBookmarkTipInfo(Tip tip) {
        return UserResponseDTO.GetMyBookmarkTipInfo.builder()
                .tipId(tip.getId())
                .title(tip.getTitle())
                .content(tip.getContent())
                .build();
    }

    public static UserResponseDTO.GetMyCompletedChallengeInfo toCompletedChallengeInfo(Challenge challenge) {
        return UserResponseDTO.GetMyCompletedChallengeInfo.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .rewardPoints(challenge.getRewardPoints())
                .imageUrl(challenge.getImageUrl())
                .build();
    }
}
