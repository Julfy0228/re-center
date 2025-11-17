package com.recenter.controllers;

import com.recenter.dto.DiscountDto;
import com.recenter.dto.UserDiscountDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/discounts")
public class DiscountsController {

    private final Map<Long, DiscountDto> discounts = new ConcurrentHashMap<>();
    private final Map<Long, UserDiscountDto> userDiscounts = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @GetMapping
    public ResponseEntity<List<DiscountDto>> list() {
        return ResponseEntity.ok(new ArrayList<>(discounts.values()));
    }

    @PostMapping
    public ResponseEntity<DiscountDto> create(@RequestBody DiscountDto dto) {
        long id = idGen.getAndIncrement();
        dto.setId(id);
        discounts.put(id, dto);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/assign")
    public ResponseEntity<UserDiscountDto> assign(@RequestBody UserDiscountDto dto) {
        long id = idGen.getAndIncrement();
        dto.setDiscountId(dto.getDiscountId());
        dto.setUserId(dto.getUserId());
        dto.setAssignedAt(LocalDateTime.now());
        userDiscounts.put(id, dto);
        return ResponseEntity.status(201).body(dto);
    }
}
