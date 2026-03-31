package com.recenter.service;

import com.recenter.model.entity.Activity;
import com.recenter.model.entity.User;
import com.recenter.model.enums.ActivityType;
import com.recenter.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity create(Activity activity) {
        return activityRepository.save(activity);
    }

    public Optional<Activity> getById(Long id) {
        return activityRepository.findById(id);
    }

    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    public List<Activity> getByUser(User user) {
        return activityRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Activity> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return activityRepository.findByCreatedAtBetween(start, end);
    }

    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

    public long count() {
        return activityRepository.count();
    }
}
