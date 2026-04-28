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

const initialFilters = {
  status: "PENDING",
  dateFrom: "",
  dateTo: "",
  paid: "all",
};

export default function BookingManager({ user }) {
  const [bookings, setBookings] = useState([]);
  const [filters, setFilters] = useState(initialFilters);
  const [loading, setLoading] = useState(true);
  const [actionId, setActionId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const params = {};
    if (filters.dateFrom) {
      params.dateFrom = filters.dateFrom;
    }
    if (filters.dateTo) {
      params.dateTo = filters.dateTo;
    }
    if (filters.paid !== "all") {
      params.paid = filters.paid === "paid";
    }

    setLoading(true);
    getAllBookings(params)
      .then((response) => setBookings(response.data))
      .catch(() => setError("Не удалось загрузить бронирования для управления."))
      .finally(() => setLoading(false));
  }, [filters.dateFrom, filters.dateTo, filters.paid]);

  const filteredBookings = useMemo(() => {
    if (filters.status === "ALL") {
      return bookings;
    }

    return bookings.filter((item) => item.status === filters.status);
  }, [bookings, filters.status]);

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
          Подтверждайте, отменяйте и отфильтровывайте брони по статусу, датам и оплате.
        </p>
      </div>

      <div className="booking-filter-grid admin-filter-grid">
        <label className="filter-field">
          <span>Статус</span>
          <select
            value={filters.status}
            onChange={(event) =>
              setFilters((current) => ({ ...current, status: event.target.value }))
            }
          >
            <option value="PENDING">Ожидают</option>
            <option value="CONFIRMED">Подтверждённые</option>
            <option value="CANCELLED">Отменённые</option>
            <option value="ALL">Все</option>
          </select>
        </label>

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
              <span className="detail-chip">{item.paid ? "Оплачено" : "Не оплачено"}</span>
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
