package com.recenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager/bookings")
public class ManagerBookingsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TransactionTemplate txTemplate;

    @Autowired
    public ManagerBookingsController(PlatformTransactionManager transactionManager) {
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    @GetMapping
    public String list(@RequestParam(value = "status", required = false) String status,
                       Model model) {

        List<Map<String, Object>> bookings;

        if (status != null && !status.isBlank()) {
            bookings = jdbcTemplate.queryForList(
                    "SELECT b.id, b.user_id, u.email AS user_email, b.service_id, s.title AS service_title, " +
                            "b.start_date, b.end_date, b.total_price, b.status " +
                            "FROM bookings b " +
                            "LEFT JOIN users u ON u.id = b.user_id " +
                            "LEFT JOIN services s ON s.id = b.service_id " +
                            "WHERE b.status = ? " +
                            "ORDER BY b.id DESC",
                    status.trim().toUpperCase()
            );
        } else {
            bookings = jdbcTemplate.queryForList(
                    "SELECT b.id, b.user_id, u.email AS user_email, b.service_id, s.title AS service_title, " +
                            "b.start_date, b.end_date, b.total_price, b.status " +
                            "FROM bookings b " +
                            "LEFT JOIN users u ON u.id = b.user_id " +
                            "LEFT JOIN services s ON s.id = b.service_id " +
                            "ORDER BY b.id DESC"
            );
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

        txTemplate.executeWithoutResult(tx -> jdbcTemplate.update("UPDATE bookings SET status = ? WHERE id = ?", normalized, id));
        return "redirect:/manager/bookings";
    }
}
