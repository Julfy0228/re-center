package com.recenter.repository;

import com.recenter.dto.NewsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class NewsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<NewsDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM news ORDER BY id DESC", new BeanPropertyRowMapper<>(NewsDto.class));
    }

    public void save(NewsDto news) {
        String sql = "INSERT INTO news (title, content, publication_date) VALUES (?, ?, ?)";
        String formattedDate = news.getPublicationDate().format(formatter);

        jdbcTemplate.update(sql, news.getTitle(), news.getContent(), formattedDate);
    }
}