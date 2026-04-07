package com.recenter.service;

import com.recenter.model.entity.User;
import com.recenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User update(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            if (userDetails.getFirstName() != null) {
                user.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null) {
                user.setLastName(userDetails.getLastName());
            }
            if (userDetails.getMiddleName() != null) {
                user.setMiddleName(userDetails.getMiddleName());
            }
            if (userDetails.getPhoneNumber() != null) {
                user.setPhoneNumber(userDetails.getPhoneNumber());
            }
            return userRepository.save(user);
        }).orElse(null);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public long count() {
        return userRepository.count();
    }
}
