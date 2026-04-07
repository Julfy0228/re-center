package com.recenter.service;

import com.recenter.model.entity.Discount;
import com.recenter.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public Discount create(Discount discount) {
        return discountRepository.save(discount);
    }

    public Optional<Discount> getById(Long id) {
        return discountRepository.findById(id);
    }

    public List<Discount> getAll() {
        return discountRepository.findAll();
    }

    public List<Discount> getActive() {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByStartDateBeforeAndEndDateAfter(now, now);
    }

    public List<Discount> getByTitle(String title) {
        return discountRepository.findByTitle(title);
    }

    public Discount update(Long id, Discount discountDetails) {
        return discountRepository.findById(id).map(discount -> {
            if (discountDetails.getTitle() != null) {
                discount.setTitle(discountDetails.getTitle());
            }
            if (discountDetails.getDescription() != null) {
                discount.setDescription(discountDetails.getDescription());
            }
            if (discountDetails.getStartDate() != null) {
                discount.setStartDate(discountDetails.getStartDate());
            }
            if (discountDetails.getEndDate() != null) {
                discount.setEndDate(discountDetails.getEndDate());
            }
            if (discountDetails.getType() != null) {
                discount.setType(discountDetails.getType());
            }
            if (discountDetails.getAmount() != null) {
                discount.setAmount(discountDetails.getAmount());
            }
            return discountRepository.save(discount);
        }).orElse(null);
    }

    public void delete(Long id) {
        discountRepository.deleteById(id);
    }

    public long count() {
        return discountRepository.count();
    }
}
