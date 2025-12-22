package com.recenter.repository;

import com.recenter.dto.RegistrationRequestDto;
import com.recenter.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(RegistrationRequestDto user) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getPhone(), "CLIENT");
    }

    public UserResponseDto findByEmail(String email) {
        try {
            String sql = "SELECT id, email, first_name, last_name, phone, role FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserResponseDto.class), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String findPasswordByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT password FROM users WHERE email = ?", String.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(UserResponseDto user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, phone=? WHERE email=?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail());
    }
}