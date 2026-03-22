package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Связь пользователя со скидкой.
 * <p>
 * Фиксирует, какая скидка была выдана пользователю, использована ли она,
 * и срок её действия.
 * </p>
 *
 * @see User
 * @see Discount
 */
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