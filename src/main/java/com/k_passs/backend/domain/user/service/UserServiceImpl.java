package com.k_passs.backend.domain.user.service;

import com.k_passs.backend.domain.bookmark.entity.Bookmark;
import com.k_passs.backend.domain.challenge.entity.Challenge;
import com.k_passs.backend.domain.user.converter.UserConverter;
import com.k_passs.backend.domain.user.dto.UserRequestDTO;
import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO.GetUserInfo getUserInfo(User user) {
        return UserConverter.toGetUserInfo(user);
    }

    @Override
    @Transactional
    public UserResponseDTO.UpdateNicknameResult updateNickname(User user, UserRequestDTO.UpdateNickname request) {
        user.setNickname(request.getNickname());
        User updated = userRepository.save(user);
        return UserConverter.toUpdateNicknameResult(updated);
    }

    @Override
    public List<UserResponseDTO.GetMyBookmarkTipInfo> getUserBookmarks(User user) {
        List<Bookmark> bookmarks = userRepository.findBookmarksByUserId(user.getId());

        return bookmarks.stream()
                .map(bookmark -> UserConverter.toBookmarkTipInfo(bookmark.getTip()))
                .toList();
    }

    @Override
    public List<UserResponseDTO.GetMyCompletedChallengeInfo> getMyCompletedChallenges(User user) {
        List<Challenge> challenges = userRepository.findCompletedChallengesByUserId(user.getId());

        return challenges.stream()
                .map(UserConverter::toCompletedChallengeInfo)
                .toList();
    }
}
