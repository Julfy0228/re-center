package com.recenter.controller;

import com.recenter.model.dto.BookingRequest;
import com.recenter.model.dto.BookingResponse;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Service;
import com.recenter.model.entity.User;
import com.recenter.repository.BookingRepository;
import com.recenter.repository.ServiceRepository;
import com.recenter.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            return ResponseEntity.badRequest().body("Дата выезда не может быть раньше даты заезда");
        }

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));

        boolean isBusy = bookingRepository.isServiceReserved(
                request.getServiceId(), request.getStartDate(), request.getEndDate());
        
        if (isBusy) {
            return ResponseEntity.badRequest().body("Извините, этот домик уже забронирован на выбранные даты");
        }

        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Booking booking = new Booking();
        booking.setService(service);
        booking.setUser(user);

        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setPeopleCount(1);
        booking.setInitialPrice(service.getPrice());
        booking.setStatus("PENDING");

        bookingRepository.save(booking);

        return ResponseEntity.ok("Бронирование успешно создано! Ждем вас " + request.getStartDate().toLocalDate());
    }

    @GetMapping("/my")
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingRepository.findByUserId(user.getId());

        return bookings.stream().map(b -> BookingResponse.builder()
                .id(b.getId())
                .serviceId(b.getService().getId())
                .serviceTitle(b.getService().getTitle())
                .userId(b.getUser().getId())
                .userEmail(b.getUser().getEmail())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .peopleCount(b.getPeopleCount())
                .totalPrice(b.getInitialPrice())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .build()
        ).toList();
    }
}