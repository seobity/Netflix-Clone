package com.likelion.NetflixClone.domain.history.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "시청 위치 저장/업데이트 요청 DTO")
public class WatchHistoryRequestDto {

    @NotNull(message = "시청 위치(초)는 필수 입력값입니다.")
    @Schema(description = "마지막 시청 위치 (초 단위)", example = "120")
    private Long lastWatchedPosition;

    @Schema(description = "시청 완료 여부", example = "false")
    private boolean isFinished;
}