package com.recenter.controller;

import com.recenter.model.dto.*;
import com.recenter.model.entity.User;
import com.recenter.model.enums.UserRole;
import com.recenter.repository.UserRepository;
import com.recenter.security.JwtUtils;
import com.recenter.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер аутентификации и авторизации для системы бронирования базы отдыха.
 * <p>
 * Предоставляет endpoints для входа, регистрации новых пользователей и получения
 * информации о текущем авторизованном пользователе.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Аутентифицирует пользователя по email и паролю.
     * <p>
     * При успешной аутентификации генерируется JWT-токен, который возвращается вместе
     * с идентификатором пользователя, именем и ролью. Токен должен передаваться в
     * заголовке {@code Authorization} для доступа к защищённым ресурсам.
     *
     * @param loginRequest объект с email и паролем пользователя
     * @return {@link AuthResponse} с токеном и данными пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AuthResponse response = new AuthResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Регистрирует нового пользователя в системе.
     * <p>
     * Проверяет уникальность email. Пароль сохраняется в зашифрованном виде.
     * Новым пользователям автоматически присваивается роль {@link UserRole#CLIENT}.
     * После успешной регистрации пользователь может войти в систему через {@code /login}.
     *
     * @param registerRequest объект с данными для регистрации (email, пароль, имя, фамилия, телефон и т.д.)
     * @return сообщение об успешной регистрации или ошибка, если email уже используется
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .passwordHash(encoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .middleName(registerRequest.getMiddleName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(UserRole.CLIENT)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Возвращает информацию о текущем авторизованном пользователе.
     * <p>
     * Эндпоинт доступен только для аутентифицированных пользователей. Данные извлекаются
     * из контекста безопасности и базы данных.
     *
     * @param authentication объект аутентификации, автоматически подставляемый Spring Security
     * @return {@link UserResponse} с полными данными пользователя или статус 401, если пользователь не авторизован
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setMiddleName(user.getMiddleName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(response);
    }
}