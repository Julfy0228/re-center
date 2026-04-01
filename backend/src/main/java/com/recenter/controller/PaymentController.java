package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.PaymentRequest;
import com.recenter.model.dto.PaymentResponse;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import com.recenter.service.BookingService;
import com.recenter.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        Booking booking = bookingService.getById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getAmount())
                .paymentDate(LocalDateTime.now())
                .status("PENDING")
                .paymentMethod(request.getPaymentMethod())
                .build();

        Payment created = paymentService.create(payment);
        return ResponseEntity.ok(EntityDtoMapper.toPaymentResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable("id") Long id) {
        return paymentService.getById(id)
                .map(payment -> ResponseEntity.ok(EntityDtoMapper.toPaymentResponse(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAll() {
        List<PaymentResponse> responses = paymentService.getAll().stream()
                .map(EntityDtoMapper::toPaymentResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getByStatus(@PathVariable("status") String status) {
        List<PaymentResponse> responses = paymentService.getByStatus(status).stream()
                .map(EntityDtoMapper::toPaymentResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getPending() {
        List<PaymentResponse> responses = paymentService.getPending().stream()
                .map(EntityDtoMapper::toPaymentResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/completed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getCompleted() {
        List<PaymentResponse> responses = paymentService.getCompleted().stream()
                .map(EntityDtoMapper::toPaymentResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> completePayment(@PathVariable("id") Long id) {
        Payment paymentDetails = new Payment();
        paymentDetails.setStatus("COMPLETED");
        paymentDetails.setPaymentDate(LocalDateTime.now());

        Payment updated = paymentService.update(id, paymentDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toPaymentResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        paymentService.delete(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}
