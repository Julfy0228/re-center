package com.recenter.controllers;

import com.recenter.dto.BookingRequestDto;
import com.recenter.dto.ServiceDetailDto;
import com.recenter.repository.ServiceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/services")
public class ServicesController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "services/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        ServiceDetailDto service = serviceRepository.findById(id);

        if (service == null) {
            return "errors/404";
        }

        model.addAttribute("service", service);
        model.addAttribute("bookingRequest", new BookingRequestDto());
        return "services/detail";
    }

    @PostMapping("/{id}/book")
    public String book(@PathVariable("id") Long id,
                       @ModelAttribute BookingRequestDto bookingRequest,
                       HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        System.out.println("Пользователь хочет забронировать услугу ID: " + id);

        return "redirect:/cabinet";
    }
}