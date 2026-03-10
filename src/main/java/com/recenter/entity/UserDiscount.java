package com.recenter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_discount")
public class UserDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "discount_id", nullable = false, foreignKey = @ForeignKey(name = "fk_userdiscount_discount"))
    private Discount discount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_userdiscount_user"))
    private User user;

    @Column(name = "is_used")
    private Integer isUsed;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public UserDiscount() {
    }

    public UserDiscount(Discount discount, User user, LocalDateTime assignedAt) {
        this.discount = discount;
        this.user = user;
        this.isUsed = 0;
        this.assignedAt = assignedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getIsUsed() { return isUsed; }
    public void setIsUsed(Integer isUsed) { this.isUsed = isUsed; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
