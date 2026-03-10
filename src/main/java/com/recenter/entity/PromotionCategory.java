package com.recenter.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "promotion_category")
public class PromotionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_promotioncategory_promotion"))
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_promotioncategory_category"))
    private Category category;

    public PromotionCategory() {
    }

    public PromotionCategory(Promotion promotion, Category category) {
        this.promotion = promotion;
        this.category = category;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Promotion getPromotion() { return promotion; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
