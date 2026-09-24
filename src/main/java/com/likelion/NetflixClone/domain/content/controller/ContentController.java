package com.likelion.NetflixClone.domain.content.controller;

import com.likelion.NetflixClone.domain.content.dto.ContentPageResponseDto;
import com.likelion.NetflixClone.domain.content.dto.ContentRequestDto;
import com.likelion.NetflixClone.domain.content.dto.ContentResponseDto;
import com.likelion.NetflixClone.domain.content.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Content", description = "콘텐츠 API")
@RestController
@RequestMapping("/api/v1/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 1. [POST] 콘텐츠 등록 (ADMIN 전용)
    @Operation(summary = "콘텐츠 등록 (ADMIN 전용)")
    @PostMapping
    public ResponseEntity<ContentResponseDto> createContent(@Valid @RequestBody ContentRequestDto requestDto) {
        ContentResponseDto response = contentService.createContent(requestDto);
        return ResponseEntity.ok(response);
    }

    // 2. [GET] 콘텐츠 목록 조회 (검색/장르/페이징/정렬)
    @Operation(summary = "콘텐츠 목록 조회 (검색/장르/페이징/정렬)")
    @GetMapping
    public ResponseEntity<ContentPageResponseDto> getContents(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String keyword,
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ContentPageResponseDto response = contentService.getContents(genre, keyword, pageable);
        return ResponseEntity.ok(response);
    }

    // 3. [GET] 콘텐츠 단건 조회
    @Operation(summary = "콘텐츠 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponseDto> getContent(@PathVariable Long id) {
        ContentResponseDto response = contentService.getContent(id);
        return ResponseEntity.ok(response);
    }

    // 4. [PUT] 콘텐츠 수정 (ADMIN 전용)
    @Operation(summary = "콘텐츠 수정 (ADMIN 전용)")
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponseDto> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ContentRequestDto requestDto
    ) {
        ContentResponseDto response = contentService.updateContent(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // 5. [DELETE] 콘텐츠 삭제 (ADMIN 전용)
    @Operation(summary = "콘텐츠 삭제 (ADMIN 전용)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}