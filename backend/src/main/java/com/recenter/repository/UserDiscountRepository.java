package com.recenter.repository;

import com.recenter.model.entity.Discount;
import com.recenter.model.entity.User;
import com.recenter.model.entity.UserDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserDiscountRepository extends JpaRepository<UserDiscount, Long> {
    List<UserDiscount> findByUser(User user);
    List<UserDiscount> findByUserAndIsUsed(User user, boolean isUsed);
    List<UserDiscount> findByDiscount(Discount discount);
    Optional<UserDiscount> findByUserAndDiscount(User user, Discount discount);
}
