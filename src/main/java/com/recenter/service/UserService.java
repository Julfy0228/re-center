package com.recenter.service;

import com.recenter.dto.RegistrationRequestDto;
import com.recenter.dto.UserResponseDto;
import com.recenter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for User operations
 * Работает с User Entity через JPA Repository
 * Преобразует между Entity и DTO для контроллеров и представлений
 */
@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Найти пользователя по email и вернуть DTO
     * @param email адрес пользователя
     * @return DTO пользователя или null
     */
    public UserResponseDto findByEmail(String email) {
        try {
            String sql = "SELECT id, email, first_name, last_name, phone, role FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserResponseDto.class), email);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Регистрация нового пользователя
     * @param req данные регистрации
     * @throws Exception если пользователь уже существует
     */
    @Transactional
    public void register(RegistrationRequestDto req) throws Exception {
        Integer exists = jdbcTemplate.queryForObject("SELECT count(*) FROM users WHERE email = ?", Integer.class, req.getEmail());
        if (exists != null && exists > 0) {
            throw new Exception("Пользователь с таким email уже существует");
        }

        jdbcTemplate.update(
                "INSERT INTO users (email, password, first_name, last_name, phone, role, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getFirstName(),
                req.getLastName(),
                req.getPhone(),
                "CLIENT",
                java.time.LocalDateTime.now()
        );
    }

    /**
     * Проверка пароля пользователя
     * @param email адрес пользователя
     * @param rawPassword введённый пароль в открытом виде
     * @return true если пароль верный
     */
    @Transactional(readOnly = true)
    public boolean checkPassword(String email, String rawPassword) {
        try {
            String hash = jdbcTemplate.queryForObject("SELECT password FROM users WHERE email = ?", String.class, email);
            return hash != null && passwordEncoder.matches(rawPassword, hash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Обновить данные пользователя
     * @param dto DTO с новыми данными
     */
    @Transactional
    public void updateUser(UserResponseDto dto) {
        jdbcTemplate.update(
                "UPDATE users SET first_name=?, last_name=?, phone=? WHERE email=?",
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhone(),
                dto.getEmail()
        );
    }

    /**
     * Преобразование User Entity в UserResponseDto
     */
    private UserResponseDto entityToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        return dto;
    }
}