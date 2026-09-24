package com.likelion.NetflixClone.domain.content.service;

import com.likelion.NetflixClone.domain.content.dto.ContentPageResponseDto;
import com.likelion.NetflixClone.domain.content.dto.ContentRequestDto;
import com.likelion.NetflixClone.domain.content.dto.ContentResponseDto;
import com.likelion.NetflixClone.domain.content.entity.Content;
import com.likelion.NetflixClone.domain.content.entity.Genre;
import com.likelion.NetflixClone.domain.content.repository.ContentRepository;
import com.likelion.NetflixClone.domain.content.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;
    private final GenreRepository genreRepository;

    // 1. 등록
    @Transactional
    public ContentResponseDto createContent(ContentRequestDto requestDto) {
        Genre genre = null;
        if (StringUtils.hasText(requestDto.getGenreName())) {
            String name = requestDto.getGenreName().toUpperCase(); // 대문자로 통일 (예: "ACTION")

            // genres 테이블에서 이름으로 조회하고, 없으면 새로 저장하여 매핑
            genre = genreRepository.findByName(name)
                    .orElseGet(() -> genreRepository.save(
                            Genre.builder().name(name).build()
                    ));
        }

        Content content = Content.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .duration(requestDto.getDuration())
                .genre(genre)
                .build();

        return ContentResponseDto.from(contentRepository.save(content));
    }

    // 2. 목록 조회 (검색/페이징)
    public ContentPageResponseDto getContents(String genre, String keyword, Pageable pageable) {
        Page<Content> contentPage;
        boolean hasGenre = StringUtils.hasText(genre);
        boolean hasKeyword = StringUtils.hasText(keyword);

        if (hasGenre && hasKeyword) {
            contentPage = contentRepository.findByGenreNameAndTitleContaining(genre, keyword, pageable);
        } else if (hasGenre) {
            contentPage = contentRepository.findByGenreName(genre, pageable);
        } else if (hasKeyword) {
            contentPage = contentRepository.findByTitleContaining(keyword, pageable);
        } else {
            contentPage = contentRepository.findAll(pageable);
        }

        return ContentPageResponseDto.from(contentPage.map(ContentResponseDto::from));
    }

    // 3. 단건 조회
    public ContentResponseDto getContent(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. id=" + id));
        return ContentResponseDto.from(content);
    }

    // 4. 수정
    @Transactional
    public ContentResponseDto updateContent(Long id, ContentRequestDto requestDto) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. id=" + id));

        Genre genre = null;
        if (StringUtils.hasText(requestDto.getGenreName())) {
            String name = requestDto.getGenreName().toUpperCase();

            // 💡 등록 시와 동일하게 이름으로 찾고, 없으면 자동 생성 후 반영
            genre = genreRepository.findByName(name)
                    .orElseGet(() -> genreRepository.save(
                            Genre.builder().name(name).build()
                    ));
        }

        content.update(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getThumbnailUrl(),
                requestDto.getDuration(),
                genre
        );

        return ContentResponseDto.from(content);
    }

    // 5. 삭제
    @Transactional
    public void deleteContent(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. id=" + id));
        contentRepository.delete(content);
    }
}