package com.recenter.controllers;

import com.recenter.dto.*;
import com.recenter.entity.News;
import com.recenter.entity.Service;
import com.recenter.repository.NewsJpaRepository;
import com.recenter.repository.ServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private ServiceJpaRepository serviceJpaRepository;
    @Autowired private NewsJpaRepository newsJpaRepository;

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
    public String processAddService(@ModelAttribute ServiceDetailDto serviceDto) {
        Service service = new Service(
            serviceDto.getTitle(),
            serviceDto.getDescription(),
            serviceDto.getBasePrice() != null ? serviceDto.getBasePrice().doubleValue() : 0.0,
            serviceDto.getServiceType(),
            serviceDto.getMinCapacity(),
            serviceDto.getMaxCapacity()
        );
        serviceJpaRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/news/add")
    public String addNewsPage() {
        return "admin/add-news";
    }

    @PostMapping("/news/add")
    public String processAddNews(@ModelAttribute NewsDto newsDto) {
        News news = new News(
            newsDto.getTitle(),
            newsDto.getContent(),
            LocalDateTime.now()
        );
        newsJpaRepository.save(news);
        return "redirect:/news";
    }
}