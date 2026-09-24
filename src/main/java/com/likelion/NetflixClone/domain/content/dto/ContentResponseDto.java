package com.likelion.NetflixClone.domain.content.dto;

import com.likelion.NetflixClone.domain.content.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponseDto {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long duration;
    private String genreName;

    public static ContentResponseDto from(Content content) {
        return ContentResponseDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .thumbnailUrl(content.getThumbnailUrl())
                .duration(content.getDuration())
                .genreName(content.getGenre() != null ? content.getGenre().getName() : null)
                .build();
    }
}