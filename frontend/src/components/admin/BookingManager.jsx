import { useEffect, useMemo, useState } from "react";
import { createActivity } from "../../api/activities";
import { deleteBooking, getAllBookings, updateBooking } from "../../api/bookings";
import { formatApiDateTime } from "../../utils/date";
import { isAdmin } from "../../utils/permissions";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function formatBookingStatus(status) {
  if (status === "CONFIRMED") {
    return "Подтверждено";
  }

  if (status === "CANCELLED") {
    return "Отменено";
  }

  return "Ожидает подтверждения";
}

function formatPrice(value) {
  return `${new Intl.NumberFormat("ru-RU").format(value || 0)} ₽`;
}

export default function BookingManager({ user }) {
  const [bookings, setBookings] = useState([]);
  const [filter, setFilter] = useState("PENDING");
  const [loading, setLoading] = useState(true);
  const [actionId, setActionId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getAllBookings()
      .then((response) => setBookings(response.data))
      .catch(() => setError("Не удалось загрузить бронирования для управления."))
      .finally(() => setLoading(false));
  }, []);

  const filteredBookings = useMemo(() => {
    if (filter === "ALL") {
      return bookings;
    }

    return bookings.filter((item) => item.status === filter);
  }, [bookings, filter]);

  const updateBookingStatus = async (booking, nextStatus) => {
    const actionKey = `${nextStatus}-${booking.id}`;
    setActionId(actionKey);
    setMessage("");
    setError("");

    try {
      const response = await updateBooking(booking.id, { status: nextStatus });
      setBookings((current) =>
        current.map((item) => (item.id === booking.id ? response.data : item))
      );
      setMessage(
        nextStatus === "CONFIRMED"
          ? `Бронь #${booking.id} подтверждена.`
          : `Бронь #${booking.id} отменена.`
      );

      if (booking.userId) {
        createActivity({
          userId: booking.userId,
          type: nextStatus === "CONFIRMED" ? "BOOK_SERVICE" : "CANCEL_BOOKING",
          details:
            nextStatus === "CONFIRMED"
              ? `Бронирование #${booking.id} подтверждено менеджером`
              : `Бронирование #${booking.id} отменено менеджером`,
        }).catch(() => {});
      }
    } catch (err) {
      setError("Не удалось обновить статус бронирования.");
    } finally {
      setActionId(null);
    }
  };

  const removeBooking = async (bookingId) => {
    if (!window.confirm(`Удалить бронь #${bookingId}?`)) {
      return;
    }

    const actionKey = `delete-${bookingId}`;
    setActionId(actionKey);
    setMessage("");
    setError("");

    try {
      await deleteBooking(bookingId);
      setBookings((current) => current.filter((item) => item.id !== bookingId));
      setMessage(`Бронь #${bookingId} удалена.`);
    } catch (err) {
      setError("Не удалось удалить бронирование.");
    } finally {
      setActionId(null);
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Бронирования</p>
        <h3>Подтверждение броней</h3>
        <p className="muted">
          Подтверждайте, отменяйте и очищайте брони прямо из панели управления.
        </p>
      </div>

      <div className="filter-toggle">
        <button
          type="button"
          className={filter === "PENDING" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("PENDING")}
        >
          Ожидают
        </button>
        <button
          type="button"
          className={
            filter === "CONFIRMED" ? "secondary-button toggle-active" : "secondary-button"
          }
          onClick={() => setFilter("CONFIRMED")}
        >
          Подтверждённые
        </button>
        <button
          type="button"
          className={
            filter === "CANCELLED" ? "secondary-button toggle-active" : "secondary-button"
          }
          onClick={() => setFilter("CANCELLED")}
        >
          Отменённые
        </button>
        <button
          type="button"
          className={filter === "ALL" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("ALL")}
        >
          Все
        </button>
      </div>

      {loading ? <p className="muted">Загружаем бронирования...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <div className="manage-list">
        {!loading && !filteredBookings.length ? (
          <EmptyState
            title="Нет бронирований для выбранного фильтра"
            description="Когда появятся новые брони, они будут отображаться здесь."
          />
        ) : null}

        {filteredBookings.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Бронь #{item.id}</p>
              <span className="detail-chip">{formatBookingStatus(item.status)}</span>
            </div>

            <h4>{item.serviceTitle}</h4>
            <div className="detail-chips">
              <span className="detail-chip">{item.userEmail}</span>
              <span className="detail-chip">
                {formatApiDateTime(item.startDate)} - {formatApiDateTime(item.endDate)}
              </span>
              <span className="detail-chip">{item.peopleCount} гостей</span>
              <span className="detail-chip">{formatPrice(item.initialPrice)}</span>
            </div>

            <div className="manage-actions">
              {item.status !== "CONFIRMED" ? (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => updateBookingStatus(item, "CONFIRMED")}
                  disabled={actionId === `CONFIRMED-${item.id}`}
                >
                  {actionId === `CONFIRMED-${item.id}` ? "Подтверждаем..." : "Подтвердить"}
                </button>
              ) : null}
              {item.status !== "CANCELLED" ? (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => updateBookingStatus(item, "CANCELLED")}
                  disabled={actionId === `CANCELLED-${item.id}`}
                >
                  {actionId === `CANCELLED-${item.id}` ? "Отменяем..." : "Отменить"}
                </button>
              ) : null}
              {isAdmin(user) ? (
                <button
                  type="button"
                  onClick={() => removeBooking(item.id)}
                  disabled={actionId === `delete-${item.id}`}
                >
                  {actionId === `delete-${item.id}` ? "Удаляем..." : "Удалить"}
                </button>
              ) : null}
            </div>
          </article>
        ))}
      </div>
    </article>
  );
}
