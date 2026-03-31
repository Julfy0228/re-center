package com.recenter.controller;

import com.recenter.model.dto.BookingRequest;
import com.recenter.model.dto.BookingResponse;
import com.recenter.model.entity.Booking;
import com.recenter.model.entity.Service;
import com.recenter.model.entity.User;
import com.recenter.model.enums.BookingStatus;
import com.recenter.service.BookingService;
import com.recenter.service.ServiceService;
import com.recenter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления бронированиями в системе базы отдыха.
 * <p>
 * Предоставляет API для создания новых бронирований и просмотра истории бронирований
 * текущего авторизованного пользователя.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    /**
     * Создаёт новое бронирование для текущего пользователя.
     * <p>
     * Перед созданием проверяется:
     * <ul>
     *     <li>Корректность дат (выезд не раньше заезда)</li>
     *     <li>Существование выбранной услуги (домика)</li>
     *     <li>Свободен ли домик на указанные даты (нет пересекающихся бронирований)</li>
     * </ul>
     * Бронирование сохраняется со статусом {@code PENDING} и количеством гостей по умолчанию 1.
     *
     * @param request объект с данными бронирования (идентификатор услуги, даты заезда и выезда)
     * @return сообщение об успешном создании бронирования или ошибка с описанием проблемы
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {

        if (request.getServiceId() == null || request.getStartDate() == null || request.getEndDate() == null) {
            return ResponseEntity.badRequest().body("serviceId, startDate, и endDate обязательны");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            return ResponseEntity.badRequest().body("Дата выезда не может быть раньше даты заезда");
        }

        Service service = serviceService.getById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));

        boolean isBusy = bookingService.getAll().stream()
                .filter(b -> b.getService().getId().equals(request.getServiceId()))
                .anyMatch(b -> !(b.getEndDate().isBefore(request.getStartDate()) || 
                                b.getStartDate().isAfter(request.getEndDate())));

        if (isBusy) {
            return ResponseEntity.badRequest().body("Извините, этот домик уже забронирован на выбранные даты");
        }

        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Booking booking = new Booking();
        booking.setService(service);
        booking.setUser(user);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setPeopleCount(request.getPeopleCount() != null ? request.getPeopleCount() : 1);
        booking.setInitialPrice(service.getPrice());
        booking.setStatus(BookingStatus.PENDING);

        Booking createdBooking = bookingService.create(booking);
        return ResponseEntity.ok(toResponse(createdBooking));
    }

    /**
     * Возвращает список бронирований текущего авторизованного пользователя.
     * <p>
     * Метод использует транзакцию только для чтения. Бронирования возвращаются в том порядке,
     * в котором они сохранены в базе (обычно по убыванию даты создания, если репозиторий настроен).
     *
     * @return список объектов {@link BookingResponse} с детальной информацией о каждом бронировании
     */
    @GetMapping("/my")
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {

        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        User user = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingService.getByUser(user);

        return bookings.stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .serviceId(b.getService().getId())
                .serviceTitle(b.getService().getTitle())
                .userId(b.getUser().getId())
                .userEmail(b.getUser().getEmail())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .peopleCount(b.getPeopleCount())
                .initialPrice(b.getInitialPrice())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .build();
    }

    /**
     * Получает бронирование по идентификатору.
     *
     * @param id идентификатор бронирования
     * @return объект бронирования или 404, если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return bookingService.getById(id)
                .map(booking -> ResponseEntity.ok(toResponse(booking)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Возвращает список всех бронирований.
     *
     * @return список всех бронирований
     */
    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.getAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Обновляет существующее бронирование.
     *
     * @param id идентификатор бронирования
     * @param request данные для обновления
     * @return обновленное бронирование или 404, если не найдено
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        Booking bookingToUpdate = new Booking();
        if (request.getStartDate() != null) {
            bookingToUpdate.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            bookingToUpdate.setEndDate(request.getEndDate());
        }
        if (request.getPeopleCount() != null) {
            bookingToUpdate.setPeopleCount(request.getPeopleCount());
        }
        if (request.getStatus() != null) {
            bookingToUpdate.setStatus(request.getStatus());
        }

        Booking updated = bookingService.update(id, bookingToUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponse(updated));
    }

    /**
     * Удаляет бронирование по идентификатору.
     *
     * @param id идентификатор бронирования
     * @return сообщение об успешном удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
