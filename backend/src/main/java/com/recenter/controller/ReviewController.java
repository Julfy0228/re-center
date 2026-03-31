package com.recenter.controller;

import com.recenter.model.dto.ReviewRequest;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Review;
import com.recenter.model.enums.ReviewStatus;
import com.recenter.repository.BookingRepository;
import com.recenter.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
/**
 * REST контроллер для управления отзывами о услугах.
 * 
 * Предоставляет API endpoints для создания, чтения, обновления и удаления отзывов.
 * Включает процесс модерации отзывов (статусы: PENDING, APPROVED, REJECTED).
 */
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Создает новый отзыв для бронирования.
     * 
     * @param request DTO с данными отзыва
     * @return созданный отзыв
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Review review = Review.builder()
                .booking(booking)
                .content(request.getContent())
                .rating(request.getRating())
                .status(ReviewStatus.PENDING)
                .build();

        Review created = reviewService.create(review);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает отзыв по идентификатору.
     * 
     * @param id идентификатор отзыва
     * @return отзыв или 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getById(id);
        return review.isPresent() ? ResponseEntity.ok(review.get()) : ResponseEntity.notFound().build();
    }

    /**
     * Получает все отзывы.
     * 
     * @return список всех отзывов
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Review> reviews = reviewService.getAll();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Получает одобренные отзывы (статус APPROVED).
     * 
     * @return список отзывов одобренных модератором
     */
    @GetMapping("/published")
    public ResponseEntity<?> getPublished() {
        List<Review> reviews = reviewService.getPublished();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Получает отзывы на модерации (требует ADMIN).
     * 
     * @return список отзывов со статусом PENDING
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * Обновляет отзыв (может только автор отзыва).
     * 
     * @param id идентификатор отзыва
     * @param request новые данные отзыва
     * @return обновленный отзыв или 404
     */
    public ResponseEntity<?> getPending() {
        List<Review> reviews = reviewService.getPending();
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        Review reviewDetails = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
    /**
     * Одобряет отзыв и изменяет статус на APPROVED (требует ADMIN).
     * 
     * @param id идентификатор отзыва
     * @return одобренный отзыв или 404
     */
                .status(request.getStatus())
                .build();

        Review updated = reviewService.update(id, reviewDetails);
    /**
     * Отклоняет отзыв и изменяет статус на REJECTED (требует ADMIN).
     * 
     * @param id идентификатор отзыва
     * @return отклоненный отзыв или 404
     */
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        Review reviewDetails = new Review();
        reviewDetails.setStatus(ReviewStatus.APPROVED);

        Review updated = reviewService.update(id, reviewDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        Review reviewDetails = new Review();
        reviewDetails.setStatus(ReviewStatus.REJECTED);

        Review updated = reviewService.update(id, reviewDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Удаляет отзыв (требует ADMIN).
     * 
     * @param id идентификатор отзыва
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok("Review deleted successfully");
    }
}
