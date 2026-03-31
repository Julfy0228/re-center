package com.recenter.controller;

import com.recenter.model.dto.UserResponse;
import com.recenter.model.entity.User;
import com.recenter.repository.UserRepository;
import com.recenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления пользователями системы.
 * <p>
 * Предоставляет API endpoints для получения информации о пользователях, управления профилем,
 * обновления данных и удаления учётных записей (требует разные уровни доступа).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return профиль пользователя или 404, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<User> user = userService.getById(id);
        if (user.isPresent()) {
            UserResponse response = mapToUserResponse(user.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех пользователей системы (требует ADMIN).
     *
     * @return список всех пользователей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll() {
        List<User> users = userService.getAll();
        List<UserResponse> responses = users.stream().map(this::mapToUserResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Получает профиль текущего авторизованного пользователя.
     *
     * @return профиль аутентифицированного пользователя
     */
    @GetMapping("/my/profile")
    public ResponseEntity<?> getMyProfile() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = mapToUserResponse(user);
        return ResponseEntity.ok(response);
    }

    /**
     * Обновляет данные пользователя (может ADMIN или сам пользователь).
     *
     * @param id      идентификатор пользователя
     * @param request новые данные пользователя
     * @return обновлённый профиль или 404, если не найден
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (#id == authentication.principal.id)")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserRequest request) {
        User userDetails = new User();
        userDetails.setFirstName(request.getFirstName());
        userDetails.setLastName(request.getLastName());
        userDetails.setMiddleName(request.getMiddleName());
        userDetails.setPhoneNumber(request.getPhoneNumber());

        User updated = userService.update(id, userDetails);
        if (updated != null) {
            UserResponse response = mapToUserResponse(updated);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Удаляет учётную запись пользователя (требует ADMIN).
     *
     * @param id идентификатор пользователя
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Вспомогательный метод для преобразования сущности User в DTO UserResponse.
     *
     * @param user сущность User
     * @return DTO с информацией пользователя
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * Класс для передачи в PUT-запросе данных обновления пользователя.
     */
    public static class UserRequest {
        private String firstName;
        private String lastName;
        private String middleName;
        private String phoneNumber;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getMiddleName() { return middleName; }
        public void setMiddleName(String middleName) { this.middleName = middleName; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }
}
