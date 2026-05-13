package com.recenter.controller;

import com.recenter.model.dto.BookingRequest;
import com.recenter.model.dto.BookingResponse;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Service;
import com.recenter.model.entity.User;
import com.recenter.model.enums.BookingStatus;
import com.recenter.repository.PaymentRepository;
import com.recenter.service.BookingService;
import com.recenter.service.ServiceService;
import com.recenter.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        if (request.getServiceId() == null || request.getStartDate() == null || request.getEndDate() == null) {
            return ResponseEntity.badRequest().body("serviceId, startDate, endDate are required");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            return ResponseEntity.badRequest().body("End date cannot be earlier than start date");
        }

        Service service = serviceService.getById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        boolean isBusy = bookingService.getAll().stream()
                .filter(b -> b.getService().getId().equals(request.getServiceId()))
                .anyMatch(b -> !(b.getEndDate().isBefore(request.getStartDate())
                        || b.getStartDate().isAfter(request.getEndDate())));

        if (isBusy) {
            return ResponseEntity.badRequest().body("Selected service is already booked for these dates");
        }

        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = new Booking();
        booking.setService(service);
        booking.setUser(user);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setPeopleCount(request.getPeopleCount() != null ? request.getPeopleCount() : 1);
        booking.setInitialPrice(service.getPrice());
        booking.setStatus(BookingStatus.PENDING);

        Booking createdBooking = bookingService.create(booking);
        return ResponseEntity.ok(toResponse(createdBooking));
    }

    @GetMapping("/my")
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(name = "paid", required = false) Boolean paid) {
        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingService.getFiltered(user.getId(), dateFrom, dateTo, paid);
        Set<Long> paidBookingIds = getPaidBookingIds(bookings);

        return bookings.stream()
                .map(booking -> toResponse(booking, paidBookingIds.contains(booking.getId())))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return bookingService.getById(id)
                .map(booking -> ResponseEntity.ok(toResponse(booking)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<BookingResponse> getAll(
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(name = "paid", required = false) Boolean paid) {
        List<Booking> bookings = bookingService.getFiltered(null, dateFrom, dateTo, paid);
        Set<Long> paidBookingIds = getPaidBookingIds(bookings);

        return bookings.stream()
                .map(booking -> toResponse(booking, paidBookingIds.contains(booking.getId())))
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        Booking bookingToUpdate = new Booking();
        if (request.getStartDate() != null) {
            bookingToUpdate.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            bookingToUpdate.setEndDate(request.getEndDate());
        }
        if (request.getPeopleCount() != null) {
            bookingToUpdate.setPeopleCount(request.getPeopleCount());
        }
        if (request.getStatus() != null) {
            bookingToUpdate.setStatus(request.getStatus());
        }

        Booking updated = bookingService.update(id, bookingToUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    private BookingResponse toResponse(Booking booking) {
        return toResponse(booking, paymentRepository.existsByBooking_IdAndStatus(booking.getId(), "COMPLETED"));
    }

    private BookingResponse toResponse(Booking booking, boolean isPaid) {
        return BookingResponse.builder()
                .id(booking.getId())
                .serviceId(booking.getService().getId())
                .serviceTitle(booking.getService().getTitle())
                .userId(booking.getUser().getId())
                .userEmail(booking.getUser().getEmail())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .peopleCount(booking.getPeopleCount())
                .initialPrice(booking.getInitialPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .paid(isPaid)
                .build();
    }

    private Set<Long> getPaidBookingIds(List<Booking> bookings) {
        List<Long> bookingIds = bookings.stream().map(Booking::getId).toList();
        if (bookingIds.isEmpty()) {
            return Set.of();
        }
        return new HashSet<>(paymentRepository.findPaidBookingIds(bookingIds));
    }
}
