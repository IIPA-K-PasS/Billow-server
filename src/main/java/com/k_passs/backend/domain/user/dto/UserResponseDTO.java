package com.k_passs.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {
    // 유저 정보 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserInfo {
        private Long id;
        private String nickname;
        private String email;
        private String profileImageUrl;
        private Long point;
    }

    // 닉네임 수정
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNicknameResult {
        private Long id;
        private String nickname;
    }

    // 내가 찜한 꿀팁 정보
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyBookmarkTipInfo {
        private Long tipId;
        private String title;
        private String content;
    }

    // 완료한 챌린지 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyCompletedChallengeInfo {
        private Long challengeId;
        private String title;
        private String description;
        private int rewardPoints;
        private String imageUrl;
    }
}
