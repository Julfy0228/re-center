package com.recenter.security;

import com.recenter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Security UserDetailsService implementation
 * Загружает пользователей из БД через JPA Repository
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            String sql = "SELECT id, email, password, first_name, last_name, phone, role, created_at FROM users WHERE email = ?";
            RowMapper<User> mapper = (rs, rowNum) -> {
                User u = new User();
                long id = rs.getLong("id");
                if (!rs.wasNull()) {
                    u.setId(id);
                }
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setPhone(rs.getString("phone"));
                u.setRole(rs.getString("role"));
                try {
                    String createdAt = rs.getString("created_at");
                    if (createdAt != null && !createdAt.isBlank()) {
                        u.setCreatedAt(java.time.LocalDateTime.parse(createdAt));
                    }
                } catch (Exception ignored) {
                }
                return u;
            };
            user = jdbcTemplate.queryForObject(sql, mapper, username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }

        if (user == null || user.getPassword() == null || user.getPassword().isBlank()) {
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}

