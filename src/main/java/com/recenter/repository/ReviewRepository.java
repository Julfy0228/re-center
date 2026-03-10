package com.recenter.repository;

import com.recenter.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByServiceId(Integer serviceId);
    List<Review> findByUserId(Integer userId);
}
