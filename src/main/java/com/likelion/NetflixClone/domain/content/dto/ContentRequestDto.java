package com.likelion.NetflixClone.domain.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequestDto {

    @NotBlank(message = "콘텐츠 제목은 필수 입력값입니다.")
    private String title;

    private String description;

    private String thumbnailUrl;

    @NotNull(message = "영상 길이는 필수 입력값입니다.")
    private Long duration; // 초 단위
    private String genreName;
}