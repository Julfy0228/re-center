import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getServiceById } from "../../api/catalog";
import { getMyReviews, getPublishedReviews } from "../../api/reviews";
import DashboardLayout from "../layout/DashboardLayout";
import ServiceBookingModal from "./ServiceBookingModal";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";
import { formatApiLongDateTime } from "../../utils/date";

function formatPrice(value) {
  return new Intl.NumberFormat("ru-RU").format(value || 0);
}

function formatRating(value) {
  if (!value) {
    return "Пока нет оценок";
  }

  return `${value.toFixed(1)} / 5`;
}

function formatReviewStatus(status) {
  if (status === "APPROVED") {
    return "Опубликован";
  }

  if (status === "REJECTED") {
    return "Отклонён";
  }

  return "На модерации";
}

export default function ServiceDetailsPage({ user, onLogout }) {
  const { serviceId } = useParams();
  const [service, setService] = useState(null);
  const [publishedReviews, setPublishedReviews] = useState([]);
  const [myReviews, setMyReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [reviewError, setReviewError] = useState("");
  const [bookingOpen, setBookingOpen] = useState(false);

  useEffect(() => {
    Promise.allSettled([getServiceById(serviceId), getPublishedReviews(), getMyReviews()]).then(
      ([serviceRes, publishedRes, myRes]) => {
        if (serviceRes.status === "fulfilled") {
          setService(serviceRes.value.data);
        } else {
          setError("Не удалось загрузить информацию об услуге.");
        }

        if (publishedRes.status === "fulfilled") {
          setPublishedReviews(
            publishedRes.value.data.filter(
              (review) => Number(review.serviceId) === Number(serviceId)
            )
          );
        } else {
          setReviewError("Не удалось загрузить отзывы по услуге.");
        }

        if (myRes.status === "fulfilled") {
          setMyReviews(
            myRes.value.data.filter(
              (review) => Number(review.serviceId) === Number(serviceId)
            )
          );
        }

        setLoading(false);
      }
    );
  }, [serviceId]);

  const averageRating = useMemo(() => {
    if (!publishedReviews.length) {
      return null;
    }

    return (
      publishedReviews.reduce((sum, item) => sum + Number(item.rating || 0), 0) /
      publishedReviews.length
    );
  }, [publishedReviews]);

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title={service?.title || "Подробности услуги"}
      subtitle="Подробное описание услуги, условия бронирования и отзывы гостей."
    >
      {loading ? <p className="muted">Загружаем страницу услуги...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>
      <AlertMessage type="error">{reviewError}</AlertMessage>

      {!loading && !error && service ? (
        <>
          <section className="service-details card-like">
            <div className="service-details-media">
              {service.imageUrl ? (
                <img className="service-details-image" src={service.imageUrl} alt={service.title} />
              ) : (
                <div className="service-details-image service-image-fallback">
                  <span>Нет фото</span>
                </div>
              )}
            </div>

            <div className="service-details-content">
              <Link className="back-link" to="/services">
                ← Назад в каталог
              </Link>
              <p className="booking-label">Услуга #{service.id}</p>
              <h3>{service.title}</h3>
              <p className="news-text">
                {service.description || "Подробное описание услуги пока не добавлено."}
              </p>

              <div className="detail-chips">
                <span className="detail-chip">{formatPrice(service.price)} ₽</span>
                <span className="detail-chip">До {service.maxPeople || 1} гостей</span>
                <span className="detail-chip">{service.duration || 1} дней</span>
                <span className="detail-chip">{formatRating(averageRating)}</span>
              </div>

              <div className="manage-actions">
                <button type="button" onClick={() => setBookingOpen(true)}>
                  Забронировать
                </button>
              </div>
            </div>
          </section>

          <section className="review-showcase">
            <div className="section-heading">
              <div>
                <p className="eyebrow">Отзывы</p>
                <h3>Отзывы об этой услуге</h3>
              </div>
            </div>

            {publishedReviews.length ? (
              <div className="review-grid">
                {publishedReviews.map((review) => (
                  <article key={review.id} className="review-card">
                    <div className="review-topline">
                      <span className="detail-chip">{review.rating}/5</span>
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
                title="Пока нет отзывов по этой услуге"
                description="После публикации первых отзывов они появятся здесь."
              />
            )}
          </section>

          {myReviews.length ? (
            <section className="review-showcase">
              <div className="section-heading">
                <div>
                  <p className="eyebrow">Ваши отзывы</p>
                  <h3>Ваши отзывы по этой услуге</h3>
                </div>
              </div>

              <div className="review-grid">
                {myReviews.map((review) => (
                  <article key={review.id} className="review-card">
                    <div className="review-topline">
                      <span className="detail-chip">{formatReviewStatus(review.status)}</span>
                      <span className="offer-meta">
                        {formatApiLongDateTime(review.createdAt, "Недавно")}
                      </span>
                    </div>
                    <p className="news-text">{review.content}</p>
                    <p className="booking-label">Бронь #{review.bookingId}</p>
                  </article>
                ))}
              </div>
            </section>
          ) : null}

          <ServiceBookingModal
            isOpen={bookingOpen}
            onClose={() => setBookingOpen(false)}
            service={service}
            user={user}
          />
        </>
      ) : null}
    </DashboardLayout>
  );
}
