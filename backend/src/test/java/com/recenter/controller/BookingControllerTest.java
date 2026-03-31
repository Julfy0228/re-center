package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.dto.BookingRequest;
import com.recenter.model.entity.*;
import com.recenter.model.enums.BookingStatus;
import com.recenter.model.enums.UserRole;
import com.recenter.repository.UserRepository;
import com.recenter.service.BookingService;
import com.recenter.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private com.recenter.model.entity.Service service;
    private Booking booking;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        objectMapper.registerModule(new JavaTimeModule());

        user = User.builder()
                .id(10L)
                .email("user@example.com")
                .role(UserRole.CLIENT)
                .build();

        service = com.recenter.model.entity.Service.builder()
                .id(5L)
                .title("Аренда домика")
                .price(BigDecimal.valueOf(3000))
                .duration(3)
                .maxPeople(6)
                .build();

        booking = Booking.builder()
                .id(1L)
                .user(user)
                .service(service)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .peopleCount(4)
                .initialPrice(BigDecimal.valueOf(3000))
                .status(BookingStatus.PENDING)
                .build();
    }

    private void authenticateUser() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("pass")
                .roles("CLIENT")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        lenient().when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
    }

    // -----------------------------
    // POST /api/bookings
    // -----------------------------
    @Test
    void create_ReturnsCreatedBooking() throws Exception {
        authenticateUser();

        BookingRequest request = new BookingRequest();
        request.setServiceId(5L);
        request.setStartDate(booking.getStartDate());
        request.setEndDate(booking.getEndDate());
        request.setPeopleCount(4);

        when(serviceService.getById(5L)).thenReturn(Optional.of(service));
        when(bookingService.create(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peopleCount").value(4))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // -----------------------------
    // GET /api/bookings/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsBooking() throws Exception {
        when(bookingService.getById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.initialPrice").value(3000));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(bookingService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/bookings
    // -----------------------------
    @Test
    void getAll_ReturnsList() throws Exception {
        when(bookingService.getAll()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].peopleCount").value(4));
    }

    // -----------------------------
    // GET /api/bookings/my
    // -----------------------------
    @Test
    void getMyBookings_ReturnsUserBookings() throws Exception {
        authenticateUser();

        when(bookingService.getByUser(user)).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    // -----------------------------
    // PUT /api/bookings/{id}
    // -----------------------------
    @Test
    void update_Found_ReturnsUpdatedBooking() throws Exception {
        Booking updated = booking.toBuilder()
                .peopleCount(5)
                .status(BookingStatus.CONFIRMED)
                .build();

        BookingRequest request = new BookingRequest();
        request.setPeopleCount(5);
        request.setStatus(BookingStatus.CONFIRMED);

        when(bookingService.update(eq(1L), any(Booking.class))).thenReturn(updated);

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peopleCount").value(5))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void update_NotFound_Returns404() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setPeopleCount(5);

        when(bookingService.update(eq(1L), any(Booking.class))).thenReturn(null);

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // DELETE /api/bookings/{id}
    // -----------------------------
    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(bookingService).delete(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking deleted successfully"));

        verify(bookingService).delete(1L);
    }
}
