package com.recenter.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.recenter.model.enums.UserRole;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * Пользователь системы.
 * <p>
 * Реализует интерфейс {@link org.springframework.security.core.userdetails.UserDetails}
 * для интеграции со Spring Security.
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
public class User implements UserDetails {

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
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
             message = "Неверный формат email")
    private String email;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$",
             message = "Номер должен быть в формате +71234567890")
    @Size(min = 10, max = 20)
    @Column(unique = true)
    private String phoneNumber;

    private String passwordHash;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder.Default
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return enabled; }
}