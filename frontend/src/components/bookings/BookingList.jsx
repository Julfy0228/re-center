import { useEffect, useState } from "react";
import DashboardLayout from "../layout/DashboardLayout";
import { getMyBookings } from "../../api/bookings";
import { formatApiDateTime } from "../../utils/date";

function formatPrice(value) {
  return new Intl.NumberFormat("ru-RU").format(value || 0);
}

export default function BookingList({ user, onLogout }) {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    getMyBookings()
      .then((res) => setBookings(res.data))
      .catch(() =>
        setError("Не удалось загрузить бронирования. Проверьте backend и авторизацию.")
      )
      .finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Мои бронирования"
      subtitle="Здесь собраны все ваши оформленные брони по услугам базы отдыха."
    >
      {loading && <p className="muted">Загружаем бронирования...</p>}
      {error && <p className="alert alert-error">{error}</p>}

      {!loading && !error && bookings.length === 0 && (
        <div className="empty-state">
          <h3>Пока бронирований нет</h3>
          <p className="muted">Перейдите в каталог и создайте первую бронь.</p>
        </div>
      )}

      <section className="booking-grid">
        {bookings.map((booking) => (
          <article key={booking.id} className="booking-card">
            <p className="booking-label">Бронь #{booking.id}</p>
            <h3>{booking.serviceTitle}</h3>
            <p>Заезд: {formatApiDateTime(booking.startDate)}</p>
            <p>Выезд: {formatApiDateTime(booking.endDate)}</p>
            <p>Статус: {booking.status}</p>
            <p>Гостей: {booking.peopleCount}</p>
            <p>Стоимость: {formatPrice(booking.initialPrice)} ₽</p>
          </article>
        ))}
      </section>
    </DashboardLayout>
  );
}
