package com.recenter.model;

public class PromotionCategory {
    private int id;
    private int promotionId;
    private int categoryId;

    public PromotionCategory() {}

    public PromotionCategory(int id, int promotionId, int categoryId) {
        this.id = id;
        this.promotionId = promotionId;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
