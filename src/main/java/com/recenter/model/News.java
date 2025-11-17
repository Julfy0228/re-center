package com.recenter.model;

import java.time.LocalDateTime;
import com.recenter.model.enums.NewsStatus;

public class News {
    private int id;
    private String title;
    private String content;
    private int authorId;
    private LocalDateTime date;
    private NewsStatus status;

    public News() {}

    public News(int id, String title, String content, int authorId, LocalDateTime date, NewsStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.date = date;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public NewsStatus getStatus() { return status; }
    public void setStatus(NewsStatus status) { this.status = status; }
}
