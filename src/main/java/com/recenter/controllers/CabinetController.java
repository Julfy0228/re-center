package com.recenter.controllers;

import com.recenter.dto.BookingViewDto;
import com.recenter.dto.UserResponseDto;
import com.recenter.entity.Booking;
import com.recenter.entity.User;
import com.recenter.repository.BookingRepository;
import com.recenter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.recenter.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public String index(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Integer userId = userOpt.get().getId();
            List<Booking> bookings = bookingRepository.findByUserId(userId);
            List<BookingViewDto> bookingDtos = bookings.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            model.addAttribute("bookings", bookingDtos);
        }

        return "user/cabinet";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("phone") String phone,
                                Authentication authentication) {
        String email = authentication.getName();
        UserResponseDto user = userService.findByEmail(email);
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            userService.updateUser(user);
        }
        return "redirect:/cabinet/profile?success";
    }

    @GetMapping("/history")
    public String history(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Integer userId = userOpt.get().getId();
            List<Booking> bookings = bookingRepository.findByUserId(userId);
            List<BookingViewDto> bookingDtos = bookings.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            model.addAttribute("bookings", bookingDtos);
        }
        return "user/history";
    }

    private BookingViewDto convertToDto(Booking booking) {
        BookingViewDto dto = new BookingViewDto();
        dto.setId(booking.getId() != null ? booking.getId().longValue() : null);
        dto.setServiceId(booking.getService() != null ? booking.getService().getId().longValue() : null);
        dto.setServiceTitle(booking.getService() != null ? booking.getService().getTitle() : "");
        dto.setStartDate(booking.getStartDate() != null ? booking.getStartDate().toString() : "");
        dto.setEndDate(booking.getEndDate() != null ? booking.getEndDate().toString() : "");
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}