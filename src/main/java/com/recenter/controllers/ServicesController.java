package com.recenter.controllers;

import com.recenter.dto.BookingRequestDto;
import com.recenter.entity.Service;
import com.recenter.repository.ServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/services")
public class ServicesController {

    @Autowired
    private ServiceJpaRepository serviceJpaRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", serviceJpaRepository.findAll());
        return "services/list";
    }

    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public String detail(@PathVariable("id") Long id, Model model) {
        Optional<Service> serviceOpt = serviceJpaRepository.findById(id);

        if (!serviceOpt.isPresent()) {
            return "errors/404";
        }

        Service service = serviceOpt.get();
        model.addAttribute("service", service);
        model.addAttribute("bookingRequest", new BookingRequestDto());
        return "services/detail";
    }

    @PostMapping("/{id}/book")
    public String book(@PathVariable("id") Long id,
                       @ModelAttribute BookingRequestDto bookingRequest) {
        System.out.println("Пользователь хочет забронировать услугу ID: " + id);

        return "redirect:/cabinet";
    }
}