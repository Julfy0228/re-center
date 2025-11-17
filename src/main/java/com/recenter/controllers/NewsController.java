package com.recenter.controllers;

import com.recenter.dto.NewsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final Map<Long, NewsDto> news = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @GetMapping
    public ResponseEntity<List<NewsDto>> list() {
        return ResponseEntity.ok(new ArrayList<>(news.values()));
    }

    @PostMapping
    public ResponseEntity<NewsDto> create(@RequestBody NewsDto dto) {
        long id = idGen.getAndIncrement();
        dto.setId(id);
        dto.setPublicationDate(LocalDateTime.now());
        news.put(id, dto);
        return ResponseEntity.status(201).body(dto);
    }
}

