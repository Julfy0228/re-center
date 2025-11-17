package com.recenter.model;

public class PromotionService {
    private int id;
    private int promotionId;
    private int serviceId;

    public PromotionService() {}

    public PromotionService(int id, int promotionId, int serviceId) {
        this.id = id;
        this.promotionId = promotionId;
        this.serviceId = serviceId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
}
