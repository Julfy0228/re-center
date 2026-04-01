package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.NotificationRequest;
import com.recenter.model.dto.NotificationResponse;
import com.recenter.model.entity.Notification;
import com.recenter.model.entity.User;
import com.recenter.service.NotificationService;
import com.recenter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
        User user = userService.getById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .build();

        Notification created = notificationService.create(notification);
        return ResponseEntity.ok(EntityDtoMapper.toNotificationResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable("id") Long id) {
        return notificationService.getById(id)
                .map(notification -> ResponseEntity.ok(EntityDtoMapper.toNotificationResponse(notification)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAll() {
        List<NotificationResponse> responses = notificationService.getAll().stream()
                .map(EntityDtoMapper::toNotificationResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<NotificationResponse> responses = notificationService.getByUser(user).stream()
                .map(EntityDtoMapper::toNotificationResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my/unread")
    public ResponseEntity<List<NotificationResponse>> getMyUnreadNotifications() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<NotificationResponse> responses = notificationService.getUnreadByUser(user).stream()
                .map(EntityDtoMapper::toNotificationResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my/unread/count")
    public ResponseEntity<Integer> getUnreadCount() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(notificationService.getUnreadCount(user));
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable("id") Long id) {
        Notification updated = notificationService.markAsRead(id);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toNotificationResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}
