package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recenter.model.dto.ReviewRequest;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Review;
import com.recenter.model.enums.BookingStatus;
import com.recenter.model.enums.ReviewStatus;
import com.recenter.service.BookingService;
import com.recenter.service.ReviewService;
import com.recenter.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewController reviewController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Review review;
    private Booking booking;
    private com.recenter.model.entity.User currentUser;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        currentUser = com.recenter.model.entity.User.builder()
                .id(7L)
                .email("guest@example.com")
                .build();

        booking = Booking.builder()
                .id(10L)
                .user(currentUser)
                .status(BookingStatus.CONFIRMED)
                .build();

        review = Review.builder()
                .id(1L)
                .booking(booking)
                .content("РћС‚Р»РёС‡РЅРѕ!")
                .rating(5)
                .status(ReviewStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        org.springframework.security.core.userdetails.User
                                .withUsername("guest@example.com")
                                .password("password")
                                .roles("USER")
                                .build(),
                        null
                )
        );
        lenient().when(userService.getByEmail("guest@example.com")).thenReturn(Optional.of(currentUser));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void create_ReturnsCreatedReview() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setBookingId(10L);
        request.setContent("РћС‚Р»РёС‡РЅРѕ!");
        request.setRating(5);

        when(bookingService.getById(10L)).thenReturn(Optional.of(booking));
        when(reviewService.getByBooking(booking)).thenReturn(Optional.empty());
        when(reviewService.create(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void create_WhenBookingNotConfirmed_ReturnsBadRequest() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setBookingId(10L);
        request.setContent("РћС‚Р»РёС‡РЅРѕ!");
        request.setRating(5);

        Booking pendingBooking = booking.toBuilder().status(BookingStatus.PENDING).build();
        when(bookingService.getById(10L)).thenReturn(Optional.of(pendingBooking));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Review is available only for confirmed bookings"));
    }

    @Test
    void create_WhenReviewAlreadyExists_ReturnsBadRequest() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setBookingId(10L);
        request.setContent("РћС‚Р»РёС‡РЅРѕ!");
        request.setRating(5);

        when(bookingService.getById(10L)).thenReturn(Optional.of(booking));
        when(reviewService.getByBooking(booking)).thenReturn(Optional.of(review));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Review for this booking already exists"));
    }

    @Test
    void getById_Found_ReturnsReview() throws Exception {
        when(reviewService.getById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("РћС‚Р»РёС‡РЅРѕ!"));
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
    void getMyReviews_ReturnsList() throws Exception {
        when(reviewService.getByBookingUserId(7L)).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(10));
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
