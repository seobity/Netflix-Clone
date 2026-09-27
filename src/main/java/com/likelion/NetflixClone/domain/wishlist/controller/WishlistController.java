package com.likelion.NetflixClone.domain.wishlist.controller;

import com.likelion.NetflixClone.domain.content.dto.ContentPageResponseDto;
import com.likelion.NetflixClone.domain.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Wishlist", description = "찜하기 (마이리스트) API")
@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "찜하기 토글 (추가/취소)")
    @PostMapping("/{contentId}")
    public ResponseEntity<Map<String, Object>> toggleWishlist(
            @PathVariable Long contentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean isWished = wishlistService.toggleWishlist(userDetails.getUsername(), contentId);
        return ResponseEntity.ok(Map.of(
                "contentId", contentId,
                "isWished", isWished,
                "message", isWished ? "마이리스트에 추가되었습니다." : "마이리스트에서 삭제되었습니다."
        ));
    }

    @Operation(summary = "내 찜 목록 조회")
    @GetMapping
    public ResponseEntity<ContentPageResponseDto> getMyWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ContentPageResponseDto response = wishlistService.getMyWishlist(userDetails.getUsername(), pageable);
        return ResponseEntity.ok(response);
    }
}