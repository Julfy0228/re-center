package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.dto.PaymentRequest;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import com.recenter.model.enums.BookingStatus;
import com.recenter.service.BookingService;
import com.recenter.service.PaymentService;
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

import java.math.BigDecimal;
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
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentController paymentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Payment payment;
    private Booking booking;
    private com.recenter.model.entity.User currentUser;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        objectMapper.registerModule(new JavaTimeModule());

        currentUser = com.recenter.model.entity.User.builder()
                .id(99L)
                .email("guest@example.com")
                .build();

        booking = Booking.builder()
                .id(1L)
                .user(currentUser)
                .status(BookingStatus.CONFIRMED)
                .build();

        payment = Payment.builder()
                .id(10L)
                .booking(booking)
                .amount(BigDecimal.valueOf(5000))
                .paymentDate(LocalDateTime.now())
                .status("SUCCESS")
                .paymentMethod("CARD")
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
    void create_ReturnsCreatedPayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(1L);
        request.setAmount(BigDecimal.valueOf(5000));
        request.setPaymentMethod("CARD");

        when(bookingService.getById(1L)).thenReturn(Optional.of(booking));
        when(paymentService.getByBooking(booking)).thenReturn(Optional.empty());
        when(paymentService.create(any(Payment.class))).thenReturn(payment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(5000))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void create_WhenPaymentAlreadyExists_ReturnsBadRequest() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(1L);
        request.setAmount(BigDecimal.valueOf(5000));
        request.setPaymentMethod("CARD");

        when(bookingService.getById(1L)).thenReturn(Optional.of(booking));
        when(paymentService.getByBooking(booking)).thenReturn(Optional.of(payment));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Payment for this booking already exists"));
    }

    @Test
    void getMyPayments_ReturnsOnlyCurrentUserPayments() throws Exception {
        when(paymentService.getByBookingUserId(99L)).thenReturn(List.of(payment));

        mockMvc.perform(get("/api/payments/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1));
    }

    @Test
    void getById_Found_ReturnsPayment() throws Exception {
        when(paymentService.getById(10L)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/api/payments/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethod").value("CARD"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(paymentService.getById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/payments/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnsList() throws Exception {
        when(paymentService.getAll()).thenReturn(List.of(payment));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SUCCESS"));
    }

    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(paymentService).delete(10L);

        mockMvc.perform(delete("/api/payments/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment deleted successfully"));

        verify(paymentService).delete(10L);
    }
}
