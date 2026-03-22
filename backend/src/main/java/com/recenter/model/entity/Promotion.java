package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Маркетинговая акция.
 * <p>
 * Может применяться к конкретным услугам или целым категориям услуг.
 * Для каждой услуги/категории можно задать индивидуальный процент скидки.
 * </p>
 *
 * @see PromotionCategory
 * @see PromotionService
 * @see Category
 * @see Service
 */
@Entity
@Table(name = "Promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromotionService> promotionServices = new ArrayList<>();

    public void addPromotionService(Service service, BigDecimal discountPercent) {
        PromotionService ps = PromotionService.builder()
                .promotion(this)
                .service(service)
                .discountPercent(discountPercent)
                .build();
        promotionServices.add(ps);
    }
}