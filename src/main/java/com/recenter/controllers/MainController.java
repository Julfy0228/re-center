package com.recenter.controllers;

import com.recenter.repository.NewsJpaRepository;
import com.recenter.repository.ServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired private NewsJpaRepository newsJpaRepository;
    @Autowired private ServiceJpaRepository serviceJpaRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newsList", newsJpaRepository.findAllByOrderByPublicationDateDesc());
        model.addAttribute("popServices", serviceJpaRepository.findAll());
        return "index";
    }

    @GetMapping("/contacts")
    public String contacts() { return "contacts"; }
}