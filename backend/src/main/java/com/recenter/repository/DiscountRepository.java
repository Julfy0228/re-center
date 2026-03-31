package com.recenter.repository;

import com.recenter.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStartDateBeforeAndEndDateAfter(LocalDateTime start, LocalDateTime end);
    List<Discount> findByTitle(String title);
}
