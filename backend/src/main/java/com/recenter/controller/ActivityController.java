package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.ActivityRequest;
import com.recenter.model.dto.ActivityResponse;
import com.recenter.model.entity.Activity;
import com.recenter.model.entity.User;
import com.recenter.service.ActivityService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    /**
     * Создаёт новую запись о действии.
     *
     * @param activity данные о действии
     * @return созданная запись
     */
    @PostMapping
    public ResponseEntity<ActivityResponse> create(@Valid @RequestBody ActivityRequest request) {
        User user = userService.getById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Activity activity = Activity.builder()
                .user(user)
                .type(request.getType())
                .details(request.getDetails())
                .build();

        Activity created = activityService.create(activity);
        return ResponseEntity.ok(EntityDtoMapper.toActivityResponse(created));
    }

    /**
     * Получает запись о действии по идентификатору.
     *
     * @param id идентификатор действия
     * @return запись о действии или 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getById(@PathVariable("id") Long id) {
        return activityService.getById(id)
                .map(activity -> ResponseEntity.ok(EntityDtoMapper.toActivityResponse(activity)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает список всех действий.
     *
     * @return список всех записей о действиях
     */
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getAll() {
        List<ActivityResponse> responses = activityService.getAll().stream()
                .map(EntityDtoMapper::toActivityResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ActivityResponse>> getMyActivities() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ActivityResponse> responses = activityService.getByUser(user).stream()
                .map(EntityDtoMapper::toActivityResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Удаляет запись о действии.
     *
     * @param id идентификатор действия
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        activityService.delete(id);
        return ResponseEntity.ok("Activity deleted successfully");
    }
}
