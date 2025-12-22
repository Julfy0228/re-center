package com.recenter.controllers;

import com.recenter.dto.UserResponseDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.recenter.service.UserService;

import java.util.ArrayList;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        model.addAttribute("bookings", new ArrayList<>());
        return "user/cabinet";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("phone") String phone,
                                HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            userService.updateUser(user);
        }
        return "redirect:/cabinet/profile?success";
    }

    @GetMapping("/history")
    public String history(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/auth/login";
        return "user/history";
    }
}