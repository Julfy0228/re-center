package com.recenter.model;

import java.time.LocalDateTime;
import com.recenter.model.enums.PaymentStatus;

public class Payment {
    private int id;
    private int bookingId;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private PaymentStatus status;

    public Payment() {}

    public Payment(int id, int bookingId, double amount, LocalDateTime paymentDate, String paymentMethod, PaymentStatus status) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
}
