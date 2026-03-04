package com.recenter.controllers;

import com.recenter.dto.*;
import com.recenter.entity.News;
import com.recenter.entity.Service;
import com.recenter.entity.User;
import com.recenter.repository.BookingRepository;
import com.recenter.repository.NewsRepository;
import com.recenter.repository.ServiceRepository;
import com.recenter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String dashboard(Model model) {
        long newBookingsCount = bookingRepository.countPendingBookings();
        Double revenue = bookingRepository.sumConfirmedBookingsRevenue();

        model.addAttribute("newBookingsCount", newBookingsCount);
        model.addAttribute("revenue", revenue != null ? revenue : 0);
        model.addAttribute("freeRooms", 0);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model,
                        @RequestParam(value = "created", required = false) String created,
                        @RequestParam(value = "error", required = false) String error) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        model.addAttribute("created", created);
        model.addAttribute("error", error);
        return "admin/users";
    }

    @GetMapping("/users/create")
    public String createUserPage() {
        return "admin/create-user";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam(value = "firstName", required = false) String firstName,
                             @RequestParam(value = "lastName", required = false) String lastName,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam("role") String role) {

        if (email == null || email.isBlank() || password == null || password.isBlank() || role == null) {
            return "redirect:/admin/users?error=validation";
        }

        String normalizedRole = role.trim().toUpperCase();
        if (!normalizedRole.equals("CLIENT") && !normalizedRole.equals("MANAGER") && !normalizedRole.equals("ADMIN")) {
            return "redirect:/admin/users?error=validation";
        }

        try {
            User user = new User(email.trim(), passwordEncoder.encode(password), firstName, lastName, normalizedRole);
            user.setPhone(phone);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            return "redirect:/admin/users?error=duplicate";
        }

        return "redirect:/admin/users?created=1";
    }

    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable("id") Long id, @RequestParam("role") String role) {
        if (role == null) {
            return "redirect:/admin/users";
        }

        String normalized = role.trim().toUpperCase();
        if (!normalized.equals("CLIENT") && !normalized.equals("MANAGER") && !normalized.equals("ADMIN")) {
            return "redirect:/admin/users";
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(normalized);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id, Authentication authentication) {
        if (id == null) {
            return "redirect:/admin/users";
        }

        Long currentUserId = null;
        if (authentication != null && authentication.getName() != null) {
            Optional<User> currentUser = userRepository.findByEmail(authentication.getName());
            if (currentUser.isPresent()) {
                currentUserId = currentUser.get().getId();
            }
        }

        if (currentUserId != null && currentUserId.equals(id)) {
            return "redirect:/admin/users";
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && "ADMIN".equalsIgnoreCase(userOpt.get().getRole())) {
            return "redirect:/admin/users";
        }

        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/services/add")
    public String addServicePage() {
        return "admin/add-service";
    }

    @PostMapping("/services/add")
    public String processAddService(@ModelAttribute ServiceDetailDto serviceDto) {
        Service service = new Service(
                serviceDto.getTitle(),
                serviceDto.getDescription(),
                serviceDto.getBasePrice() != null ? serviceDto.getBasePrice().doubleValue() : null,
                serviceDto.getServiceType(),
                serviceDto.getMinCapacity(),
                serviceDto.getMaxCapacity()
        );
        serviceRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/news/add")
    public String addNewsPage() {
        return "admin/add-news";
    }

    @PostMapping("/news/add")
    public String processAddNews(@ModelAttribute NewsDto newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setPublicationDate(LocalDateTime.now());
        newsRepository.save(news);
        return "redirect:/news";
    }
}