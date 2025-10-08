package com.k_passs.backend.domain.user.repository;

import com.k_passs.backend.domain.bookmark.entity.Bookmark;
import com.k_passs.backend.domain.challenge.entity.Challenge;
import com.k_passs.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // 내가 찜한 꿀팁(Bookmarks) 조회
    @Query("select b from Bookmark b join fetch b.tip where b.user.id = :userId")
    List<Bookmark> findBookmarksByUserId(Long userId);

    // 완료한 챌린지 조회
    @Query("""
    select uc.challenge
    from UserChallenge uc
    join uc.challenge c
    where uc.user.id = :userId
      and uc.status = com.k_passs.backend.domain.model.enums.ChallengeStatus.COMPLETED
    """)
    List<Challenge> findCompletedChallengesByUserId(Long userId);
}
