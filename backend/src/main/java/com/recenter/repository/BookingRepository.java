package com.recenter.repository;

import com.recenter.model.entity.Booking;
import com.recenter.model.entity.User;
import com.recenter.model.entity.Service;
import org.springframework.data.jpa.repository.EntityGraph;
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
    
    @Override
    @EntityGraph(attributePaths = {"user", "service"})
    List<Booking> findAll();

    @Override
    @EntityGraph(attributePaths = {"user", "service"})
    java.util.Optional<Booking> findById(Long id);

    @EntityGraph(attributePaths = {"user", "service"})
    List<Booking> findByUser(User user);

    @EntityGraph(attributePaths = {"user", "service"})
    List<Booking> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "service"})
    List<Booking> findByService(Service service);

    @EntityGraph(attributePaths = {"user", "service"})
    @Query("""
            select b from Booking b
            where (:userId is null or b.user.id = :userId)
              and (:dateFrom is null or b.endDate >= :dateFrom)
              and (:dateTo is null or b.startDate <= :dateTo)
              and (
                    :paid is null
                    or (:paid = true and exists (select p.id from Payment p where p.booking = b and p.status = 'COMPLETED'))
                    or (:paid = false and not exists (select p.id from Payment p where p.booking = b and p.status = 'COMPLETED'))
                  )
            order by b.createdAt desc
            """)
    List<Booking> findFiltered(
            @Param("userId") Long userId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("paid") Boolean paid);
}
