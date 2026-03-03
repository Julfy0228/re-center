package com.recenter.service;

import com.recenter.dto.RegistrationRequestDto;
import com.recenter.dto.UserResponseDto;
import com.recenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void register(RegistrationRequestDto req) throws Exception {
        if (userRepository.findByEmail(req.getEmail()) != null) {
            throw new Exception("Пользователь с таким email уже существует");
        }
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(req);
    }

    public boolean checkPassword(String email, String rawPassword) {
        String storedPass = userRepository.findPasswordByEmail(email);
        return storedPass != null && passwordEncoder.matches(rawPassword, storedPass);
    }

    public void updateUser(UserResponseDto dto) {
        userRepository.update(dto);
    }
}