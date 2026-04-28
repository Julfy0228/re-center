import { useEffect, useMemo, useState } from "react";
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

const initialFilters = {
  dateFrom: "",
  dateTo: "",
  paid: "all",
};

export default function BookingList({ user, onLogout }) {
  const [bookings, setBookings] = useState([]);
  const [payments, setPayments] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [filters, setFilters] = useState(initialFilters);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const bookingParams = {};
    if (filters.dateFrom) {
      bookingParams.dateFrom = filters.dateFrom;
    }
    if (filters.dateTo) {
      bookingParams.dateTo = filters.dateTo;
    }
    if (filters.paid !== "all") {
      bookingParams.paid = filters.paid === "paid";
    }

    setLoading(true);
    setError("");

    Promise.allSettled([getMyBookings(bookingParams), getMyPayments(), getMyReviews()])
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
  }, [filters]);

  const paymentsByBookingId = useMemo(() => buildBookingMap(payments), [payments]);
  const reviewsByBookingId = useMemo(() => buildBookingMap(reviews), [reviews]);

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Мои бронирования"
      subtitle="Здесь собраны все ваши оформленные брони, быстрые действия по оплате и уже сохранённые отзывы."
    >
      <section className="card-like booking-filter-panel">
        <div>
          <p className="eyebrow">Фильтры</p>
          <h3>Срез по датам и оплате</h3>
          <p className="muted">
            Можно показать только нужный диапазон поездок и быстро отделить оплаченные бронирования от неоплаченных.
          </p>
        </div>

        <div className="booking-filter-grid">
          <label className="filter-field">
            <span>Дата от</span>
            <input
              type="date"
              value={filters.dateFrom}
              onChange={(event) =>
                setFilters((current) => ({ ...current, dateFrom: event.target.value }))
              }
            />
          </label>

          <label className="filter-field">
            <span>Дата до</span>
            <input
              type="date"
              value={filters.dateTo}
              onChange={(event) =>
                setFilters((current) => ({ ...current, dateTo: event.target.value }))
              }
            />
          </label>

          <label className="filter-field">
            <span>Оплата</span>
            <select
              value={filters.paid}
              onChange={(event) =>
                setFilters((current) => ({ ...current, paid: event.target.value }))
              }
            >
              <option value="all">Все</option>
              <option value="paid">Оплаченные</option>
              <option value="unpaid">Неоплаченные</option>
            </select>
          </label>

          <button
            type="button"
            className="secondary-button"
            onClick={() => setFilters(initialFilters)}
          >
            Сбросить фильтры
          </button>
        </div>
      </section>

      {loading ? <p className="muted">Загружаем бронирования...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !bookings.length ? (
        <EmptyState
          title="Бронирований не найдено"
          description="Попробуйте изменить фильтры или перейдите в каталог и создайте первую бронь."
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
