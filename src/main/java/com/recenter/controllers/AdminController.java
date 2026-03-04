package com.recenter.controllers;

import com.recenter.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TransactionTemplate txTemplate;

    @Autowired
    public AdminController(PlatformTransactionManager transactionManager) {
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    @GetMapping
    public String dashboard(Model model) {
        Integer newBookingsCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM bookings WHERE status = 'CREATED'",
                Integer.class
        );
        Double revenue = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE status = 'PAID'",
                Double.class
        );

        model.addAttribute("newBookingsCount", newBookingsCount != null ? newBookingsCount : 0);
        model.addAttribute("revenue", revenue != null ? revenue : 0);
        model.addAttribute("freeRooms", 0);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model,
                        @RequestParam(value = "created", required = false) String created,
                        @RequestParam(value = "error", required = false) String error) {
        List<Map<String, Object>> users = jdbcTemplate.queryForList(
                "SELECT id, email, first_name, last_name, phone, role, created_at FROM users ORDER BY id"
        );
        model.addAttribute("users", users);
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
            txTemplate.executeWithoutResult(status -> jdbcTemplate.update(
                    "INSERT INTO users (email, password, first_name, last_name, phone, role, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    email.trim(),
                    passwordEncoder.encode(password),
                    firstName,
                    lastName,
                    phone,
                    normalizedRole,
                    LocalDateTime.now()
            ));
        } catch (DuplicateKeyException ex) {
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

        txTemplate.executeWithoutResult(status -> jdbcTemplate.update("UPDATE users SET role = ? WHERE id = ?", normalized, id));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id, Authentication authentication) {
        if (id == null) {
            return "redirect:/admin/users";
        }

        Long currentUserId = null;
        if (authentication != null && authentication.getName() != null) {
            currentUserId = jdbcTemplate.queryForObject(
                    "SELECT id FROM users WHERE email = ?",
                    Long.class,
                    authentication.getName()
            );
        }

        if (currentUserId != null && currentUserId.equals(id)) {
            return "redirect:/admin/users";
        }

        String role = jdbcTemplate.queryForObject("SELECT role FROM users WHERE id = ?", String.class, id);
        if (role != null && role.trim().equalsIgnoreCase("ADMIN")) {
            return "redirect:/admin/users";
        }

        txTemplate.executeWithoutResult(status -> {
            jdbcTemplate.update("DELETE FROM bookings WHERE user_id = ?", id);
            jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
        });

        return "redirect:/admin/users";
    }

    @GetMapping("/services/add")
    public String addServicePage() {
        return "admin/add-service";
    }

    @PostMapping("/services/add")
    public String processAddService(@ModelAttribute ServiceDetailDto serviceDto) {
        jdbcTemplate.update(
                "INSERT INTO services (title, description, base_price, service_type, min_capacity, max_capacity) VALUES (?, ?, ?, ?, ?, ?)",
                serviceDto.getTitle(),
                serviceDto.getDescription(),
                serviceDto.getBasePrice() != null ? serviceDto.getBasePrice().doubleValue() : null,
                serviceDto.getServiceType(),
                serviceDto.getMinCapacity(),
                serviceDto.getMaxCapacity()
        );
        return "redirect:/services";
    }

    @GetMapping("/news/add")
    public String addNewsPage() {
        return "admin/add-news";
    }

    @PostMapping("/news/add")
    public String processAddNews(@ModelAttribute NewsDto newsDto) {
        jdbcTemplate.update(
                "INSERT INTO news (title, content, publication_date) VALUES (?, ?, ?)",
                newsDto.getTitle(),
                newsDto.getContent(),
                LocalDateTime.now().toString()
        );
        return "redirect:/news";
    }
}