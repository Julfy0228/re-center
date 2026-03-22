package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Услуга, предоставляемая компанией.
 * <p>
 * Содержит описание, длительность, стоимость, максимальное количество участников
 * и ссылку на категорию.
 * </p>
 *
 * @see Category
 * @see Booking
 * @see PromotionService
 */
@Entity
@Table(name = "Services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    private String description;

    private String serviceType;

    private Integer duration;

    private BigDecimal price;

    private Integer maxPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;
}