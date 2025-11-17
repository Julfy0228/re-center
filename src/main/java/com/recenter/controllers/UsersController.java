package com.recenter.controllers;

import com.recenter.dto.RegistrationRequestDto;
import com.recenter.dto.LoginRequestDto;
import com.recenter.dto.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final Map<Long, UserResponseDto> users = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegistrationRequestDto req) {
        long id = idGen.getAndIncrement();
        UserResponseDto u = new UserResponseDto();
        u.setId(id);
        u.setEmail(req.getEmail());
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setPhone(req.getPhone());
        u.setRole("CLIENT");
        u.setDeleted(false);
        u.setRegistrationDate(LocalDateTime.now());
        users.put(id, u);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto req) {
        for (UserResponseDto u : users.values()) {
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(req.getEmail())) {
                return ResponseEntity.ok(u);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable("id") Long id) {
        UserResponseDto u = users.get(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }
}
