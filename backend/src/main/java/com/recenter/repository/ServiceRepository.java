package com.recenter.repository;

import com.recenter.model.entity.Service;
import com.recenter.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCategoryId(Long categoryId);
    List<Service> findByCategory(Category category);
    List<Service> findByPriceLessThanEqual(Double maxPrice);
    List<Service> findByTitleContainingIgnoreCase(String title);
    Optional<Service> findByTitle(String title);
}