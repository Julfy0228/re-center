package com.recenter.repository;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Override
    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Payment> findAll();

    @Override
    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    Optional<Payment> findById(Long id);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    Optional<Payment> findByBooking(Booking booking);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Payment> findByStatus(String status);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Payment> findByPaymentMethod(String method);

    @EntityGraph(attributePaths = {"booking", "booking.user", "booking.service"})
    List<Payment> findByBooking_User_IdOrderByPaymentDateDesc(Long userId);

    boolean existsByBooking_Id(Long bookingId);

    boolean existsByBooking_IdAndStatus(Long bookingId, String status);

    @Query("select distinct p.booking.id from Payment p where p.booking.id in :bookingIds and p.status = 'COMPLETED'")
    List<Long> findPaidBookingIds(@Param("bookingIds") List<Long> bookingIds);
}
