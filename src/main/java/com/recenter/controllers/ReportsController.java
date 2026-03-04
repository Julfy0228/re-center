package com.recenter.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportsController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public String index(Model model) {
        Long usersCount = entityManager.createQuery("select count(u) from User u", Long.class)
                .getSingleResult();
        Long servicesCount = entityManager.createQuery("select count(s) from Service s", Long.class)
                .getSingleResult();
        Long bookingsCount = entityManager.createQuery("select count(b) from Booking b", Long.class)
                .getSingleResult();

        Double totalRevenue = entityManager.createQuery(
                        "select coalesce(sum(b.totalPrice), 0) from Booking b where b.status = 'PAID'",
                        Double.class
                )
                .getSingleResult();

        List<Object[]> bookingsByStatus = entityManager.createQuery(
                        "select b.status, count(b) from Booking b group by b.status order by count(b) desc",
                        Object[].class
                )
                .getResultList();

        List<Object[]> topServices = entityManager.createQuery(
                        "select b.service.title, count(b) from Booking b group by b.service.title order by count(b) desc",
                        Object[].class
                )
                .setMaxResults(5)
                .getResultList();

        model.addAttribute("usersCount", usersCount != null ? usersCount : 0);
        model.addAttribute("servicesCount", servicesCount != null ? servicesCount : 0);
        model.addAttribute("bookingsCount", bookingsCount != null ? bookingsCount : 0);
        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0);
        model.addAttribute("bookingsByStatus", bookingsByStatus);
        model.addAttribute("topServices", topServices);

        return "reports/index";
    }
}
