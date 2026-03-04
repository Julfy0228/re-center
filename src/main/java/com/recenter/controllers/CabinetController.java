package com.recenter.controllers;

import com.recenter.dto.BookingViewDto;
import com.recenter.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.recenter.service.UserService;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String index(Authentication authentication, Model model) {
        String email = authentication.getName();
        Long userId = jdbcTemplate.queryForObject("SELECT id FROM users WHERE email = ?", Long.class, email);

        if (userId != null) {
            model.addAttribute(
                    "bookings",
                    jdbcTemplate.query(
                            "SELECT b.id AS id, b.service_id AS serviceId, s.title AS serviceTitle, b.start_date AS startDate, b.end_date AS endDate, b.total_price AS totalPrice, b.status AS status " +
                                    "FROM bookings b " +
                                    "LEFT JOIN services s ON s.id = b.service_id " +
                                    "WHERE b.user_id = ? " +
                                    "ORDER BY b.id DESC",
                            new BeanPropertyRowMapper<>(BookingViewDto.class),
                            userId
                    )
            );
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
        Long userId = jdbcTemplate.queryForObject("SELECT id FROM users WHERE email = ?", Long.class, email);
        if (userId != null) {
            model.addAttribute(
                    "bookings",
                    jdbcTemplate.query(
                            "SELECT b.id AS id, b.service_id AS serviceId, s.title AS serviceTitle, b.start_date AS startDate, b.end_date AS endDate, b.total_price AS totalPrice, b.status AS status " +
                                    "FROM bookings b " +
                                    "LEFT JOIN services s ON s.id = b.service_id " +
                                    "WHERE b.user_id = ? " +
                                    "ORDER BY b.id DESC",
                            new BeanPropertyRowMapper<>(BookingViewDto.class),
                            userId
                    )
            );
        }
        return "user/history";
    }
}