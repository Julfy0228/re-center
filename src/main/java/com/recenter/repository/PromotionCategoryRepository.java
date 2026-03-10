package com.recenter.repository;

import com.recenter.entity.PromotionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionCategoryRepository extends JpaRepository<PromotionCategory, Integer> {
    List<PromotionCategory> findByPromotionId(Integer promotionId);
    List<PromotionCategory> findByCategoryId(Integer categoryId);
}
