package com.recenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer duration;
    private BigDecimal price;
    private Integer maxPeople;
}
