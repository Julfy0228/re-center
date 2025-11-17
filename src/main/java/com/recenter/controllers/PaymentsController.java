package com.recenter.controllers;

import com.recenter.dto.PaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final Map<Long, PaymentDto> payments = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody PaymentDto dto) {
        long id = idGen.getAndIncrement();
        dto.setId(id);
        payments.put(id, dto);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> get(@PathVariable("id") Long id) {
        PaymentDto p = payments.get(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }
}
