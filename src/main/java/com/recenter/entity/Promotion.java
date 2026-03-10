package com.recenter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "discount_percent")
    private Double discountPercent;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionService> promotionServices = new ArrayList<>();

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionCategory> promotionCategories = new ArrayList<>();

    public Promotion() {
    }

    public Promotion(String title, String description, LocalDateTime startDate, LocalDateTime endDate, Double discountPercent) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercent = discountPercent;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public List<PromotionService> getPromotionServices() { return promotionServices; }
    public void setPromotionServices(List<PromotionService> promotionServices) { this.promotionServices = promotionServices; }

    public List<PromotionCategory> getPromotionCategories() { return promotionCategories; }
    public void setPromotionCategories(List<PromotionCategory> promotionCategories) { this.promotionCategories = promotionCategories; }
}
