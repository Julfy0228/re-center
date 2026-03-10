package com.recenter.repository;

import com.recenter.entity.UserDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDiscountRepository extends JpaRepository<UserDiscount, Integer> {
    List<UserDiscount> findByUserId(Integer userId);
    List<UserDiscount> findByDiscountId(Integer discountId);
}
