package com.recenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String list(Model model) {
        model.addAttribute(
                "newsList",
                jdbcTemplate.query(
                        "SELECT * FROM news ORDER BY publication_date DESC",
                        new BeanPropertyRowMapper<>(com.recenter.entity.News.class)
                )
        );
        return "news/list";
    }
}