package com.likelion.NetflixClone.domain.history.dto;

import com.likelion.NetflixClone.domain.content.dto.ContentResponseDto;
import com.likelion.NetflixClone.domain.history.entity.WatchHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "시청 기록 및 이어보기 응답 DTO")
public class WatchHistoryResponseDto {

    @Schema(description = "시청 기록 ID", example = "1")
    private Long historyId;

    @Schema(description = "콘텐츠 정보")
    private ContentResponseDto content;

    @Schema(description = "마지막 시청 위치 (초 단위)", example = "120")
    private Long lastWatchedPosition;

    @Schema(description = "시청 완료 여부", example = "false")
    private boolean isFinished;

    @Schema(description = "마지막 시청 일시")
    private LocalDateTime lastWatchedAt;

    public static WatchHistoryResponseDto from(WatchHistory history) {
        return WatchHistoryResponseDto.builder()
                .historyId(history.getId())
                .content(ContentResponseDto.from(history.getContent()))
                .lastWatchedPosition(history.getLastWatchedPosition())
                .isFinished(history.isFinished())
                .lastWatchedAt(history.getLastWatchedAt())
                .build();
    }
}