package com.recenter.service;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import com.recenter.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getByBooking(Booking booking) {
        return paymentRepository.findByBooking(booking);
    }

    public List<Payment> getByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getByPaymentMethod(String method) {
        return paymentRepository.findByPaymentMethod(method);
    }

    public List<Payment> getPending() {
        return getByStatus("PENDING");
    }

    public List<Payment> getCompleted() {
        return getByStatus("COMPLETED");
    }

    public Payment update(Long id, Payment paymentDetails) {
        return paymentRepository.findById(id).map(payment -> {
            if (paymentDetails.getAmount() != null) {
                payment.setAmount(paymentDetails.getAmount());
            }
            if (paymentDetails.getPaymentDate() != null) {
                payment.setPaymentDate(paymentDetails.getPaymentDate());
            }
            if (paymentDetails.getStatus() != null) {
                payment.setStatus(paymentDetails.getStatus());
            }
            if (paymentDetails.getPaymentMethod() != null) {
                payment.setPaymentMethod(paymentDetails.getPaymentMethod());
            }
            return paymentRepository.save(payment);
        }).orElse(null);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    public long count() {
        return paymentRepository.count();
    }
}
