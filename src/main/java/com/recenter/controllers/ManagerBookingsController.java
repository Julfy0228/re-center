package com.recenter.controllers;

import com.recenter.entity.Booking;
import com.recenter.repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager/bookings")
public class ManagerBookingsController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public String list(@RequestParam(value = "status", required = false) String status,
                       Model model) {

        List<Booking> bookings;

        if (status != null && !status.isBlank()) {
            String normalizedStatus = status.trim().toUpperCase();
            bookings = bookingRepository.findAll().stream()
                    .filter(b -> normalizedStatus.equals(b.getStatus()))
                    .collect(Collectors.toList());
        } else {
            bookings = bookingRepository.findAll();
        }

        model.addAttribute("status", status);
        model.addAttribute("bookings", bookings);
        return "manager/bookings";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status) {
        if (status == null) {
            return "redirect:/manager/bookings";
        }

        String normalized = status.trim().toUpperCase();
        if (!normalized.equals("CREATED") && !normalized.equals("CONFIRMED") && !normalized.equals("PAID") && !normalized.equals("CANCELED")) {
            return "redirect:/manager/bookings";
        }

        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(normalized);
            bookingRepository.save(booking);
        }
        return "redirect:/manager/bookings";
    }
}
