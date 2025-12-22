package com.recenter.controllers;

import com.recenter.repository.NewsRepository;
import com.recenter.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired private NewsRepository newsRepository;
    @Autowired private ServiceRepository serviceRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newsList", newsRepository.findAll());
        model.addAttribute("popServices", serviceRepository.findAll());
        return "index";
    }

    @GetMapping("/contacts")
    public String contacts() { return "contacts"; }
}