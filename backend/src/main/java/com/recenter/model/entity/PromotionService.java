package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Связующая сущность между акцией и конкретной услугой.
 * <p>
 * Позволяет задать для услуги в рамках акции индивидуальную скидку.
 * </p>
 *
 * @see Promotion
 * @see Service
 */
@Entity
@Table(name = "PromotionServices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotionId", nullable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;

    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercent;
}