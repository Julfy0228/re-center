package com.recenter.repository;

import com.recenter.model.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}