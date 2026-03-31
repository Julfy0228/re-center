package com.recenter.controller;

import com.recenter.model.entity.Activity;
import com.recenter.model.entity.User;
import com.recenter.repository.UserRepository;
import com.recenter.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления логом действий пользователей (аудит).
 * <p>
 * Предоставляет API endpoints для просмотра истории действий в системе.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создаёт новую запись о действии.
     *
     * @param activity данные о действии
     * @return созданная запись
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Activity activity) {
        Activity created = activityService.create(activity);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает запись о действии по идентификатору.
     *
     * @param id идентификатор действия
     * @return запись о действии или 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Activity> activity = activityService.getById(id);
        return activity.isPresent() ? ResponseEntity.ok(activity.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех действий.
     *
     * @return список всех записей о действиях
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Activity> activities = activityService.getAll();
        return ResponseEntity.ok(activities);
    }

    /**
     * Получает действия текущего авторизованного пользователя.
     *
     * @return список действий пользователя в обратном хронологическом порядке
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyActivities() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Activity> activities = activityService.getByUser(user);
        return ResponseEntity.ok(activities);
    }

    /**
     * Удаляет запись о действии.
     *
     * @param id идентификатор действия
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        activityService.delete(id);
        return ResponseEntity.ok("Activity deleted successfully");
    }
}
