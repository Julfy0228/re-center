package com.recenter.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Бронирование (заказ) услуги пользователем.
 * <p>
 * Содержит информацию о выбранной услуге, дате, количестве участников,
 * финальной цене и статусе бронирования.
 * </p>
 *
 * @see User
 * @see Service
 * @see Payment
 * @see Review
 */
@Entity
@Table(name = "Bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;

    private LocalDateTime startDate;

    private Integer peopleCount;

    @Column(precision = 10, scale = 2)
    private BigDecimal initialPrice;

    private String status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}