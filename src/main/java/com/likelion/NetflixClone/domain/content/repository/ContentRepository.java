package com.likelion.NetflixClone.domain.content.repository;

import com.likelion.NetflixClone.domain.content.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

    // 1. 키워드 검색 + 페이징
    Page<Content> findByTitleContaining(String keyword, Pageable pageable);

    // 2. 장르 검색 + 페이징
    Page<Content> findByGenreName(String genreName, Pageable pageable);

    // 3. 장르 + 키워드 동시 검색 + 페이징
    Page<Content> findByGenreNameAndTitleContaining(String genreName, String keyword, Pageable pageable);
}