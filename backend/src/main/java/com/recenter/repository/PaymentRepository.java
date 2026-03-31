package com.recenter.repository;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking(Booking booking);
    List<Payment> findByStatus(String status);
    List<Payment> findByPaymentMethod(String method);
}
