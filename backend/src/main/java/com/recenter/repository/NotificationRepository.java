package com.recenter.repository;

import com.recenter.model.entity.Notification;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndRead(User user, boolean read);
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    List<Notification> findByType(NotificationType type);
}
