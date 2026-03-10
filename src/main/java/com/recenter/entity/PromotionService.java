package com.recenter.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "promotion_service")
public class PromotionService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_promotionservice_promotion"))
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false, foreignKey = @ForeignKey(name = "fk_promotionservice_service"))
    private Service service;

    public PromotionService() {
    }

    public PromotionService(Promotion promotion, Service service) {
        this.promotion = promotion;
        this.service = service;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Promotion getPromotion() { return promotion; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }
}
