package com.recenter.controller;

import com.recenter.model.dto.CategoryRequest;
import com.recenter.model.entity.Category;
import com.recenter.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
/**
 * REST контроллер для управления категориями услуг.
 * 
 * Предоставляет API endpoints для создания, чтения, обновления и удаления категорий.
 */
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Создает новую категорию услуг.
     * 
     * @param request DTO с данными категории (название и описание)
     * @return созданная категория
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        Category created = categoryService.create(category);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает категорию по идентификатору.
     * 
     * @param id идентификатор категории
     * @return категория или 404 если не найдена
     */
    /**
     * Получает список всех категорий.
     * 
     * @return список всех категорий
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getById(id);
        return category.isPresent() ? ResponseEntity.ok(category.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping
    /**
     * Обновляет существующую категорию.
     * 
     * @param id идентификатор категории
     * @param request новые данные категории
     * @return обновленная категория или 404 если не найдена
     */
    public ResponseEntity<?> getAll() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(categories);
    /**
     * Удаляет категорию по идентификатору.
     * 
     * @param id идентификатор категории
     * @return сообщение об успешном удалении
     */
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category categoryDetails = new Category();
        categoryDetails.setName(request.getName());
        categoryDetails.setDescription(request.getDescription());
        
        Category updated = categoryService.update(id, categoryDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
