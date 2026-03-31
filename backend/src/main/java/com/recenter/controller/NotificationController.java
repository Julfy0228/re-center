package com.recenter.controller;

import com.recenter.model.entity.Notification;
import com.recenter.model.entity.User;
import com.recenter.repository.UserRepository;
import com.recenter.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления уведомлениями пользователей.
 * <p>
 * Предоставляет API endpoints для просмотра и управления уведомлениями.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создаёт новое уведомление.
     *
     * @param notification данные уведомления
     * @return созданное уведомление
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Notification notification) {
        Notification created = notificationService.create(notification);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     * @return уведомление или 404, если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Notification> notification = notificationService.getById(id);
        return notification.isPresent() ? ResponseEntity.ok(notification.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех уведомлений.
     *
     * @return список всех уведомлений
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Notification> notifications = notificationService.getAll();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Получает уведомления текущего авторизованного пользователя.
     *
     * @return список уведомлений пользователя в обратном хронологическом порядке
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyNotifications() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications = notificationService.getByUser(user);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Получает список непрочитанных уведомлений текущего пользователя.
     *
     * @return список непрочитанных уведомлений
     */
    @GetMapping("/my/unread")
    public ResponseEntity<?> getMyUnreadNotifications() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications = notificationService.getUnreadByUser(user);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Возвращает количество непрочитанных уведомлений текущего пользователя.
     *
     * @return количество непрочитанных уведомлений
     */
    @GetMapping("/my/unread/count")
    public ResponseEntity<?> getUnreadCount() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int count = notificationService.getUnreadCount(user);
        return ResponseEntity.ok(count);
    }

    /**
     * Отмечает уведомление как прочитанное.
     *
     * @param id идентификатор уведомления
     * @return обновлённое уведомление или 404, если не найдено
     */
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<?> markAsRead(@PathVariable("id") Long id) {
        Notification updated = notificationService.markAsRead(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Удаляет уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}
