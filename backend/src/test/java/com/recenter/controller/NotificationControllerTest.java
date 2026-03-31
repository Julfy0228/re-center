package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.dto.NotificationRequest;
import com.recenter.model.entity.Notification;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NotificationType;
import com.recenter.model.enums.UserRole;
import com.recenter.repository.UserRepository;
import com.recenter.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationController notificationController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private Notification notification;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();

        objectMapper.registerModule(new JavaTimeModule());

        user = User.builder()
                .id(10L)
                .email("user@example.com")
                .role(UserRole.CLIENT)
                .build();

        notification = Notification.builder()
                .id(1L)
                .user(user)
                .type(NotificationType.INFO)
                .title("Бронирование подтверждено")
                .message("Ваш домик успешно забронирован")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void authenticateUser() {
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

        lenient().when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
    }

    @Test
    void create_ReturnsCreatedNotification() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setTitle("Бронирование подтверждено");
        request.setMessage("Ваш домик успешно забронирован");
        request.setType(NotificationType.INFO);

        when(notificationService.create(any(Notification.class))).thenReturn(notification);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Бронирование подтверждено"));
    }

    @Test
    void getById_Found_ReturnsNotification() throws Exception {
        when(notificationService.getById(1L)).thenReturn(Optional.of(notification));

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Бронирование подтверждено"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(notificationService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnsList() throws Exception {
        when(notificationService.getAll()).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Бронирование подтверждено"));
    }

    @Test
    void getMyNotifications_ReturnsUserNotifications() throws Exception {
        authenticateUser();

        when(notificationService.getByUser(user)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Ваш домик успешно забронирован"));
    }

    @Test
    void getUnread_ReturnsUnreadNotifications() throws Exception {
        authenticateUser();

        when(notificationService.getUnreadByUser(user)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/my/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].read").value(false));
    }

    @Test
    void getUnreadCount_ReturnsCount() throws Exception {
        authenticateUser();

        when(notificationService.getUnreadCount(user)).thenReturn(3);

        mockMvc.perform(get("/api/notifications/my/unread/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void markRead_ReturnsUpdatedNotification() throws Exception {
        authenticateUser();

        Notification updated = Notification.builder()
                .id(notification.getId())
                .user(notification.getUser())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(true)
                .createdAt(notification.getCreatedAt())
                .build();

        when(notificationService.markAsRead(1L)).thenReturn(updated);

        mockMvc.perform(put("/api/notifications/1/mark-read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read").value(true));
    }

    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(notificationService).delete(1L);

        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification deleted successfully"));

        verify(notificationService).delete(1L);
    }
}
