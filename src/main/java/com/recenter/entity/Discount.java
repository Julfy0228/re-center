package com.recenter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double value;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY)
    private List<UserDiscount> userDiscounts = new ArrayList<>();

    public Discount() {
    }

    public Discount(String title, String type, Double value, LocalDateTime expireDate) {
        this.title = title;
        this.type = type;
        this.value = value;
        this.expireDate = expireDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public LocalDateTime getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDateTime expireDate) { this.expireDate = expireDate; }

    public List<UserDiscount> getUserDiscounts() { return userDiscounts; }
    public void setUserDiscounts(List<UserDiscount> userDiscounts) { this.userDiscounts = userDiscounts; }
}
