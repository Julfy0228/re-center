package com.recenter.controllers;

import com.recenter.dto.BookingRequestDto;
import com.recenter.dto.BookingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/bookings")
public class BookingsController {

    private final Map<Long, BookingResponseDto> bookings = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@RequestBody BookingRequestDto req) {
        long id = idGen.getAndIncrement();
        BookingResponseDto r = new BookingResponseDto();
        r.setId(id);
        r.setUserId(null);
        if (req.getServiceId() != null) r.setServiceId(req.getServiceId());
        r.setStartDate(req.getStartDate());
        r.setEndDate(req.getEndDate());
        r.setNumberOfGuests(req.getNumberOfGuests());
        r.setTotalPrice(BigDecimal.ZERO);
        r.setStatus("PENDING");
        r.setBookingDate(LocalDateTime.now());
        bookings.put(id, r);
        return ResponseEntity.status(201).body(r);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> get(@PathVariable("id") Long id) {
        BookingResponseDto r = bookings.get(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }
}

