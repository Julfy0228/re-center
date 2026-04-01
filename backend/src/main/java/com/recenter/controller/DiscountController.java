package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.DiscountRequest;
import com.recenter.model.dto.DiscountResponse;
import com.recenter.model.entity.Discount;
import com.recenter.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountResponse> create(@Valid @RequestBody DiscountRequest request) {
        Discount discount = Discount.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .type(request.getType())
                .amount(request.getAmount())
                .build();

        Discount created = discountService.create(discount);
        return ResponseEntity.ok(EntityDtoMapper.toDiscountResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponse> getById(@PathVariable("id") Long id) {
        return discountService.getById(id)
                .map(discount -> ResponseEntity.ok(EntityDtoMapper.toDiscountResponse(discount)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DiscountResponse>> getAll() {
        List<DiscountResponse> responses = discountService.getAll().stream()
                .map(EntityDtoMapper::toDiscountResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DiscountResponse>> getActive() {
        List<DiscountResponse> responses = discountService.getActive().stream()
                .map(EntityDtoMapper::toDiscountResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountResponse> update(@PathVariable("id") Long id, @Valid @RequestBody DiscountRequest request) {
        Discount discountDetails = Discount.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .type(request.getType())
                .amount(request.getAmount())
                .build();

        Discount updated = discountService.update(id, discountDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toDiscountResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        discountService.delete(id);
        return ResponseEntity.ok("Discount deleted successfully");
    }
}
