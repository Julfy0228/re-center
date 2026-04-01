import { useEffect, useState } from "react";
import {
  completePayment,
  deletePayment,
  getAllPayments,
  getCompletedPayments,
  getPendingPayments,
} from "../../api/payments";
import { formatApiDateTime } from "../../utils/date";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function formatPrice(value) {
  return `${new Intl.NumberFormat("ru-RU").format(value || 0)} ₽`;
}

function fetchByFilter(filter) {
  if (filter === "PENDING") {
    return getPendingPayments();
  }

  if (filter === "COMPLETED") {
    return getCompletedPayments();
  }

  return getAllPayments();
}

export default function PaymentManager() {
  const [payments, setPayments] = useState([]);
  const [filter, setFilter] = useState("ALL");
  const [loading, setLoading] = useState(true);
  const [actionId, setActionId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    fetchByFilter(filter)
      .then((response) => setPayments(response.data))
      .catch(() => setError("Не удалось загрузить платежи."))
      .finally(() => setLoading(false));
  }, [filter]);

  const handleComplete = async (paymentId) => {
    setActionId(`complete-${paymentId}`);
    setMessage("");
    setError("");

    try {
      const response = await completePayment(paymentId);
      setPayments((current) =>
        current.map((item) => (item.id === paymentId ? response.data : item))
      );
      setMessage(`Платёж #${paymentId} отмечен как завершённый.`);
    } catch (err) {
      setError("Не удалось обновить статус платежа.");
    } finally {
      setActionId(null);
    }
  };

  const handleDelete = async (paymentId) => {
    if (!window.confirm(`Удалить платёж #${paymentId}?`)) {
      return;
    }

    setActionId(`delete-${paymentId}`);
    setMessage("");
    setError("");

    try {
      await deletePayment(paymentId);
      setPayments((current) => current.filter((item) => item.id !== paymentId));
      setMessage(`Платёж #${paymentId} удалён.`);
    } catch (err) {
      setError("Не удалось удалить платёж.");
    } finally {
      setActionId(null);
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Платежи</p>
        <h3>Управление платежами</h3>
        <p className="muted">Следите за новыми оплатами и вручную завершайте обработку.</p>
      </div>

      <div className="filter-toggle">
        <button
          type="button"
          className={filter === "ALL" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("ALL")}
        >
          Все
        </button>
        <button
          type="button"
          className={filter === "PENDING" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("PENDING")}
        >
          В ожидании
        </button>
        <button
          type="button"
          className={filter === "COMPLETED" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("COMPLETED")}
        >
          Завершённые
        </button>
      </div>

      {loading ? <p className="muted">Загружаем платежи...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <div className="manage-list">
        {!loading && !payments.length ? (
          <EmptyState
            title="Платежи не найдены"
            description="Когда появятся оплаты, они будут отображаться в этом разделе."
          />
        ) : null}

        {payments.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Платёж #{item.id} по брони #{item.bookingId}</p>
              <span className="detail-chip">{item.status}</span>
            </div>
            <div className="detail-chips">
              <span className="detail-chip">{formatPrice(item.amount)}</span>
              <span className="detail-chip">{item.paymentMethod || "Не указан способ"}</span>
              <span className="detail-chip">{formatApiDateTime(item.paymentDate)}</span>
            </div>
            <div className="manage-actions">
              {item.status !== "COMPLETED" ? (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => handleComplete(item.id)}
                  disabled={Boolean(actionId)}
                >
                  {actionId === `complete-${item.id}` ? "Завершаем..." : "Завершить"}
                </button>
              ) : null}
              <button
                type="button"
                onClick={() => handleDelete(item.id)}
                disabled={Boolean(actionId)}
              >
                {actionId === `delete-${item.id}` ? "Удаляем..." : "Удалить"}
              </button>
            </div>
          </article>
        ))}
      </div>
    </article>
  );
}
