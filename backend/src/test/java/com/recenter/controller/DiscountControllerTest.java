package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.dto.DiscountRequest;
import com.recenter.model.entity.Discount;
import com.recenter.model.enums.DiscountType;
import com.recenter.service.DiscountService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DiscountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Discount discount;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(discountController).build();

        objectMapper.registerModule(new JavaTimeModule());

        discount = Discount.builder()
                .id(1L)
                .title("Скидка на аренду домиков")
                .description("10% на домики в будние дни")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(5))
                .type(DiscountType.PERCENT)
                .amount(BigDecimal.valueOf(10))
                .build();
    }

    // -----------------------------
    // POST /api/discounts
    // -----------------------------
    @Test
    void create_ReturnsCreatedDiscount() throws Exception {
        DiscountRequest request = new DiscountRequest();
        request.setTitle("Скидка на аренду домиков");
        request.setDescription("10% на домики в будние дни");
        request.setStartDate(discount.getStartDate());
        request.setEndDate(discount.getEndDate());
        request.setType(DiscountType.PERCENT);
        request.setAmount(BigDecimal.valueOf(10));

        when(discountService.create(any(Discount.class))).thenReturn(discount);

        mockMvc.perform(post("/api/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Скидка на аренду домиков"))
                .andExpect(jsonPath("$.type").value("PERCENT"));

        verify(discountService).create(any(Discount.class));
    }

    // -----------------------------
    // GET /api/discounts/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsDiscount() throws Exception {
        when(discountService.getById(1L)).thenReturn(Optional.of(discount));

        mockMvc.perform(get("/api/discounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Скидка на аренду домиков"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(discountService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/discounts/1"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/discounts
    // -----------------------------
    @Test
    void getAll_ReturnsList() throws Exception {
        when(discountService.getAll()).thenReturn(List.of(discount));

        mockMvc.perform(get("/api/discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Скидка на аренду домиков"));
    }

    // -----------------------------
    // GET /api/discounts/active
    // -----------------------------
    @Test
    void getActive_ReturnsActiveDiscounts() throws Exception {
        when(discountService.getActive()).thenReturn(List.of(discount));

        mockMvc.perform(get("/api/discounts/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("PERCENT"));
    }

    // -----------------------------
    // PUT /api/discounts/{id}
    // -----------------------------
    @Test
    void update_Found_ReturnsUpdatedDiscount() throws Exception {
        DiscountRequest request = new DiscountRequest();
        request.setTitle("Скидка на баню");
        request.setDescription("15% на баню по выходным");
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusDays(3));
        request.setType(DiscountType.PERCENT);
        request.setAmount(BigDecimal.valueOf(15));

        Discount updated = Discount.builder()
                .id(1L)
                .title("Скидка на баню")
                .description("15% на баню по выходным")
                .type(DiscountType.PERCENT)
                .amount(BigDecimal.valueOf(15))
                .build();

        when(discountService.update(eq(1L), any(Discount.class))).thenReturn(updated);

        mockMvc.perform(put("/api/discounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Скидка на баню"));
    }

    @Test
    void update_NotFound_Returns404() throws Exception {
        DiscountRequest request = new DiscountRequest();
        request.setTitle("Скидка на баню");
        request.setDescription("15% на баню по выходным");
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusDays(3));
        request.setType(DiscountType.PERCENT);
        request.setAmount(BigDecimal.valueOf(15));

        when(discountService.update(eq(1L), any(Discount.class))).thenReturn(null);

        mockMvc.perform(put("/api/discounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // DELETE /api/discounts/{id}
    // -----------------------------
    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(discountService).delete(1L);

        mockMvc.perform(delete("/api/discounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Discount deleted successfully"));

        verify(discountService).delete(1L);
    }
}
