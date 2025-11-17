package com.recenter.controllers;

import com.recenter.dto.ServiceSummaryDto;
import com.recenter.dto.ServiceDetailDto;
import com.recenter.dto.ServicePhotoDto;
import com.recenter.dto.ServicePriceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/services")
public class ServicesController {

    private final Map<Long, ServiceSummaryDto> summaries = new ConcurrentHashMap<>();
    private final Map<Long, ServiceDetailDto> details = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @GetMapping
    public ResponseEntity<List<ServiceSummaryDto>> list() {
        return ResponseEntity.ok(new ArrayList<>(summaries.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDetailDto> get(@PathVariable("id") Long id) {
        ServiceDetailDto d = details.get(id);
        if (d == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<ServiceDetailDto> create(@RequestBody ServiceDetailDto dto) {
        long id = idGen.getAndIncrement();
        dto.setId(id);
        if (dto.getPhotos() == null) dto.setPhotos(Collections.emptyList());
        if (dto.getPriceList() == null) dto.setPriceList(Collections.emptyList());
        if (dto.getReviews() == null) dto.setReviews(Collections.emptyList());

        ServiceSummaryDto s = new ServiceSummaryDto();
        s.setId(id);
        s.setTitle(dto.getTitle());
        s.setDescription(dto.getDescription());
        s.setServiceType(dto.getServiceType());
        s.setBasePrice(dto.getBasePrice());
        s.setActive(dto.isActive());
        s.setDeleted(dto.isDeleted());
        s.setCategory(null);

        summaries.put(id, s);
        details.put(id, dto);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/seed")
    public ResponseEntity<String> seed() {
        long id = idGen.getAndIncrement();
        ServiceDetailDto d = new ServiceDetailDto();
        d.setId(id);
        d.setTitle("Demo service");
        d.setDescription("Demo description");
        d.setServiceType("DAILY");
        d.setMinCapacity(1);
        d.setMaxCapacity(4);
        d.setBasePrice(new BigDecimal("1000"));
        d.setActive(true);
        d.setDeleted(false);
        d.setDurationMinutes(60);
        d.setPhotos(Arrays.asList(new ServicePhotoDto()));
        d.setPriceList(Arrays.asList(new ServicePriceDto()));
        details.put(id, d);
        ServiceSummaryDto s = new ServiceSummaryDto();
        s.setId(id);
        s.setTitle(d.getTitle());
        s.setDescription(d.getDescription());
        s.setServiceType(d.getServiceType());
        s.setBasePrice(d.getBasePrice());
        s.setActive(d.isActive());
        s.setDeleted(d.isDeleted());
        summaries.put(id, s);
        return ResponseEntity.ok("seeded");
    }
}

