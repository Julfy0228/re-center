import { formatApiDateTime } from "../../utils/date";
import BookingPaymentForm from "./BookingPaymentForm";
import BookingReviewForm from "./BookingReviewForm";

function formatPrice(value) {
  return new Intl.NumberFormat("ru-RU").format(value || 0);
}

function formatBookingStatus(status) {
  if (status === "CONFIRMED") {
    return "Подтверждено";
  }

  if (status === "CANCELLED") {
    return "Отменено";
  }

  return "Ожидает подтверждения";
}

export default function BookingCard({ booking }) {
  return (
    <article className="booking-card booking-card-extended">
      <div className="booking-card-main">
        <p className="booking-label">Бронь #{booking.id}</p>
        <h3>{booking.serviceTitle}</h3>

        <div className="booking-summary-grid">
          <p>Заезд: {formatApiDateTime(booking.startDate)}</p>
          <p>Выезд: {formatApiDateTime(booking.endDate)}</p>
          <p>Статус: {formatBookingStatus(booking.status)}</p>
          <p>Гостей: {booking.peopleCount}</p>
          <p>Стоимость: {formatPrice(booking.initialPrice)} ₽</p>
        </div>
      </div>

      <div className="booking-card-stack">
        <BookingPaymentForm booking={booking} />
        <BookingReviewForm booking={booking} />
      </div>
    </article>
  );
}
