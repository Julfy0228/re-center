package com.recenter.repository;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.User;
import com.recenter.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.service.id = :serviceId AND b.startDate < :end AND b.endDate > :start")
    boolean isServiceReserved(@Param("serviceId") Long serviceId, 
                            @Param("start") LocalDateTime start, 
                            @Param("end") LocalDateTime end);
    
    List<Booking> findByUser(User user);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByService(Service service);
}