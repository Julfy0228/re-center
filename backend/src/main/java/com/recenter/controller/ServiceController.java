package com.recenter.controller;

import com.recenter.model.dto.ServiceRequest;
import com.recenter.model.entity.Category;
import com.recenter.model.entity.Service;
import com.recenter.service.CategoryService;
import com.recenter.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления услугами базы отдыха.
 * <p>
 * Предоставляет API endpoints для создания, чтения, обновления и удаления услуг.
 * Услуги группируются по категориям и содержат информацию о названии, описании, цене и максимальном количестве участников.
 */
@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Создаёт новую услугу (требует ADMIN или MANAGER).
     *
     * @param request DTO с данными услуги (название, описание, цена, категория, длительность, макс. люди)
     * @return созданная услуга
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody ServiceRequest request) {
        Category category = categoryService.getById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Service service = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .price(request.getPrice())
                .maxPeople(request.getMaxPeople())
                .category(category)
                .build();

        Service created = serviceService.create(service);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает услугу по идентификатору.
     *
     * @param id идентификатор услуги
     * @return услуга или 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Service> service = serviceService.getById(id);
        return service.isPresent() ? ResponseEntity.ok(service.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает список всех услуг.
     *
     * @return список всех услуг
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Service> services = serviceService.getAll();
        return ResponseEntity.ok(services);
    }

    /**
     * Получает список услуг по идентификатору категории.
     *
     * @param categoryId идентификатор категории
     * @return список услуг в указанной категории
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getByCategory(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.getById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Service> services = serviceService.getByCategory(category);
        return ResponseEntity.ok(services);
    }

    /**
     * Обновляет существующую услугу (требует ADMIN или MANAGER).
     *
     * @param id идентификатор услуги
     * @param request DTO с обновленными данными
     * @return обновленная услуга или 404, если не найдена
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody ServiceRequest request) {
        Category category = categoryService.getById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Service serviceDetails = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .price(request.getPrice())
                .maxPeople(request.getMaxPeople())
                .category(category)
                .build();

        Service updated = serviceService.update(id, serviceDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Удаляет услугу (требует ADMIN или MANAGER).
     *
     * @param id идентификатор услуги
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        serviceService.delete(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
