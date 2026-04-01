package com.recenter.repository;

import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Override
    @EntityGraph(attributePaths = {"author"})
    List<News> findAll();

    @Override
    @EntityGraph(attributePaths = {"author"})
    Optional<News> findById(Long id);

    @EntityGraph(attributePaths = {"author"})
    List<News> findByStatus(NewsStatus status);

    @EntityGraph(attributePaths = {"author"})
    List<News> findByAuthor(User author);

    @EntityGraph(attributePaths = {"author"})
    List<News> findByStatusOrderByPublishedAtDesc(NewsStatus status);
}
