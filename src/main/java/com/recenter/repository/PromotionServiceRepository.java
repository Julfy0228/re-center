package com.recenter.repository;

import com.recenter.entity.PromotionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionServiceRepository extends JpaRepository<PromotionService, Integer> {
    List<PromotionService> findByPromotionId(Integer promotionId);
    List<PromotionService> findByServiceId(Integer serviceId);
}
