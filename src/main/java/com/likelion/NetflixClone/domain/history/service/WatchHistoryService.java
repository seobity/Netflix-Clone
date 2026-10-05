package com.likelion.NetflixClone.domain.history.service;

import com.likelion.NetflixClone.domain.content.entity.Content;
import com.likelion.NetflixClone.domain.content.repository.ContentRepository;
import com.likelion.NetflixClone.domain.history.dto.WatchHistoryRequestDto;
import com.likelion.NetflixClone.domain.history.dto.WatchHistoryResponseDto;
import com.likelion.NetflixClone.domain.history.entity.WatchHistory;
import com.likelion.NetflixClone.domain.history.repository.WatchHistoryRepository;
import com.likelion.NetflixClone.domain.user.entity.User;
import com.likelion.NetflixClone.domain.user.repository.UserRepository;
import com.likelion.NetflixClone.global.exception.CustomException;
import com.likelion.NetflixClone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    // 시청 위치 기록 및 업데이트 (Upsert)
    @Transactional
    public WatchHistoryResponseDto saveOrUpdateProgress(String userEmail, Long contentId, WatchHistoryRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        WatchHistory history = watchHistoryRepository.findByUserAndContent(user, content)
                .map(existingHistory -> {
                    existingHistory.updateProgress(requestDto.getLastWatchedPosition(), requestDto.isFinished());
                    return existingHistory;
                })
                .orElseGet(() -> watchHistoryRepository.save(
                        WatchHistory.builder()
                                .user(user)
                                .content(content)
                                .lastWatchedPosition(requestDto.getLastWatchedPosition())
                                .isFinished(requestDto.isFinished())
                                .build()
                ));

        return WatchHistoryResponseDto.from(history);
    }

    // 단건 콘텐츠 이어보기 위치 조회
    public WatchHistoryResponseDto getProgress(String userEmail, Long contentId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        WatchHistory history = watchHistoryRepository.findByUserAndContent(user, content)
                .orElseThrow(() -> new CustomException(ErrorCode.HISTORY_NOT_FOUND));

        return WatchHistoryResponseDto.from(history);
    }

    // 이어보기 목록 조회 (미완료 시청 콘텐츠)
    public Page<WatchHistoryResponseDto> getContinueWatchingList(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return watchHistoryRepository.findByUserAndIsFinishedFalseOrderByLastWatchedAtDesc(user, pageable)
                .map(WatchHistoryResponseDto::from);
    }
}