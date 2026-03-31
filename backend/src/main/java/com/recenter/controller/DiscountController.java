package com.recenter.controller;

import com.recenter.model.dto.DiscountRequest;
import com.recenter.model.entity.Discount;
import com.recenter.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления скидками.
 * <p>
 * Предоставляет API endpoints для создания, чтения, обновления и удаления скидок.
 */
@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    /**
     * Создаёт новую скидку.
     *
     * @param request DTO с данными скидки
     * @return созданная скидка
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DiscountRequest request) {
        Discount discount = Discount.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .type(request.getType())
                .amount(request.getAmount())
                .build();

        Discount created = discountService.create(discount);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает скидку по идентификатору.
     *
     * @param id идентификатор скидки
     * @return скидка или 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Discount> discount = discountService.getById(id);
        return discount.isPresent() ? ResponseEntity.ok(discount.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех скидок.
     *
     * @return список всех скидок
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Discount> discounts = discountService.getAll();
        return ResponseEntity.ok(discounts);
    }

    /**
     * Получает список активных скидок (действующих на текущий момент).
     *
     * @return список активных скидок
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActive() {
        List<Discount> discounts = discountService.getActive();
        return ResponseEntity.ok(discounts);
    }

    /**
     * Обновляет существующую скидку.
     *
     * @param id      идентификатор скидки
     * @param request новые данные скидки
     * @return обновлённая скидка или 404, если не найдена
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody DiscountRequest request) {
        Discount discountDetails = Discount.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .type(request.getType())
                .amount(request.getAmount())
                .build();

        Discount updated = discountService.update(id, discountDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Удаляет скидку по идентификатору.
     *
     * @param id идентификатор скидки
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        discountService.delete(id);
        return ResponseEntity.ok("Discount deleted successfully");
    }
}