package com.recenter.service;

import com.recenter.model.entity.*;
import com.recenter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> getByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getByService(com.recenter.model.entity.Service service) {
        return bookingRepository.findByService(service);
    }

    public Booking update(Long id, Booking bookingDetails) {
        return bookingRepository.findById(id).map(booking -> {
            if (bookingDetails.getStartDate() != null) {
                booking.setStartDate(bookingDetails.getStartDate());
            }
            if (bookingDetails.getEndDate() != null) {
                booking.setEndDate(bookingDetails.getEndDate());
            }
            if (bookingDetails.getPeopleCount() != null) {
                booking.setPeopleCount(bookingDetails.getPeopleCount());
            }
            if (bookingDetails.getInitialPrice() != null) {
                booking.setInitialPrice(bookingDetails.getInitialPrice());
            }
            if (bookingDetails.getStatus() != null) {
                booking.setStatus(bookingDetails.getStatus());
            }
            return bookingRepository.save(booking);
        }).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    public long count() {
        return bookingRepository.count();
    }
}
