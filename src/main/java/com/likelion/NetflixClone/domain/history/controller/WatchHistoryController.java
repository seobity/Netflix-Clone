package com.likelion.NetflixClone.domain.history.controller;

import com.likelion.NetflixClone.domain.history.dto.WatchHistoryRequestDto;
import com.likelion.NetflixClone.domain.history.dto.WatchHistoryResponseDto;
import com.likelion.NetflixClone.domain.history.service.WatchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Watch History", description = "시청 기록 및 이어보기 API")
@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @Operation(summary = "시청 위치 저장/업데이트 (이어보기 기능)")
    @PostMapping("/contents/{contentId}")
    public ResponseEntity<WatchHistoryResponseDto> saveProgress(
            @PathVariable Long contentId,
            @Valid @RequestBody WatchHistoryRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        WatchHistoryResponseDto response = watchHistoryService.saveOrUpdateProgress(
                userDetails.getUsername(), contentId, requestDto
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 콘텐츠 시청 위치 조회")
    @GetMapping("/contents/{contentId}")
    public ResponseEntity<WatchHistoryResponseDto> getProgress(
            @PathVariable Long contentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        WatchHistoryResponseDto response = watchHistoryService.getProgress(userDetails.getUsername(), contentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이어보기 목록 조회 (시청 중인 콘텐츠)")
    @GetMapping("/continue")
    public ResponseEntity<Page<WatchHistoryResponseDto>> getContinueWatchingList(
            @AuthenticationPrincipal UserDetails userDetails,
            @ParameterObject
            @PageableDefault(size = 10, sort = "lastWatchedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<WatchHistoryResponseDto> response = watchHistoryService.getContinueWatchingList(
                userDetails.getUsername(), pageable
        );
        return ResponseEntity.ok(response);
    }
}