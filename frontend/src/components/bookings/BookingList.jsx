import { useEffect, useState } from "react";
import api from "../../api/axios";

export default function BookingList({ user, onLogout }) {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/bookings/my")
      .then((res) => setBookings(res.data))
      .catch(() => setError("Не удалось загрузить бронирования."))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="app-shell">
      <div className="card dashboard-card">
        <div className="dashboard-header">
          <div>
            <p className="eyebrow">Личный кабинет</p>
            <h1>Мои бронирования</h1>
            <p className="muted">
              {user
                ? `Вы вошли как ${user.email}.`
                : "Авторизация прошла успешно."}
            </p>
          </div>

          <button type="button" className="secondary-button" onClick={onLogout}>
            Выйти
          </button>
        </div>

        {loading && <p className="muted">Загружаем бронирования...</p>}
        {error && <p className="alert alert-error">{error}</p>}

        {!loading && !error && bookings.length === 0 && (
          <div className="empty-state">
            <h2>Пока бронирований нет</h2>
            <p className="muted">После создания брони она появится здесь.</p>
          </div>
        )}

        <div className="booking-grid">
          {bookings.map((booking) => (
            <article key={booking.id} className="booking-card">
              <p className="booking-label">Бронь #{booking.id}</p>
              <h2>{booking.serviceTitle}</h2>
              <p>
                {booking.startDate} - {booking.endDate}
              </p>
              <p>Статус: {booking.status}</p>
              <p>Гостей: {booking.peopleCount}</p>
              <p>Стоимость: {booking.initialPrice}</p>
            </article>
          ))}
        </div>
      </div>
    </div>
  );
}
