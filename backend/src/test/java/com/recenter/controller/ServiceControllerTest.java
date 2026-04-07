package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recenter.model.dto.ServiceRequest;
import com.recenter.model.entity.Category;
import com.recenter.model.entity.Service;
import com.recenter.service.CategoryService;
import com.recenter.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceService serviceService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ServiceController serviceController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Category category;
    private Service service;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();

        category = Category.builder()
                .id(1L)
                .name("Домики")
                .description("Отдых на природе")
                .build();

        service = Service.builder()
                .id(5L)
                .title("Аренда домика")
                .description("Уютный домик у озера")
                .price(BigDecimal.valueOf(3000))
                .duration(3)
                .maxPeople(6)
                .category(category)
                .build();
    }

    // -----------------------------
    // POST /api/services
    // -----------------------------
    @Test
    void create_ReturnsCreatedService() throws Exception {
        ServiceRequest request = new ServiceRequest();
        request.setTitle("Аренда домика");
        request.setDescription("Уютный домик у озера");
        request.setPrice(BigDecimal.valueOf(3000));
        request.setDuration(3);
        request.setMaxPeople(6);
        request.setCategoryId(1L);

        when(categoryService.getById(1L)).thenReturn(Optional.of(category));
        when(serviceService.create(any(Service.class))).thenReturn(service);

        mockMvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Аренда домика"))
                .andExpect(jsonPath("$.price").value(3000))
                .andExpect(jsonPath("$.maxPeople").value(6));

        verify(serviceService).create(any(Service.class));
    }

    // -----------------------------
    // GET /api/services/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsService() throws Exception {
        when(serviceService.getById(5L)).thenReturn(Optional.of(service));

        mockMvc.perform(get("/api/services/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Аренда домика"))
                .andExpect(jsonPath("$.duration").value(3))
                .andExpect(jsonPath("$.categoryId").value(1));
    }
    // -----------------------------
    // GET /api/services/{id}
    // -----------------------------
    void getById_NotFound_Returns404() throws Exception {
        when(serviceService.getById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/services/5"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/services
    // -----------------------------
    @Test
    void getAll_ReturnsList() throws Exception {
        when(serviceService.getAll()).thenReturn(List.of(service));

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Аренда домика"));
    }

    // -----------------------------
    // GET /api/services/category/{categoryId}
    // -----------------------------
    @Test
    void getByCategory_ReturnsServices() throws Exception {
        when(categoryService.getById(1L)).thenReturn(Optional.of(category));
        when(serviceService.getByCategory(category)).thenReturn(List.of(service));

        mockMvc.perform(get("/api/services/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Аренда домика"));
    }

    // -----------------------------
    // PUT /api/services/{id}
    // -----------------------------
    @Test
    void update_Found_ReturnsUpdatedService() throws Exception {
        ServiceRequest request = new ServiceRequest();
        request.setTitle("Аренда домика люкс");
        request.setDescription("Премиум домик у озера");
        request.setPrice(BigDecimal.valueOf(5000));
        request.setDuration(3);
        request.setMaxPeople(8);
        request.setCategoryId(1L);

        Service updated = Service.builder()
                .id(5L)
                .title("Аренда домика люкс")
                .description("Премиум домик у озера")
                .price(BigDecimal.valueOf(5000))
                .duration(3)
                .maxPeople(8)
                .category(category)
                .build();

        when(categoryService.getById(1L)).thenReturn(Optional.of(category));
        when(serviceService.update(eq(5L), any(Service.class))).thenReturn(updated);

        mockMvc.perform(put("/api/services/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Аренда домика люкс"))
                .andExpect(jsonPath("$.price").value(5000))
                .andExpect(jsonPath("$.maxPeople").value(8));
    }

    @Test
    void update_NotFound_Returns404() throws Exception {
        ServiceRequest request = new ServiceRequest();
        request.setTitle("Аренда домика люкс");
        request.setDescription("Премиум домик у озера");
        request.setPrice(BigDecimal.valueOf(5000));
        request.setDuration(3);
        request.setMaxPeople(8);
        request.setCategoryId(1L);

        when(categoryService.getById(1L)).thenReturn(Optional.of(category));
        when(serviceService.update(eq(5L), any(Service.class))).thenReturn(null);

        mockMvc.perform(put("/api/services/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // DELETE /api/services/{id}
    // -----------------------------
    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(serviceService).delete(5L);

        mockMvc.perform(delete("/api/services/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service deleted successfully"));

        verify(serviceService).delete(5L);
    }
}
