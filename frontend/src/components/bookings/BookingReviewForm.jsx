import { useState } from "react";
import { createReview } from "../../api/reviews";
import AlertMessage from "../ui/AlertMessage";

function renderStars(value) {
  return "★".repeat(value) + "☆".repeat(Math.max(0, 5 - value));
}

export default function BookingReviewForm({ booking }) {
  const [form, setForm] = useState({
    rating: "5",
    content: "",
  });
  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  if (booking.status !== "CONFIRMED") {
    return (
      <div className="booking-card-section">
        <p className="eyebrow">Отзыв</p>
        <p className="muted">Оставить отзыв можно после подтверждения бронирования.</p>
      </div>
    );
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setMessage("");
    setLoading(true);

    try {
      await createReview({
        bookingId: booking.id,
        content: form.content,
        rating: Number(form.rating),
      });

      setSubmitted(true);
      setMessage("Отзыв отправлен на модерацию.");
    } catch (err) {
      setError(
        typeof err.response?.data === "string"
          ? err.response.data
          : "Не удалось отправить отзыв."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="booking-card-section">
      <div className="section-heading">
        <div>
          <p className="eyebrow">Отзыв</p>
          <h4>Поделитесь впечатлением</h4>
        </div>
        <span className="detail-chip">{renderStars(Number(form.rating))}</span>
      </div>

      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      {submitted ? (
        <p className="muted">
          Спасибо. После проверки отзыв появится в публичной ленте для гостей.
        </p>
      ) : (
        <form className="admin-form" onSubmit={handleSubmit}>
          <label>
            <span>Оценка</span>
            <select
              value={form.rating}
              onChange={(event) =>
                setForm((current) => ({ ...current, rating: event.target.value }))
              }
            >
              <option value="5">5 — Отлично</option>
              <option value="4">4 — Очень хорошо</option>
              <option value="3">3 — Нормально</option>
              <option value="2">2 — Слабо</option>
              <option value="1">1 — Плохо</option>
            </select>
          </label>

          <label>
            <span>Текст отзыва</span>
            <textarea
              rows="4"
              value={form.content}
              onChange={(event) =>
                setForm((current) => ({ ...current, content: event.target.value }))
              }
              required
            />
          </label>

          <div className="manage-actions">
            <button type="submit" disabled={loading || !form.content.trim()}>
              {loading ? "Отправляем отзыв..." : "Отправить отзыв"}
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
