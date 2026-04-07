package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recenter.model.dto.CategoryRequest;
import com.recenter.model.entity.Category;
import com.recenter.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Category category;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        category = Category.builder()
                .id(1L)
                .name("Домики")
                .description("Аренда уютных домиков на берегу")
                .build();
    }

    // -----------------------------
    // POST /api/categories
    // -----------------------------
    @Test
    void create_ReturnsCreatedCategory() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Домики");
        request.setDescription("Аренда уютных домиков на берегу");

        when(categoryService.create(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Домики"))
                .andExpect(jsonPath("$.description").value("Аренда уютных домиков на берегу"));

        verify(categoryService).create(any(Category.class));
    }

    // -----------------------------
    // GET /api/categories/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsCategory() throws Exception {
        when(categoryService.getById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Домики"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(categoryService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/categories
    // -----------------------------
    @Test
    void getAll_ReturnsList() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Домики"));
    }

    // -----------------------------
    // PUT /api/categories/{id}
    // -----------------------------
    @Test
    void update_Found_ReturnsUpdatedCategory() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Бани");
        request.setDescription("Русские и финские бани");

        Category updated = Category.builder()
                .id(1L)
                .name("Бани")
                .description("Русские и финские бани")
                .build();

        when(categoryService.update(eq(1L), any(Category.class))).thenReturn(updated);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Бани"));
    }

    @Test
    void update_NotFound_Returns404() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Бани");
        request.setDescription("Русские и финские бани");

        when(categoryService.update(eq(1L), any(Category.class))).thenReturn(null);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // DELETE /api/categories/{id}
    // -----------------------------
    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully"));

        verify(categoryService).delete(1L);
    }
}
