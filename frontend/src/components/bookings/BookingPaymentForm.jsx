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

export default function BookingPaymentForm({ booking }) {
  const [paymentMethod, setPaymentMethod] = useState("CARD");
  const [payment, setPayment] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const canPay = booking.status !== "CANCELLED";

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

      setPayment(response.data);
      setMessage("Платёж создан. Статус можно уточнить у администратора.");
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

  if (!canPay) {
    return <p className="muted">Для отменённой брони оплата недоступна.</p>;
  }

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
          <p className="muted">
            Платёж #{payment.id} создан со статусом <strong>{payment.status}</strong>.
          </p>
          <p className="muted">Способ оплаты: {formatPaymentMethod(payment.paymentMethod)}</p>
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
