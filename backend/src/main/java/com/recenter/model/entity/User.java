package com.recenter.model.entity;

import jakarta.persistence.*;
import lombok.*;
import com.recenter.model.enums.UserRole;
import java.time.LocalDateTime;

/**
 * Пользователь системы.
 * <p>
 * Содержит данные для аутентификации и профиль пользователя.
 * Аутентификация выполняется по email (поле {@code email}).
 * </p>
 *
 * @see UserRole
 * @see Activity
 * @see Booking
 * @see Notification
 * @see News
 * @see UserDiscount
 */
@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role;

    private String firstName;
    private String lastName;
    private String middleName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private String passwordHash;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}