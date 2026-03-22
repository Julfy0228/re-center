package com.recenter.model.entity;

import java.time.LocalDateTime;
import com.recenter.model.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Activities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Activity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ActivityType type;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}