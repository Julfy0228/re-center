package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.PromotionRequest;
import com.recenter.model.dto.PromotionResponse;
import com.recenter.model.entity.Promotion;
import com.recenter.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<PromotionResponse> create(@Valid @RequestBody PromotionRequest request) {
        Promotion promotion = Promotion.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Promotion created = promotionService.create(promotion);
        return ResponseEntity.ok(EntityDtoMapper.toPromotionResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getById(@PathVariable("id") Long id) {
        return promotionService.getById(id)
                .map(promotion -> ResponseEntity.ok(EntityDtoMapper.toPromotionResponse(promotion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAll() {
        List<PromotionResponse> responses = promotionService.getAll().stream()
                .map(EntityDtoMapper::toPromotionResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<PromotionResponse> update(@PathVariable("id") Long id, @Valid @RequestBody PromotionRequest request) {
        Promotion promotionDetails = Promotion.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Promotion updated = promotionService.update(id, promotionDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toPromotionResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        promotionService.delete(id);
        return ResponseEntity.ok("Promotion deleted successfully");
    }
}
