package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private PromotionCategory category;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromotionService> promotionServices = new ArrayList<>();

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromotionCategory> promotionCategories = new ArrayList<>();

    public void addPromotionService(Service service, BigDecimal discountPercent) {
        PromotionService ps = PromotionService.builder()
                .promotion(this)
                .service(service)
                .discountPercent(discountPercent)
                .build();
        promotionServices.add(ps);
    }

    public void addPromotionServiceCategory(Category category, BigDecimal discountPercent) {
        PromotionCategory psc = PromotionCategory.builder()
                .promotion(this)
                .serviceCategory(category)
                .discountPercent(discountPercent)
                .build();
        promotionCategories.add(psc);
    }
}