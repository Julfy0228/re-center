package com.recenter.model;

import java.time.LocalDateTime;
import com.recenter.model.enums.DiscountType;

public class Discount {
    private int id;
    private String title;
    private DiscountType type;
    private double value;
    private LocalDateTime expireDate;

    public Discount() {}

    public Discount(int id, String title, DiscountType type, double value, LocalDateTime expireDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.value = value;
        this.expireDate = expireDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public DiscountType getType() { return type; }
    public void setType(DiscountType type) { this.type = type; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public LocalDateTime getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDateTime expireDate) { this.expireDate = expireDate; }
}
