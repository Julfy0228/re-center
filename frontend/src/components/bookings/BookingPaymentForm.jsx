import { useState } from "react";
import { createPayment } from "../../api/payments";
import AlertMessage from "../ui/AlertMessage";

function formatPrice(value) {
  return new Intl.NumberFormat("ru-RU").format(value || 0);
}

function formatPaymentMethod(method) {
  if (method === "CARD") {
    return "Банковская карта";
  }

  if (method === "SBP") {
    return "СБП";
  }

  if (method === "CASH") {
    return "Наличные при заезде";
  }

  return method || "Не указан";
}

function formatPaymentStatus(status) {
  if (status === "COMPLETED") {
    return "Оплачен";
  }

  return "Ожидает подтверждения";
}

export default function BookingPaymentForm({ booking, payment, onPaymentCreated }) {
  const [paymentMethod, setPaymentMethod] = useState("CARD");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  if (booking.status === "CANCELLED") {
    return (
      <div className="booking-card-section">
        <p className="eyebrow">Оплата</p>
        <p className="muted">Для отменённой брони оплата недоступна.</p>
      </div>
    );
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setMessage("");
    setLoading(true);

    try {
      const response = await createPayment({
        bookingId: booking.id,
        amount: booking.initialPrice,
        paymentMethod,
      });

      onPaymentCreated?.(response.data);
      setMessage("Платёж создан и отправлен в обработку.");
    } catch (err) {
      setError(
        typeof err.response?.data === "string"
          ? err.response.data
          : "Не удалось создать платёж."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="booking-card-section">
      <div className="section-heading">
        <div>
          <p className="eyebrow">Оплата</p>
          <h4>Оплатить бронирование</h4>
        </div>
        <span className="detail-chip">{formatPrice(booking.initialPrice)} ₽</span>
      </div>

      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      {payment ? (
        <div className="booking-inline-summary">
          <p>
            Платёж #{payment.id} уже создан. Статус:{" "}
            <strong>{formatPaymentStatus(payment.status)}</strong>.
          </p>
          <p>Способ оплаты: {formatPaymentMethod(payment.paymentMethod)}</p>
          {payment.status === "PENDING" && booking.status === "CONFIRMED" && (
            <p className="muted" style={{ marginTop: "8px" }}>
              Бронирование подтверждено. Возможно, платёж уже обработан - обновите страницу.
            </p>
          )}
        </div>
      ) : (
        <form className="admin-form" onSubmit={handleSubmit}>
          <label>
            <span>Способ оплаты</span>
            <select
              value={paymentMethod}
              onChange={(event) => setPaymentMethod(event.target.value)}
            >
              <option value="CARD">Банковская карта</option>
              <option value="SBP">СБП</option>
              <option value="CASH">Наличные при заезде</option>
            </select>
          </label>

          <div className="manage-actions">
            <button type="submit" disabled={loading}>
              {loading ? "Создаём платёж..." : "Оплатить"}
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
