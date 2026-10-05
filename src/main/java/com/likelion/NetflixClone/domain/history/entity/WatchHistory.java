package com.likelion.NetflixClone.domain.history.entity;

import com.likelion.NetflixClone.domain.content.entity.Content;
import com.likelion.NetflixClone.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "watch_histories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "content_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Column(nullable = false)
    private Long lastWatchedPosition;

    @Column(nullable = false)
    private boolean isFinished;

    @LastModifiedDate
    private LocalDateTime lastWatchedAt;

    public void updateProgress(Long position, boolean isFinished) {
        this.lastWatchedPosition = position;
        this.isFinished = isFinished;
    }
}