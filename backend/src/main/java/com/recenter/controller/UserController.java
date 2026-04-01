package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.UserRequest;
import com.recenter.model.dto.UserResponse;
import com.recenter.model.entity.User;
import com.recenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable("id") Long id) {
        return userService.getById(id)
                .map(user -> ResponseEntity.ok(EntityDtoMapper.toUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> responses = userService.getAll().stream()
                .map(EntityDtoMapper::toUserResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my/profile")
    public ResponseEntity<UserResponse> getMyProfile() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(EntityDtoMapper.toUserResponse(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (#id == authentication.principal.id)")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long id, @RequestBody UserRequest request) {
        User userDetails = new User();
        userDetails.setFirstName(request.getFirstName());
        userDetails.setLastName(request.getLastName());
        userDetails.setMiddleName(request.getMiddleName());
        userDetails.setPhoneNumber(request.getPhoneNumber());

        User updated = userService.update(id, userDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toUserResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
