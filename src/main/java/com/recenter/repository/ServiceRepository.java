package com.recenter.repository;

import com.recenter.dto.ServiceDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ServiceRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    public List<ServiceDetailDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM services", new BeanPropertyRowMapper<>(ServiceDetailDto.class));
    }

    public ServiceDetailDto findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM services WHERE id = ?", new BeanPropertyRowMapper<>(ServiceDetailDto.class), id);
        } catch (Exception e) { return null; }
    }

    public void save(ServiceDetailDto service) {
        String sql = "INSERT INTO services (title, description, base_price, service_type, min_capacity, max_capacity) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, service.getTitle(), service.getDescription(), service.getBasePrice(), service.getServiceType(), service.getMinCapacity(), service.getMaxCapacity());
    }
}