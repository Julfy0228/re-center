package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.ReviewRequest;
import com.recenter.model.dto.ReviewResponse;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Review;
import com.recenter.model.entity.User;
import com.recenter.model.enums.BookingStatus;
import com.recenter.model.enums.ReviewStatus;
import com.recenter.service.BookingService;
import com.recenter.service.ReviewService;
import com.recenter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReviewRequest request) {
        Booking booking = bookingService.getById(request.getBookingId()).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = getCurrentUser();
        if (!booking.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("You can only leave a review for your own booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            return ResponseEntity.badRequest().body("Review is available only for confirmed bookings");
        }

        if (reviewService.getByBooking(booking).isPresent()) {
            return ResponseEntity.badRequest().body("Review for this booking already exists");
        }

        Review review = Review.builder()
                .booking(booking)
                .content(request.getContent())
                .rating(request.getRating())
                .status(ReviewStatus.PENDING)
                .build();

        Review created = reviewService.create(review);
        return ResponseEntity.ok(EntityDtoMapper.toReviewResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable("id") Long id) {
        return reviewService.getById(id)
                .map(review -> ResponseEntity.ok(EntityDtoMapper.toReviewResponse(review)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ReviewResponse>> getAll() {
        List<ReviewResponse> responses = reviewService.getAll().stream()
                .map(EntityDtoMapper::toReviewResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        User currentUser = getCurrentUser();
        List<ReviewResponse> responses = reviewService.getByBookingUserId(currentUser.getId()).stream()
                .map(EntityDtoMapper::toReviewResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/published")
    public ResponseEntity<List<ReviewResponse>> getPublished() {
        List<ReviewResponse> responses = reviewService.getPublished().stream()
                .map(EntityDtoMapper::toReviewResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewResponse>> getPending() {
        List<ReviewResponse> responses = reviewService.getPending().stream()
                .map(EntityDtoMapper::toReviewResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ReviewResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ReviewRequest request) {
        Review reviewDetails = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .status(request.getStatus())
                .build();

        Review updated = reviewService.update(id, reviewDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toReviewResponse(updated));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ReviewResponse> approve(@PathVariable("id") Long id) {
        Review reviewDetails = new Review();
        reviewDetails.setStatus(ReviewStatus.APPROVED);

        Review updated = reviewService.update(id, reviewDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toReviewResponse(updated));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ReviewResponse> reject(@PathVariable("id") Long id) {
        Review reviewDetails = new Review();
        reviewDetails.setStatus(ReviewStatus.REJECTED);

        Review updated = reviewService.update(id, reviewDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toReviewResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok("Review deleted successfully");
    }

    private User getCurrentUser() {
        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

        return userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
