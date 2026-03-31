package com.recenter.repository;

import com.recenter.model.entity.Activity;
import com.recenter.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUser(User user);
    List<Activity> findByUserOrderByCreatedAtDesc(User user);
    List<Activity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
