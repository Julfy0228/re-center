package com.recenter.controller;

import com.recenter.model.entity.Promotion;
import com.recenter.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления промоакциями и скидками на услуги.
 * <p>
 * Предоставляет API endpoints для создания, чтения, обновления и удаления промоакций.
 * Промоакции применяются к конкретным услугам с индивидуальным процентом скидки.
 * Требует роль ADMIN или MANAGER для изменения промоакций.
 */
@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    /**
     * Создаёт новую промоакцию (требует ADMIN или MANAGER).
     *
     * @param promotion объект с данными промоакции
     * @return созданная промоакция
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> create(@RequestBody Promotion promotion) {
        Promotion created = promotionService.create(promotion);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает промоакцию по идентификатору.
     *
     * @param id идентификатор промоакции
     * @return промоакция или 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Promotion> promotion = promotionService.getById(id);
        return promotion.isPresent() ? ResponseEntity.ok(promotion.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех активных промоакций.
     *
     * @return список всех промоакций
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Promotion> promotions = promotionService.getAll();
        return ResponseEntity.ok(promotions);
    }

    /**
     * Обновляет существующую промоакцию (требует ADMIN или MANAGER).
     *
     * @param id идентификатор промоакции
     * @param promotionDetails объект с обновленными данными
     * @return обновленная промоакция или 404, если не найдена
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Promotion promotionDetails) {
        Promotion updated = promotionService.update(id, promotionDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Удаляет промоакцию (требует ADMIN).
     *
     * @param id идентификатор промоакции
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        promotionService.delete(id);
        return ResponseEntity.ok("Promotion deleted successfully");
    }
}