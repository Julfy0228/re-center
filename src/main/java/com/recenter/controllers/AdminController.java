package com.recenter.controllers;

import com.recenter.dto.*;
import com.recenter.repository.NewsRepository;
import com.recenter.repository.ServiceRepository;
import jakarta.servlet.http.HttpSession;
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

    private boolean checkAdmin(HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!checkAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("newBookingsCount", 5);
        model.addAttribute("revenue", 120000);
        model.addAttribute("freeRooms", 8);
        return "admin/dashboard";
    }

    @GetMapping("/services/add")
    public String addServicePage(HttpSession session) {
        return checkAdmin(session) ? "admin/add-service" : "redirect:/auth/login";
    }

    @PostMapping("/services/add")
    public String processAddService(@ModelAttribute ServiceDetailDto service, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/auth/login";
        serviceRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/news/add")
    public String addNewsPage(HttpSession session) {
        return checkAdmin(session) ? "admin/add-news" : "redirect:/auth/login";
    }

    @PostMapping("/news/add")
    public String processAddNews(@ModelAttribute NewsDto news, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/auth/login";
        news.setPublicationDate(LocalDateTime.now());
        newsRepository.save(news);
        return "redirect:/news";
    }
}