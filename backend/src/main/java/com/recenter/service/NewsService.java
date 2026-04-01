package com.recenter.service;

import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import com.recenter.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public News create(News news) {
        return newsRepository.save(news);
    }

    public Optional<News> getById(Long id) {
        return newsRepository.findById(id);
    }

    public List<News> getAll() {
        return newsRepository.findAll();
    }

    public List<News> getByStatus(NewsStatus status) {
        return newsRepository.findByStatusOrderByPublishedAtDesc(status);
    }

    public List<News> getPublished() {
        return getByStatus(NewsStatus.PUBLISHED);
    }

    public List<News> getByAuthor(User author) {
        return newsRepository.findByAuthor(author);
    }

    public News update(Long id, News newsDetails) {
        return newsRepository.findById(id).map(news -> {
            if (newsDetails.getTitle() != null) {
                news.setTitle(newsDetails.getTitle());
            }
            if (newsDetails.getContent() != null) {
                news.setContent(newsDetails.getContent());
            }
            if (newsDetails.getImageUrl() != null) {
                news.setImageUrl(newsDetails.getImageUrl());
            }
            if (newsDetails.getStatus() != null) {
                news.setStatus(newsDetails.getStatus());
                if (newsDetails.getStatus() == NewsStatus.PUBLISHED) {
                    news.setPublishedAt(LocalDateTime.now());
                }
            }
            return newsRepository.save(news);
        }).orElse(null);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }

    public long count() {
        return newsRepository.count();
    }
}
