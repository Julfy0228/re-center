package com.recenter.repository;

import com.recenter.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByAuthorId(Integer authorId);
    List<News> findByStatus(String status);
}
