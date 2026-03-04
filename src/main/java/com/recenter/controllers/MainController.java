package com.recenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(
                "newsList",
                jdbcTemplate.query(
                        "SELECT * FROM news ORDER BY publication_date DESC",
                        new BeanPropertyRowMapper<>(com.recenter.entity.News.class)
                )
        );
        model.addAttribute(
                "popServices",
                jdbcTemplate.query(
                        "SELECT * FROM services",
                        new BeanPropertyRowMapper<>(com.recenter.entity.Service.class)
                )
        );
        return "index";
    }

    @GetMapping("/contacts")
    public String contacts() { return "contacts"; }
}