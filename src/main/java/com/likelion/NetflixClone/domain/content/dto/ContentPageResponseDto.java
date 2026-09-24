package com.likelion.NetflixClone.domain.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentPageResponseDto {
    private List<ContentResponseDto> contents;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private boolean isLast;

    public static ContentPageResponseDto from(Page<ContentResponseDto> page) {
        return ContentPageResponseDto.builder()
                .contents(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .isLast(page.isLast())
                .build();
    }
}