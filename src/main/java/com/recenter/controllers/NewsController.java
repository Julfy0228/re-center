package com.recenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.recenter.repository.NewsRepository;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("newsList", newsRepository.findAll());
        return "news/list";
    }
}
