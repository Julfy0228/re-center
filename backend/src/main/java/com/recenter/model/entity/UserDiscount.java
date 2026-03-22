package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserDiscounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDiscount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discountId", nullable = false)
    private Discount discount;

    @Builder.Default
    private boolean isUsed = false;

    private LocalDateTime expireDate;
}