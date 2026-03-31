package com.recenter.controller;

import com.recenter.model.dto.CategoryRequest;
import com.recenter.model.dto.ServiceRequest;
import com.recenter.model.entity.Category;
import com.recenter.model.entity.Service;
import com.recenter.repository.CategoryRepository;
import com.recenter.repository.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
/**
 * REST контроллер для управления услугами базы отдыха.
 * 
 * Предоставляет API endpoints для создания, чтения и удаления услуг и их категорий.
 * Услуги группируются по категориям и имеют информацию о названии, описании и цене.
 */
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Создает новую категорию услуг (требует ADMIN).
     * 
     * @param request DTO с данными категории
     * @return сообщение об успешном создании
     */
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        categoryRepository.save(category);
        return ResponseEntity.ok("Категория '" + category.getName() + "' успешно создана");
    }

    /**
     * Получает все категории услуг.
     * 
     * @return список всех категорий
     */
    /**
     * Получает все услуги.
     * 
     * @return список всех услуг
     */
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Создает новую услугу (требует ADMIN или MANAGER).
     * 
     * @param request DTO с данными услуги
     * @return сообщение об успешном создании
     */
    @GetMapping
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createService(@Valid @RequestBody ServiceRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Ошибка: Категория с ID " + request.getCategoryId() + " не найдена"));

        Service service = new Service();
        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        
        service.setPrice(request.getPrice());
        
    /**
     * Удаляет услугу (требует ADMIN).
     * 
     * @param id идентификатор услуги
     * @return сообщение об успешном удалении
     */
        service.setCategory(category);

        serviceRepository.save(service);
        return ResponseEntity.ok("Услуга '" + service.getTitle() + "' добавлена в категорию '" + category.getName() + "'");
    /**
     * Получает услуги по категории.
     * 
     * @param categoryId идентификатор категории
     * @return список услуг в категории
     */
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        if (!serviceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        serviceRepository.deleteById(id);
        return ResponseEntity.ok("Услуга удалена");
    }

    @GetMapping("/categories/{categoryId}")
    public List<Service> getServicesByCategory(@PathVariable Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId);
    }
}