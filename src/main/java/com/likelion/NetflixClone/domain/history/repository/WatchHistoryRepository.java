package com.likelion.NetflixClone.domain.history.repository;

import com.likelion.NetflixClone.domain.content.entity.Content;
import com.likelion.NetflixClone.domain.history.entity.WatchHistory;
import com.likelion.NetflixClone.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    Optional<WatchHistory> findByUserAndContent(User user, Content content);

    Page<WatchHistory> findByUserOrderByLastWatchedAtDesc(User user, Pageable pageable);

    Page<WatchHistory> findByUserAndIsFinishedFalseOrderByLastWatchedAtDesc(User user, Pageable pageable);
}