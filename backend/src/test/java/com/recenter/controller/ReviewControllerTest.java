package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recenter.model.dto.ReviewRequest;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Review;
import com.recenter.model.enums.ReviewStatus;
import com.recenter.service.BookingService;
import com.recenter.service.ReviewService;
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
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Review review;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        review = Review.builder()
                .id(1L)
                .booking(Booking.builder().id(10L).build())
                .content("Отлично!")
                .rating(5)
                .status(ReviewStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void create_ReturnsCreatedReview() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setBookingId(10L);
        request.setContent("Отлично!");
        request.setRating(5);

        when(bookingService.getById(10L)).thenReturn(Optional.of(Booking.builder().id(10L).build()));
        when(reviewService.create(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void getById_Found_ReturnsReview() throws Exception {
        when(reviewService.getById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Отлично!"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(reviewService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnsList() throws Exception {
        when(reviewService.getAll()).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(reviewService).delete(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review deleted successfully"));

        verify(reviewService).delete(1L);
    }
}
