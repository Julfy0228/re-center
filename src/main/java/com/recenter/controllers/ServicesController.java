package com.recenter.controllers;

import com.recenter.dto.BookingRequestDto;
import com.recenter.entity.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/services")
public class ServicesController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TransactionTemplate txTemplate;

    @Autowired
    public ServicesController(PlatformTransactionManager transactionManager) {
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute(
                "services",
                jdbcTemplate.query("SELECT * FROM services", new BeanPropertyRowMapper<>(Service.class))
        );
        return "services/list";
    }

    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public String detail(@PathVariable("id") Long id, Model model) {
        Service service;
        try {
            service = jdbcTemplate.queryForObject(
                    "SELECT * FROM services WHERE id = ?",
                    new BeanPropertyRowMapper<>(Service.class),
                    id
            );
        } catch (Exception e) {
            return "errors/404";
        }
        model.addAttribute("service", service);
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
        Long userId = jdbcTemplate.queryForObject("SELECT id FROM users WHERE email = ?", Long.class, email);

        if (userId == null) {
            return "redirect:/auth/login";
        }

        String serviceType = jdbcTemplate.queryForObject(
                "SELECT service_type FROM services WHERE id = ?",
                String.class,
                id
        );

        LocalDate start = bookingRequest.getStartDate() != null ? bookingRequest.getStartDate() : LocalDate.now();
        LocalDate end = bookingRequest.getEndDate() != null ? bookingRequest.getEndDate() : start;

        Double basePrice = jdbcTemplate.queryForObject("SELECT base_price FROM services WHERE id = ?", Double.class, id);
        double totalPrice;

        if (serviceType != null && serviceType.trim().equalsIgnoreCase("HOURLY")) {
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

            String startValue = start + " " + timeFrom;
            String endValue = start + " " + timeTo;

            txTemplate.executeWithoutResult(status -> jdbcTemplate.update(
                    "INSERT INTO bookings (user_id, service_id, start_date, end_date, total_price, status) VALUES (?, ?, ?, ?, ?, ?)",
                    userId,
                    id,
                    startValue,
                    endValue,
                    totalPrice,
                    "CREATED"
            ));

            return "redirect:/cabinet?bookingSuccess";
        }

        totalPrice = basePrice != null ? basePrice : 0.0;

        txTemplate.executeWithoutResult(status -> jdbcTemplate.update(
                "INSERT INTO bookings (user_id, service_id, start_date, end_date, total_price, status) VALUES (?, ?, ?, ?, ?, ?)",
                userId,
                id,
                start.toString(),
                end.toString(),
                totalPrice,
                "CREATED"
        ));

        return "redirect:/cabinet?bookingSuccess";
    }
}