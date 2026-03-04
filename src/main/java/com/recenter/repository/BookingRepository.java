package com.recenter.repository;

import com.recenter.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CREATED'")
    long countPendingBookings();
    
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.status = 'PAID'")
    Double sumConfirmedBookingsRevenue();
}
