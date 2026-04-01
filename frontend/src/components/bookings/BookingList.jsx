import { useEffect, useState } from "react";
import { getMyBookings } from "../../api/bookings";
import BookingCard from "./BookingCard";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

export default function BookingList({ user, onLogout }) {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    getMyBookings()
      .then((response) => setBookings(response.data))
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
      subtitle="Здесь собраны все ваши оформленные брони, быстрые действия по оплате и форма отзыва."
    >
      {loading ? <p className="muted">Загружаем бронирования...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !error && !bookings.length ? (
        <EmptyState
          title="Пока бронирований нет"
          description="Перейдите в каталог и создайте первую бронь."
        />
      ) : null}

      <section className="booking-grid booking-grid-extended">
        {bookings.map((booking) => (
          <BookingCard key={booking.id} booking={booking} />
        ))}
      </section>
    </DashboardLayout>
  );
}
