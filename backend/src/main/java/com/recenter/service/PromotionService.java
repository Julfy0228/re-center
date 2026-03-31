package com.recenter.service;

import com.recenter.model.entity.Promotion;
import com.recenter.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public Promotion create(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public Optional<Promotion> getById(Long id) {
        return promotionRepository.findById(id);
    }

    public List<Promotion> getAll() {
        return promotionRepository.findAll();
    }

    public Promotion update(Long id, Promotion promotionDetails) {
        return promotionRepository.findById(id).map(promotion -> {
            if (promotionDetails.getTitle() != null) {
                promotion.setTitle(promotionDetails.getTitle());
            }
            if (promotionDetails.getDescription() != null) {
                promotion.setDescription(promotionDetails.getDescription());
            }
            if (promotionDetails.getStartDate() != null) {
                promotion.setStartDate(promotionDetails.getStartDate());
            }
            if (promotionDetails.getEndDate() != null) {
                promotion.setEndDate(promotionDetails.getEndDate());
            }
            return promotionRepository.save(promotion);
        }).orElse(null);
    }

    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }

    public long count() {
        return promotionRepository.count();
    }
}
