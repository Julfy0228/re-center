package com.recenter.service;

import com.recenter.model.entity.Category;
import com.recenter.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category update(Long id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            if (categoryDetails.getName() != null) {
                category.setName(categoryDetails.getName());
            }
            if (categoryDetails.getDescription() != null) {
                category.setDescription(categoryDetails.getDescription());
            }
            return categoryRepository.save(category);
        }).orElse(null);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public long count() {
        return categoryRepository.count();
    }
}
