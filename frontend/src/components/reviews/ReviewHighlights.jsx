import { formatApiLongDateTime } from "../../utils/date";
import EmptyState from "../ui/EmptyState";

function renderStars(value) {
  const rating = Number(value) || 0;
  return "★".repeat(rating) + "☆".repeat(Math.max(0, 5 - rating));
}

export default function ReviewHighlights({ reviews }) {
  return (
    <section className="review-showcase">
      <div className="section-heading">
        <div>
          <p className="eyebrow">Отзывы гостей</p>
          <h3>Что говорят о Re-Center</h3>
        </div>
      </div>

      {reviews.length ? (
        <div className="review-grid">
          {reviews.map((review) => (
            <article key={review.id} className="review-card">
              <div className="review-topline">
                <span className="detail-chip">{renderStars(review.rating)}</span>
                <span className="offer-meta">
                  {formatApiLongDateTime(review.createdAt, "Недавно")}
                </span>
              </div>
              <p className="news-text">{review.content}</p>
              <p className="booking-label">Бронь #{review.bookingId}</p>
            </article>
          ))}
        </div>
      ) : (
        <EmptyState
          title="Пока нет опубликованных отзывов"
          description="Как только гости поделятся впечатлениями, они появятся здесь."
        />
      )}
    </section>
  );
}
