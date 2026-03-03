package com.recenter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity for News
 * Maps to 'news' table in database
 */
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 4000)
    private String content;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    // Constructors
    public News() {
    }

    public News(String title, String content, LocalDateTime publicationDate) {
        this.title = title;
        this.content = content;
        this.publicationDate = publicationDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publicationDate=" + publicationDate +
                '}';
    }
}
