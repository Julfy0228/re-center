package com.recenter.model.enums;

/**
 * Типы действий для аудита.
 */
public enum ActivityType {
    LOGIN,                // вход в систему
    LOGOUT,               // выход из системы
    VIEW_PAGE,            // просмотр страницы
    UPDATE_PROFILE,       // обновление профиля
    BOOK_SERVICE,         // бронирование услуги
    CANCEL_BOOKING,       // отмена бронирования
    APPLY_DISCOUNT        // применение скидки
}