package com.recenter.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import com.recenter.model.enums.DiscountType;

/**
 * Скидка, которая может быть применена к услугам.
 * <p>
 * Может быть процентной (PERCENT) или фиксированной (AMOUNT).
 * Действует в указанный период.
 * </p>
 *
 * @see DiscountType
 * @see UserDiscount
 */
@Entity
@Table(name = "Discounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Discount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private DiscountType type;

    @Column(precision = 10, scale = 2)
    private BigDecimal value;
}