package com.recenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorEmail;
    private String status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
}
