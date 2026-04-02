import { useEffect, useState } from "react";
import { getMyBookings } from "../../api/bookings";
import { getMyPayments } from "../../api/payments";
import { getMyReviews } from "../../api/reviews";
import BookingCard from "./BookingCard";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function buildBookingMap(items) {
  const map = new Map();

  items.forEach((item) => {
    const bookingId = Number(item.bookingId);
    if (!map.has(bookingId)) {
      map.set(bookingId, item);
    }
  });

  return map;
}

export default function BookingList({ user, onLogout }) {
  const [bookings, setBookings] = useState([]);
  const [payments, setPayments] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.allSettled([getMyBookings(), getMyPayments(), getMyReviews()])
      .then(([bookingsResult, paymentsResult, reviewsResult]) => {
        if (bookingsResult.status === "fulfilled") {
          setBookings(bookingsResult.value.data);
        } else {
          setError(
            "Не удалось загрузить бронирования. Проверьте backend и авторизацию."
          );
        }

        if (paymentsResult.status === "fulfilled") {
          setPayments(paymentsResult.value.data);
        }

        if (reviewsResult.status === "fulfilled") {
          setReviews(reviewsResult.value.data);
        }

        if (
          bookingsResult.status === "fulfilled" &&
          (paymentsResult.status === "rejected" || reviewsResult.status === "rejected")
        ) {
          setError(
            "Часть данных кабинета не загрузилась, но ваши бронирования доступны."
          );
        }
      })
      .finally(() => setLoading(false));
  }, []);

  const paymentsByBookingId = buildBookingMap(payments);
  const reviewsByBookingId = buildBookingMap(reviews);

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Мои бронирования"
      subtitle="Здесь собраны все ваши оформленные брони, быстрые действия по оплате и уже сохранённые отзывы."
    >
      {loading ? <p className="muted">Загружаем бронирования...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !bookings.length ? (
        <EmptyState
          title="Пока бронирований нет"
          description="Перейдите в каталог и создайте первую бронь."
        />
      ) : null}

      <section className="booking-grid booking-grid-extended">
        {bookings.map((booking) => (
          <BookingCard
            key={booking.id}
            booking={booking}
            payment={paymentsByBookingId.get(Number(booking.id))}
            review={reviewsByBookingId.get(Number(booking.id))}
            onPaymentCreated={(payment) =>
              setPayments((current) => {
                const next = current.filter(
                  (item) => Number(item.bookingId) !== Number(payment.bookingId)
                );
                return [payment, ...next];
              })
            }
            onReviewCreated={(review) =>
              setReviews((current) => {
                const next = current.filter(
                  (item) => Number(item.bookingId) !== Number(review.bookingId)
                );
                return [review, ...next];
              })
            }
          />
        ))}
      </section>
    </DashboardLayout>
  );
}
