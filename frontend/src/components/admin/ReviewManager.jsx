import { useEffect, useMemo, useState } from "react";
import {
  approveReview,
  deleteReview,
  getAllReviews,
  rejectReview,
} from "../../api/reviews";
import { formatApiDateTime } from "../../utils/date";
import { isAdmin } from "../../utils/permissions";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function formatReviewStatus(status) {
  if (status === "APPROVED") {
    return "Опубликован";
  }

  if (status === "REJECTED") {
    return "Отклонён";
  }

  return "На модерации";
}

function renderStars(value) {
  const rating = Number(value) || 0;
  return "★".repeat(rating) + "☆".repeat(Math.max(0, 5 - rating));
}

export default function ReviewManager({ user }) {
  const [reviews, setReviews] = useState([]);
  const [filter, setFilter] = useState("PENDING");
  const [loading, setLoading] = useState(true);
  const [actionId, setActionId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getAllReviews()
      .then((response) => setReviews(response.data))
      .catch(() => setError("Не удалось загрузить отзывы для модерации."))
      .finally(() => setLoading(false));
  }, []);

  const filteredReviews = useMemo(() => {
    if (filter === "ALL") {
      return reviews;
    }

    return reviews.filter((item) => item.status === filter);
  }, [filter, reviews]);

  const updateReviewStatus = async (reviewId, action) => {
    setActionId(`${action}-${reviewId}`);
    setMessage("");
    setError("");

    try {
      const response = action === "approve" ? await approveReview(reviewId) : await rejectReview(reviewId);
      setReviews((current) =>
        current.map((item) => (item.id === reviewId ? response.data : item))
      );
      setMessage(
        action === "approve"
          ? `Отзыв #${reviewId} опубликован.`
          : `Отзыв #${reviewId} отклонён.`
      );
    } catch (err) {
      setError("Не удалось обновить статус отзыва.");
    } finally {
      setActionId(null);
    }
  };

  const removeReview = async (reviewId) => {
    if (!window.confirm(`Удалить отзыв #${reviewId}?`)) {
      return;
    }

    setActionId(`delete-${reviewId}`);
    setMessage("");
    setError("");

    try {
      await deleteReview(reviewId);
      setReviews((current) => current.filter((item) => item.id !== reviewId));
      setMessage(`Отзыв #${reviewId} удалён.`);
    } catch (err) {
      setError("Не удалось удалить отзыв.");
    } finally {
      setActionId(null);
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Отзывы</p>
        <h3>Модерация отзывов</h3>
        <p className="muted">Проверяйте новые отзывы гостей перед публикацией на сайте.</p>
      </div>

      <div className="filter-toggle">
        <button
          type="button"
          className={filter === "PENDING" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("PENDING")}
        >
          На модерации
        </button>
        <button
          type="button"
          className={filter === "APPROVED" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("APPROVED")}
        >
          Опубликованные
        </button>
        <button
          type="button"
          className={filter === "ALL" ? "secondary-button toggle-active" : "secondary-button"}
          onClick={() => setFilter("ALL")}
        >
          Все
        </button>
      </div>

      {loading ? <p className="muted">Загружаем отзывы...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <div className="manage-list">
        {!loading && !filteredReviews.length ? (
          <EmptyState
            title="Нет отзывов для выбранного фильтра"
            description="Когда появятся новые отзывы, они отобразятся здесь."
          />
        ) : null}

        {filteredReviews.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Отзыв #{item.id} по брони #{item.bookingId}</p>
              <span className="detail-chip">{formatReviewStatus(item.status)}</span>
            </div>
            <div className="detail-chips">
              <span className="detail-chip">{renderStars(item.rating)}</span>
              <span className="detail-chip">{formatApiDateTime(item.createdAt)}</span>
            </div>
            <p className="muted">{item.content}</p>
            <div className="manage-actions">
              {item.status !== "APPROVED" ? (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => updateReviewStatus(item.id, "approve")}
                  disabled={Boolean(actionId)}
                >
                  {actionId === `approve-${item.id}` ? "Публикуем..." : "Одобрить"}
                </button>
              ) : null}
              {item.status !== "REJECTED" ? (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => updateReviewStatus(item.id, "reject")}
                  disabled={Boolean(actionId)}
                >
                  {actionId === `reject-${item.id}` ? "Отклоняем..." : "Отклонить"}
                </button>
              ) : null}
              {isAdmin(user) ? (
                <button
                  type="button"
                  onClick={() => removeReview(item.id)}
                  disabled={Boolean(actionId)}
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
