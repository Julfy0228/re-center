package com.recenter.controllers;

import com.recenter.dto.*;
import com.recenter.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;

    @GetMapping("/login")
    public String loginPage() { return "auth/login"; }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginRequestDto loginDto, HttpSession session, Model model) {
        if (userService.checkPassword(loginDto.getEmail(), loginDto.getPassword())) {
            UserResponseDto user = userService.findByEmail(loginDto.getEmail());
            session.setAttribute("user", user);
            return "ADMIN".equals(user.getRole()) ? "redirect:/admin" : "redirect:/cabinet";
        }
        model.addAttribute("error", "Неверный логин или пароль");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("regDto", new RegistrationRequestDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegistrationRequestDto regDto, Model model) {
        try {
            userService.register(regDto);
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("regDto", regDto);
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}