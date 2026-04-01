import { useEffect, useMemo, useState } from "react";
import { createBooking } from "../../api/bookings";
import { getCategories, getServices, getServicesByCategory } from "../../api/catalog";
import { createActivity } from "../../api/activities";
import { getPublishedReviews } from "../../api/reviews";
import ReviewHighlights from "../reviews/ReviewHighlights";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function toApiDateTime(value) {
  return `${value}:00`;
}

function formatPrice(value) {
  return new Intl.NumberFormat("ru-RU").format(value || 0);
}

export default function ServiceCatalog({ user, onLogout }) {
  const [categories, setCategories] = useState([]);
  const [services, setServices] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [selectedServiceId, setSelectedServiceId] = useState("");
  const [form, setForm] = useState({
    startDate: "",
    endDate: "",
    peopleCount: 1,
  });
  const [loading, setLoading] = useState(true);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [error, setError] = useState("");
  const [reviewError, setReviewError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    Promise.allSettled([getCategories(), getServices(), getPublishedReviews()]).then(
      ([categoriesRes, servicesRes, reviewsRes]) => {
        if (categoriesRes.status === "fulfilled") {
          setCategories(categoriesRes.value.data);
        }

        if (servicesRes.status === "fulfilled") {
          setServices(servicesRes.value.data);
        } else {
          setError("Не удалось загрузить каталог. Проверьте, что backend запущен.");
        }

        if (reviewsRes.status === "fulfilled") {
          setReviews(reviewsRes.value.data.slice(0, 4));
        } else {
          setReviewError("Не удалось загрузить отзывы гостей.");
        }

        setLoading(false);
      }
    );
  }, []);

  useEffect(() => {
    if (selectedCategory === "all") {
      return;
    }

    setError("");

    getServicesByCategory(selectedCategory)
      .then((response) => setServices(response.data))
      .catch(() => setError("Не удалось обновить список услуг."));
  }, [selectedCategory]);

  const selectedService = useMemo(
    () => services.find((service) => String(service.id) === String(selectedServiceId)),
    [services, selectedServiceId]
  );

  const handleCategoryChange = async (event) => {
    const nextCategory = event.target.value;
    setSelectedCategory(nextCategory);
    setError("");

    if (nextCategory === "all") {
      try {
        const response = await getServices();
        setServices(response.data);
      } catch (err) {
        setError("Не удалось обновить список услуг.");
      }
    }
  };

  const submitBooking = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    if (!selectedServiceId) {
      setError("Выберите услугу для бронирования.");
      return;
    }

    if (!form.startDate || !form.endDate) {
      setError("Укажите даты заезда и выезда.");
      return;
    }

    setBookingLoading(true);

    try {
      await createBooking({
        serviceId: Number(selectedServiceId),
        startDate: toApiDateTime(form.startDate),
        endDate: toApiDateTime(form.endDate),
        peopleCount: Number(form.peopleCount),
      });

      setMessage("Бронирование создано. Оно уже появится в разделе с вашими бронями.");
      setForm({
        startDate: "",
        endDate: "",
        peopleCount: 1,
      });

      if (user?.id) {
        createActivity({
          userId: user.id,
          type: "BOOK_SERVICE",
          details: `Создана бронь на услугу "${selectedService?.title || selectedServiceId}"`,
        }).catch(() => {});
      }
    } catch (err) {
      setError(
        typeof err.response?.data === "string"
          ? err.response.data
          : "Не удалось создать бронирование."
      );
    } finally {
      setBookingLoading(false);
    }
  };

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Каталог услуг"
      subtitle="Выберите домик или услугу, а затем сразу оформите бронирование."
    >
      {loading ? <p className="muted">Загружаем каталог...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{reviewError}</AlertMessage>

      {!loading ? (
        <>
          <section className="toolbar">
            <label className="filter-field">
              <span>Категория</span>
              <select value={selectedCategory} onChange={handleCategoryChange}>
                <option value="all">Все категории</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </label>
          </section>

          <section className="catalog-grid">
            {services.map((service) => (
              <article
                key={service.id}
                className={`service-card ${
                  String(service.id) === String(selectedServiceId) ? "service-card-active" : ""
                }`}
              >
                <div className="service-image-wrap">
                  {service.imageUrl ? (
                    <img className="service-image" src={service.imageUrl} alt={service.title} />
                  ) : (
                    <div className="service-image service-image-fallback">
                      <span>Нет фото</span>
                    </div>
                  )}
                </div>

                <p className="booking-label">Услуга #{service.id}</p>
                <h3>{service.title}</h3>
                <p className="muted">
                  {service.description || "Описание пока не добавлено."}
                </p>
                <div className="service-meta">
                  <span>{formatPrice(service.price)} ₽</span>
                  <span>До {service.maxPeople || 1} гостей</span>
                  <span>{service.duration || 1} дней</span>
                </div>
                <button type="button" onClick={() => setSelectedServiceId(String(service.id))}>
                  {String(service.id) === String(selectedServiceId)
                    ? "Услуга выбрана"
                    : "Выбрать"}
                </button>
              </article>
            ))}
          </section>

          {!services.length ? (
            <EmptyState
              title="Услуги не найдены"
              description="Попробуйте выбрать другую категорию."
            />
          ) : null}

          <section className="booking-panel">
            <div>
              <p className="eyebrow">Быстрое бронирование</p>
              <h3>{selectedService ? selectedService.title : "Сначала выберите услугу"}</h3>
              <p className="muted">
                {selectedService
                  ? `Стоимость от ${formatPrice(selectedService.price)} ₽.`
                  : "После выбора можно сразу указать даты и количество гостей."}
              </p>
            </div>

            <form className="booking-form" onSubmit={submitBooking}>
              <label>
                <span>Дата заезда</span>
                <input
                  type="datetime-local"
                  value={form.startDate}
                  onChange={(event) =>
                    setForm((current) => ({ ...current, startDate: event.target.value }))
                  }
                />
              </label>

              <label>
                <span>Дата выезда</span>
                <input
                  type="datetime-local"
                  value={form.endDate}
                  onChange={(event) =>
                    setForm((current) => ({ ...current, endDate: event.target.value }))
                  }
                />
              </label>

              <label>
                <span>Количество гостей</span>
                <input
                  type="number"
                  min="1"
                  value={form.peopleCount}
                  onChange={(event) =>
                    setForm((current) => ({ ...current, peopleCount: event.target.value }))
                  }
                />
              </label>

              <button type="submit" disabled={bookingLoading}>
                {bookingLoading ? "Создаём бронь..." : "Забронировать"}
              </button>
            </form>
          </section>

          <ReviewHighlights reviews={reviews} />
        </>
      ) : null}
    </DashboardLayout>
  );
}
