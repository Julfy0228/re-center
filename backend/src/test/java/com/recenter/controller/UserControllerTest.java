package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recenter.model.entity.User;
import com.recenter.model.enums.UserRole;
import com.recenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = User.builder()
                .id(10L)
                .email("user@example.com")
                .firstName("Иван")
                .lastName("Петров")
                .middleName("Сергеевич")
                .phoneNumber("+7-999-123-45-67")
                .role(UserRole.CLIENT)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void authenticateAsAdmin() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("admin@example.com")
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void authenticateAsUser(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("pass")
                .roles("CLIENT")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // -----------------------------
    // GET /api/users/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsUser() throws Exception {
        when(userService.getById(10L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(userService.getById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/10"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/users
    // -----------------------------
    @Test
    void getAll_ReturnsUserList() throws Exception {
        authenticateAsAdmin();

        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("Иван"));
    }

    // -----------------------------
    // GET /api/users/my/profile
    // -----------------------------
    @Test
    void getMyProfile_ReturnsCurrentUserProfile() throws Exception {
        authenticateAsUser(user);

        when(userService.getByEmail(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/my/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    // -----------------------------
    // PUT /api/users/{id}
    // -----------------------------
    @Test
    void update_Found_ReturnsUpdatedUser() throws Exception {
        authenticateAsAdmin();

        UserController.UserRequest request = new UserController.UserRequest();
        request.setFirstName("Петр");
        request.setLastName("Сидоров");
        request.setMiddleName("Иванович");
        request.setPhoneNumber("+7-999-987-65-43");

        User updated = User.builder()
                .id(10L)
                .email("user@example.com")
                .firstName("Петр")
                .lastName("Сидоров")
                .middleName("Иванович")
                .phoneNumber("+7-999-987-65-43")
                .role(UserRole.CLIENT)
                .createdAt(user.getCreatedAt())
                .build();

        when(userService.update(eq(10L), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.lastName").value("Сидоров"));
    }

    @Test
    void update_NotFound_Returns404() throws Exception {
        authenticateAsAdmin();

        UserController.UserRequest request = new UserController.UserRequest();
        request.setFirstName("Петр");
        request.setLastName("Сидоров");

        when(userService.update(eq(10L), any(User.class))).thenReturn(null);

        mockMvc.perform(put("/api/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // DELETE /api/users/{id} (ADMIN only)
    // -----------------------------
    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        authenticateAsAdmin();

        doNothing().when(userService).delete(10L);

        mockMvc.perform(delete("/api/users/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService).delete(10L);
    }
}
