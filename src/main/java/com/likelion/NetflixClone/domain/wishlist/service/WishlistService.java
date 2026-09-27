package com.likelion.NetflixClone.domain.wishlist.service;

import com.likelion.NetflixClone.domain.content.dto.ContentPageResponseDto;
import com.likelion.NetflixClone.domain.content.dto.ContentResponseDto;
import com.likelion.NetflixClone.domain.content.entity.Content;
import com.likelion.NetflixClone.domain.content.repository.ContentRepository;
import com.likelion.NetflixClone.domain.user.entity.User;
import com.likelion.NetflixClone.domain.user.repository.UserRepository;
import com.likelion.NetflixClone.domain.wishlist.entity.Wishlist;
import com.likelion.NetflixClone.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    // 찜하기 토글 (이미 찜했으면 해제, 안 했으면 추가)
    @Transactional
    public boolean toggleWishlist(String userEmail, Long contentId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. id=" + contentId));

        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserAndContent(user, content);

        if (wishlistOpt.isPresent()) {
            wishlistRepository.delete(wishlistOpt.get());
            return false; // 찜 취소됨
        } else {
            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .content(content)
                    .build();
            wishlistRepository.save(wishlist);
            return true; // 찜 추가됨
        }
    }

    // 내 찜 목록 조회
    public ContentPageResponseDto getMyWishlist(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<Content> contentPage = wishlistRepository.findContentsByUser(user, pageable);
        return ContentPageResponseDto.from(contentPage.map(ContentResponseDto::from));
    }
}