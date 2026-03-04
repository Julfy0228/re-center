package com.recenter.service;

import com.recenter.dto.RegistrationRequestDto;
import com.recenter.dto.UserResponseDto;
import com.recenter.entity.User;
import com.recenter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for User operations
 * Работает с User Entity через JPA Repository
 * Преобразует между Entity и DTO для контроллеров и представлений
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Найти пользователя по email и вернуть DTO
     * @param email адрес пользователя
     * @return DTO пользователя или null
     */
    public UserResponseDto findByEmail(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return entityToDto(userOpt.get());
            }
            return null;
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
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new Exception("Пользователь с таким email уже существует");
        }

        User user = new User(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getFirstName(),
                req.getLastName(),
                "CLIENT"
        );
        user.setPhone(req.getPhone());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
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
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
            }
            return false;
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
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setPhone(dto.getPhone());
            userRepository.save(user);
        }
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