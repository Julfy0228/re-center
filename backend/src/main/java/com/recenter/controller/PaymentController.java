package com.recenter.controller;

import com.recenter.model.dto.PaymentRequest;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import com.recenter.repository.BookingRepository;
import com.recenter.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления платежами и оплатами.
 * <p>
 * Предоставляет API endpoints для управления платежами за бронирования.
 * Требует роль ADMIN для просмотра и изменения платежей.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Создаёт новый платёж.
     *
     * @param request DTO с данными платежа
     * @return созданный платёж
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
     * Получает платёж по идентификатору (требует ADMIN).
     *
     * @param id идентификатор платежа
     * @return платёж или 404, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Payment> payment = paymentService.getById(id);
        return payment.isPresent() ? ResponseEntity.ok(payment.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех платежей (требует ADMIN).
     *
     * @return список всех платежей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    /**
     * Получает платежи по статусу (требует ADMIN).
     *
     * @param status статус платежа
     * @return список платежей с указанным статусом
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByStatus(@PathVariable("status") String status) {
        List<Payment> payments = paymentService.getByStatus(status);
        return ResponseEntity.ok(payments);
    }

    /**
     * Получает список ожидающих платежей (требует ADMIN).
     *
     * @return список платежей со статусом PENDING
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPending() {
        List<Payment> payments = paymentService.getPending();
        return ResponseEntity.ok(payments);
    }

    /**
     * Получает список завершённых платежей (требует ADMIN).
     *
     * @return список платежей со статусом COMPLETED
     */
    @GetMapping("/completed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCompleted() {
        List<Payment> payments = paymentService.getCompleted();
        return ResponseEntity.ok(payments);
    }

    /**
     * Отмечает платёж как завершённый (требует ADMIN).
     *
     * @param id идентификатор платежа
     * @return обновлённый платёж или 404, если не найден
     */
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> completePayment(@PathVariable("id") Long id) {
        Payment paymentDetails = new Payment();
        paymentDetails.setStatus("COMPLETED");
        paymentDetails.setPaymentDate(LocalDateTime.now());

        Payment updated = paymentService.update(id, paymentDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Удаляет платёж по идентификатору (требует ADMIN).
     *
     * @param id идентификатор платежа
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        paymentService.delete(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}