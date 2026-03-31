package com.recenter.controller;

import com.recenter.model.dto.PaymentRequest;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import com.recenter.model.entity.User;
import com.recenter.repository.BookingRepository;
import com.recenter.repository.UserRepository;
import com.recenter.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
/**
 * REST контроллер для управления платежами и оплатами.
 * 
 * Предоставляет API endpoints для управления платежами за бронирования.
 * Требует роль ADMIN для просмотра и изменения платежей.
 */
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создает новый платеж.
     * 
     * @param request DTO с данными платежа
     * @return созданный платеж
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getAmount())
                .paymentDate(LocalDateTime.now())
                .status("PENDING")
                .paymentMethod(request.getPaymentMethod())
                .build();

        Payment created = paymentService.create(payment);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает платеж по идентификатору (требует ADMIN).
     * 
     * @param id идентификатор платежа
     * @return платеж или 404
     */
    /**
     * Получает все платежи (требует ADMIN).
     * 
     * @return список всех платежей
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getById(id);
        return payment.isPresent() ? ResponseEntity.ok(payment.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * Получает платежи по статусу (требует ADMIN).
     * 
     * @param status статус платежа
     * @return список платежей с указанным статусом
     */
    public ResponseEntity<?> getAll() {
    /**
     * Получает ожидающие платежи (требует ADMIN).
     * 
     * @return список платежей со статусом PENDING
     */
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
    /**
     * Получает завершенные платежи (требует ADMIN).
     * 
     * @return список платежей со статусом COMPLETED
     */
        List<Payment> payments = paymentService.getByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPending() {
        List<Payment> payments = paymentService.getPending();
    /**
     * Отмечает платеж как завершенный (требует ADMIN).
     * 
     * @param id идентификатор платежа
     * @return обновленный платеж или 404
     */
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/completed")
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * Удаляет платеж (требует ADMIN).
     * 
     * @param id идентификатор платежа
     * @return сообщение об успешном удалении
     */
    public ResponseEntity<?> getCompleted() {
        List<Payment> payments = paymentService.getCompleted();
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> completePayment(@PathVariable Long id) {
        Payment paymentDetails = new Payment();
        paymentDetails.setStatus("COMPLETED");
        paymentDetails.setPaymentDate(LocalDateTime.now());

        Payment updated = paymentService.update(id, paymentDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}
