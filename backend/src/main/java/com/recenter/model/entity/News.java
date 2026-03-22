package com.recenter.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.recenter.model.enums.NewsStatus;
import java.time.LocalDateTime;

/**
 * Новость или объявление.
 * <p>
 * Создаётся автором (обычно администратором или менеджером).
 * Имеет статус черновика или опубликована.
 * </p>
 *
 * @see User
 * @see NewsStatus
 */
@Entity
@Table(name = "News")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class News {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", nullable = false)
    private User author;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private NewsStatus status;

    private LocalDateTime publishedAt;
}