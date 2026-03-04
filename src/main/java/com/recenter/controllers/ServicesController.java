package com.recenter.controllers;

import com.recenter.dto.BookingRequestDto;
import com.recenter.entity.Booking;
import com.recenter.entity.Service;
import com.recenter.entity.User;
import com.recenter.repository.BookingRepository;
import com.recenter.repository.ServiceRepository;
import com.recenter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/services")
public class ServicesController {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public String list(Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "services/list";
    }

    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public String detail(@PathVariable("id") Long id, Model model) {
        Optional<Service> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isEmpty()) {
            return "errors/404";
        }
        model.addAttribute("service", serviceOpt.get());
        model.addAttribute("bookingRequest", new BookingRequestDto());
        return "services/detail";
    }

    @PostMapping("/{id}/book")
    public String book(@PathVariable("id") Long id,
                       @ModelAttribute BookingRequestDto bookingRequest,
                       Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            return "redirect:/auth/login";
        }

        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        Optional<Service> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isEmpty()) {
            return "redirect:/services";
        }

        Service service = serviceOpt.get();
        LocalDate start = bookingRequest.getStartDate() != null ? bookingRequest.getStartDate() : LocalDate.now();
        LocalDate end = bookingRequest.getEndDate() != null ? bookingRequest.getEndDate() : start;

        Double basePrice = service.getBasePrice();
        double totalPrice;

        Booking booking = new Booking();
        booking.setUser(userOpt.get());
        booking.setService(service);
        booking.setStatus("CREATED");

        if (service.getServiceType() != null && service.getServiceType().trim().equalsIgnoreCase("HOURLY")) {
            LocalTime timeFrom = bookingRequest.getTimeFrom();
            LocalTime timeTo = bookingRequest.getTimeTo();
            if (timeFrom == null || timeTo == null) {
                return "redirect:/services/" + id;
            }

            long hours = ChronoUnit.HOURS.between(timeFrom, timeTo);
            if (hours <= 0) {
                return "redirect:/services/" + id;
            }

            double pricePerHour = basePrice != null ? basePrice : 0.0;
            totalPrice = pricePerHour * hours;

            booking.setStartDate(start + " " + timeFrom);
            booking.setEndDate(start + " " + timeTo);
            booking.setTotalPrice(totalPrice);
            bookingRepository.save(booking);

            return "redirect:/cabinet?bookingSuccess";
        }

        totalPrice = basePrice != null ? basePrice : 0.0;
        booking.setStartDate(start.toString());
        booking.setEndDate(end.toString());
        booking.setTotalPrice(totalPrice);
        bookingRepository.save(booking);

        return "redirect:/cabinet?bookingSuccess";
    }
}