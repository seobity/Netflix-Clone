package com.likelion.NetflixClone.domain.wishlist.repository;

import com.likelion.NetflixClone.domain.content.entity.Content;
import com.likelion.NetflixClone.domain.user.entity.User;
import com.likelion.NetflixClone.domain.wishlist.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserAndContent(User user, Content content);

    boolean existsByUserAndContent(User user, Content content);

    // 내가 찜한 콘텐츠 목록 조회 (Content 정보 페치 조인)
    @Query("SELECT w.content FROM Wishlist w WHERE w.user = :user")
    Page<Content> findContentsByUser(@Param("user") User user, Pageable pageable);
}