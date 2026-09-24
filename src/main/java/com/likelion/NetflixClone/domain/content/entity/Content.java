package com.likelion.NetflixClone.domain.content.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String thumbnailUrl;

    private Long duration;

    // Genre 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public void update(String title, String description, String thumbnailUrl, Long duration, Genre genre) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.genre = genre;
    }
}