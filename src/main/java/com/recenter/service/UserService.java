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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setRegistrationDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(String email, String rawPassword) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return passwordEncoder.matches(rawPassword, userOpt.get().getPasswordHash());
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

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

    private UserResponseDto entityToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId() != null ? user.getId().longValue() : null);
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        return dto;
    }
}