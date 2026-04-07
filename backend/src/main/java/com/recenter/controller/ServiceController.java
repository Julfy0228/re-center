package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.ServiceRequest;
import com.recenter.model.dto.ServiceResponse;
import com.recenter.model.entity.Category;
import com.recenter.model.entity.Service;
import com.recenter.service.CategoryService;
import com.recenter.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody ServiceRequest request) {
        Category category = categoryService.getById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Service service = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .duration(request.getDuration())
                .price(request.getPrice())
                .maxPeople(request.getMaxPeople())
                .category(category)
                .build();

        Service created = serviceService.create(service);
        return ResponseEntity.ok(EntityDtoMapper.toServiceResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable("id") Long id) {
        return serviceService.getById(id)
                .map(service -> ResponseEntity.ok(EntityDtoMapper.toServiceResponse(service)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAll() {
        List<ServiceResponse> responses = serviceService.getAll().stream()
                .map(EntityDtoMapper::toServiceResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceResponse>> getByCategory(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.getById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<ServiceResponse> responses = serviceService.getByCategory(category).stream()
                .map(EntityDtoMapper::toServiceResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ServiceResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ServiceRequest request) {
        Category category = categoryService.getById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Service serviceDetails = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .duration(request.getDuration())
                .price(request.getPrice())
                .maxPeople(request.getMaxPeople())
                .category(category)
                .build();

        Service updated = serviceService.update(id, serviceDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toServiceResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        serviceService.delete(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
