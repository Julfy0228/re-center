package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.entity.Promotion;
import com.recenter.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    private ObjectMapper objectMapper;

    private Promotion promotion;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders
                .standaloneSetup(promotionController)
                .build();

        promotion = Promotion.builder()
                .id(1L)
                .title("Весенняя акция")
                .description("Скидки до 20%")
                .startDate(LocalDateTime.of(2025, 3, 1, 10, 0))
                .endDate(LocalDateTime.of(2025, 3, 10, 10, 0))
                .build();
    }

    // -----------------------------
    // POST /api/promotions
    // -----------------------------
    @Test
    void create_ReturnsCreatedPromotion() throws Exception {
        when(promotionService.create(any(Promotion.class))).thenReturn(promotion);

        String json = """
        {
          "title": "Весенняя акция",
          "description": "Скидки до 20%",
          "startDate": "2025-03-01T10:00:00",
          "endDate": "2025-03-10T10:00:00"
        }
        """;

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Весенняя акция"));
    }

    // -----------------------------
    // GET /api/promotions/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsPromotion() throws Exception {
        when(promotionService.getById(1L)).thenReturn(Optional.of(promotion));

        mockMvc.perform(get("/api/promotions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Весенняя акция"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(promotionService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/promotions/1"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/promotions
    // -----------------------------
    @Test
    void getAll_ReturnsList() throws Exception {
        when(promotionService.getAll()).thenReturn(List.of(promotion));

        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Весенняя акция"));
    }

    // -----------------------------
    // DELETE /api/promotions/{id}
    // -----------------------------
    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(promotionService).delete(1L);

        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Promotion deleted successfully"));

        verify(promotionService).delete(1L);
    }
}
