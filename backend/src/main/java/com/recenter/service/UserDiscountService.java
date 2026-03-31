package com.recenter.service;

import com.recenter.model.entity.Discount;
import com.recenter.model.entity.User;
import com.recenter.model.entity.UserDiscount;
import com.recenter.repository.UserDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserDiscountService {

    @Autowired
    private UserDiscountRepository userDiscountRepository;

    public UserDiscount create(UserDiscount userDiscount) {
        return userDiscountRepository.save(userDiscount);
    }

    public Optional<UserDiscount> getById(Long id) {
        return userDiscountRepository.findById(id);
    }

    public List<UserDiscount> getAll() {
        return userDiscountRepository.findAll();
    }

    public List<UserDiscount> getByUser(User user) {
        return userDiscountRepository.findByUser(user);
    }

    public List<UserDiscount> getAvailableByUser(User user) {
        return userDiscountRepository.findByUserAndIsUsed(user, false);
    }

    public List<UserDiscount> getUsedByUser(User user) {
        return userDiscountRepository.findByUserAndIsUsed(user, true);
    }

    public List<UserDiscount> getByDiscount(Discount discount) {
        return userDiscountRepository.findByDiscount(discount);
    }

    public Optional<UserDiscount> getByUserAndDiscount(User user, Discount discount) {
        return userDiscountRepository.findByUserAndDiscount(user, discount);
    }

    public UserDiscount markAsUsed(Long id) {
        return userDiscountRepository.findById(id).map(userDiscount -> {
            userDiscount.setUsed(true);
            return userDiscountRepository.save(userDiscount);
        }).orElse(null);
    }

    public UserDiscount update(Long id, UserDiscount userDiscountDetails) {
        return userDiscountRepository.findById(id).map(userDiscount -> {
            if (userDiscountDetails.getExpireDate() != null) {
                userDiscount.setExpireDate(userDiscountDetails.getExpireDate());
            }
            return userDiscountRepository.save(userDiscount);
        }).orElse(null);
    }

    public void delete(Long id) {
        userDiscountRepository.deleteById(id);
    }

    public long count() {
        return userDiscountRepository.count();
    }
}
