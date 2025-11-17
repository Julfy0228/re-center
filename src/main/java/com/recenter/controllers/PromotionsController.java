package com.recenter.controllers;

import com.recenter.dto.PromotionDto;
import com.recenter.dto.PromotionDetailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/promotions")
public class PromotionsController {

    private final Map<Long, PromotionDto> promotions = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @GetMapping
    public ResponseEntity<List<PromotionDto>> list() {
        return ResponseEntity.ok(new ArrayList<>(promotions.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDetailDto> detail(@PathVariable("id") Long id) {
        PromotionDto p = promotions.get(id);
        if (p == null) return ResponseEntity.notFound().build();
        PromotionDetailDto d = new PromotionDetailDto();
        d.setPromotion(p);
        d.setApplicableServices(Collections.emptyList());
        d.setApplicableCategories(Collections.emptyList());
        return ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<PromotionDto> create(@RequestBody PromotionDto dto) {
        long id = idGen.getAndIncrement();
        dto.setId(id);
        promotions.put(id, dto);
        return ResponseEntity.status(201).body(dto);
    }
}
