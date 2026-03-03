package com.recenter.controllers;

import com.recenter.dto.*;
import com.recenter.repository.NewsRepository;
import com.recenter.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private NewsRepository newsRepository;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("newBookingsCount", 5);
        model.addAttribute("revenue", 120000);
        model.addAttribute("freeRooms", 8);
        return "admin/dashboard";
    }

    @GetMapping("/services/add")
    public String addServicePage() {
        return "admin/add-service";
    }

    @PostMapping("/services/add")
    public String processAddService(@ModelAttribute ServiceDetailDto service) {
        serviceRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/news/add")
    public String addNewsPage() {
        return "admin/add-news";
    }

    @PostMapping("/news/add")
    public String processAddNews(@ModelAttribute NewsDto news) {
        news.setPublicationDate(LocalDateTime.now());
        newsRepository.save(news);
        return "redirect:/news";
    }
}