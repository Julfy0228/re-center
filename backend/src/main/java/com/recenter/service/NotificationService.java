package com.recenter.service;

import com.recenter.model.entity.Notification;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NotificationType;
import com.recenter.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public List<Notification> getByUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadByUser(User user) {
        return notificationRepository.findByUserAndRead(user, false);
    }

    public List<Notification> getByType(NotificationType type) {
        return notificationRepository.findByType(type);
    }

    public int getUnreadCount(User user) {
        return getUnreadByUser(user).size();
    }

    public Notification markAsRead(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setRead(true);
            return notificationRepository.save(notification);
        }).orElse(null);
    }

    public Notification update(Long id, Notification notificationDetails) {
        return notificationRepository.findById(id).map(notification -> {
            if (notificationDetails.getTitle() != null) {
                notification.setTitle(notificationDetails.getTitle());
            }
            if (notificationDetails.getMessage() != null) {
                notification.setMessage(notificationDetails.getMessage());
            }
            if (notificationDetails.getType() != null) {
                notification.setType(notificationDetails.getType());
            }
            return notificationRepository.save(notification);
        }).orElse(null);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    public long count() {
        return notificationRepository.count();
    }
}
