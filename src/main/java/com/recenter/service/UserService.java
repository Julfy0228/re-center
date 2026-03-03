package com.recenter.service;

import com.recenter.dto.RegistrationRequestDto;
import com.recenter.dto.UserResponseDto;
import com.recenter.entity.User;
import com.recenter.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Найти пользователя по email и вернуть DTO
     * @param email адрес пользователя
     * @return DTO пользователя или null
     */
    public UserResponseDto findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
            .map(this::entityToDto)
            .orElse(null);
    }

    /**
     * Регистрация нового пользователя
     * @param req данные регистрации
     * @throws Exception если пользователь уже существует
     */
    @Transactional
    public void register(RegistrationRequestDto req) throws Exception {
        if (userJpaRepository.existsByEmail(req.getEmail())) {
            throw new Exception("Пользователь с таким email уже существует");
        }

        User newUser = new User(
            req.getEmail(),
            passwordEncoder.encode(req.getPassword()),
            req.getFirstName(),
            req.getLastName(),
            "CLIENT"  // По умолчанию новый пользователь - клиент
        );
        newUser.setPhone(req.getPhone());
        newUser.setCreatedAt(java.time.LocalDateTime.now());
        
        userJpaRepository.save(newUser);
    }

    /**
     * Проверка пароля пользователя
     * @param email адрес пользователя
     * @param rawPassword введённый пароль в открытом виде
     * @return true если пароль верный
     */
    @Transactional(readOnly = true)
    public boolean checkPassword(String email, String rawPassword) {
        return userJpaRepository.findByEmail(email)
            .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
            .orElse(false);
    }

    /**
     * Обновить данные пользователя
     * @param dto DTO с новыми данными
     */
    @Transactional
    public void updateUser(UserResponseDto dto) {
        userJpaRepository.findByEmail(dto.getEmail())
            .ifPresent(user -> {
                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setPhone(dto.getPhone());
                userJpaRepository.save(user);
            });
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