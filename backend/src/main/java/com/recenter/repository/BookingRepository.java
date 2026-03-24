package com.recenter.repository;

import com.recenter.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.service.id = :serviceId " +
        "AND (:start < b.endDate AND :end > b.startDate)")
    boolean isServiceReserved(@Param("serviceId") Long serviceId, 
                            @Param("start") LocalDateTime start, 
                            @Param("end") LocalDateTime end);
}