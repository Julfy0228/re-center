package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.CategoryRequest;
import com.recenter.model.dto.CategoryResponse;
import com.recenter.model.entity.Category;
import com.recenter.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST контроллер для управления категориями услуг.
 * <p>
 * Предоставляет API endpoints для создания, чтения, обновления и удаления категорий.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Создаёт новую категорию услуг.
     *
     * @param request DTO с данными категории (название и описание)
     * @return созданная категория
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category created = categoryService.create(category);
        return ResponseEntity.ok(EntityDtoMapper.toCategoryResponse(created));
    }

    /**
     * Получает категорию по идентификатору.
     *
     * @param id идентификатор категории
     * @return категория или 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable("id") Long id) {
        return categoryService.getById(id)
                .map(category -> ResponseEntity.ok(EntityDtoMapper.toCategoryResponse(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает список всех категорий.
     *
     * @return список всех категорий
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> responses = categoryService.getAll().stream()
                .map(EntityDtoMapper::toCategoryResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequest request) {
        Category categoryDetails = new Category();
        categoryDetails.setName(request.getName());
        categoryDetails.setDescription(request.getDescription());

        Category updated = categoryService.update(id, categoryDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toCategoryResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
