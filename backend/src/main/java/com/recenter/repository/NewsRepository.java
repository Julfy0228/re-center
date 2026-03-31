package com.recenter.repository;

import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByStatus(NewsStatus status);
    List<News> findByAuthor(User author);
    List<News> findByStatusOrderByPublishedAtDesc(NewsStatus status);
}
