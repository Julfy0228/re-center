package com.recenter.repository;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Review;
import com.recenter.model.enums.ReviewStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Override
    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Review> findAll();

    @Override
    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    Optional<Review> findById(Long id);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    Optional<Review> findByBooking(Booking booking);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Review> findByStatus(ReviewStatus status);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Review> findByStatusOrderByCreatedAtDesc(ReviewStatus status);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Review> findByBooking_User_IdOrderByCreatedAtDesc(Long userId);
}
