package com.recenter.service;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Review;
import com.recenter.model.enums.ReviewStatus;
import com.recenter.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> getById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getByBooking(Booking booking) {
        return reviewRepository.findByBooking(booking);
    }

    public List<Review> getByStatus(ReviewStatus status) {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Review> getPublished() {
        return getByStatus(ReviewStatus.APPROVED);
    }

    public List<Review> getPending() {
        return getByStatus(ReviewStatus.PENDING);
    }

    public List<Review> getRejected() {
        return getByStatus(ReviewStatus.REJECTED);
    }

    public Review update(Long id, Review reviewDetails) {
        return reviewRepository.findById(id).map(review -> {
            if (reviewDetails.getContent() != null) {
                review.setContent(reviewDetails.getContent());
            }
            if (reviewDetails.getRating() != null) {
                review.setRating(reviewDetails.getRating());
            }
            if (reviewDetails.getStatus() != null) {
                review.setStatus(reviewDetails.getStatus());
            }
            return reviewRepository.save(review);
        }).orElse(null);
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public long count() {
        return reviewRepository.count();
    }
}
