import { useEffect, useState } from "react";
import { getActiveDiscounts } from "../../api/discounts";
import { getPromotions } from "../../api/promotions";
import { formatApiDate, formatApiDateTime } from "../../utils/date";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function formatMoney(value) {
  return new Intl.NumberFormat("ru-RU").format(value || 0);
}

function formatDiscountAmount(item) {
  if (item.type === "PERCENT") {
    return `${item.amount}%`;
  }

  return `${formatMoney(item.amount)} ₽`;
}

function formatPeriod(startDate, endDate) {
  if (!startDate && !endDate) {
    return "Сроки уточняются";
  }

  return `${formatApiDate(startDate, "без даты")} - ${formatApiDate(endDate, "без даты")}`;
}

export default function OffersPage({ user, onLogout }) {
  const [promotions, setPromotions] = useState([]);
  const [discounts, setDiscounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getPromotions(), getActiveDiscounts()])
      .then(([promotionsRes, discountsRes]) => {
        setPromotions(promotionsRes.data);
        setDiscounts(discountsRes.data);
      })
      .catch(() => setError("Не удалось загрузить акции и скидки. Проверьте backend."))
      .finally(() => setLoading(false));
  }, []);

  const hasOffers = promotions.length > 0 || discounts.length > 0;

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Акции и скидки"
      subtitle="Здесь собраны актуальные спецпредложения, которые помогут выбрать лучший вариант отдыха."
    >
      <section className="card-like offers-hero">
        <p className="eyebrow">Спецпредложения</p>
        <h3>Планируйте отдых с выгодой</h3>
        <p className="muted">
          Следите за сезонными акциями и активными скидками. Мы собрали их в одном разделе,
          чтобы ничего не потерялось между новостями и каталогом услуг.
        </p>
      </section>

      {loading ? <p className="muted">Загружаем предложения...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !error && !hasOffers ? (
        <EmptyState
          title="Пока нет активных предложений"
          description="Когда появятся скидки и акции, они будут отображаться здесь."
        />
      ) : null}

      {!loading && !error ? (
        <div className="offers-layout">
          <section className="offers-section">
            <div className="section-heading">
              <div>
                <p className="eyebrow">Акции</p>
                <h3>Промо-кампании базы</h3>
              </div>
            </div>

            {promotions.length ? (
              <div className="offers-grid">
                {promotions.map((item) => (
                  <article key={item.id} className="offer-card">
                    <p className="booking-label">{formatPeriod(item.startDate, item.endDate)}</p>
                    <h3>{item.title}</h3>
                    <p className="muted">
                      {item.description || "Описание акции пока не добавлено."}
                    </p>
                    {(item.startDate || item.endDate) ? (
                      <p className="offer-meta">
                        Обновлено: {formatApiDateTime(item.endDate || item.startDate)}
                      </p>
                    ) : null}
                  </article>
                ))}
              </div>
            ) : (
              <EmptyState
                title="Акций пока нет"
                description="Опубликованные кампании появятся здесь."
              />
            )}
          </section>

          <section className="offers-section">
            <div className="section-heading">
              <div>
                <p className="eyebrow">Скидки</p>
                <h3>Активные скидки</h3>
              </div>
            </div>

            {discounts.length ? (
              <div className="offers-grid">
                {discounts.map((item) => (
                  <article key={item.id} className="offer-card offer-card-highlight">
                    <div className="offer-topline">
                      <p className="booking-label">{formatPeriod(item.startDate, item.endDate)}</p>
                      <span className="offer-badge">{formatDiscountAmount(item)}</span>
                    </div>
                    <h3>{item.title}</h3>
                    <p className="muted">{item.description}</p>
                    <p className="offer-meta">
                      Формат: {item.type === "PERCENT" ? "процентная скидка" : "фиксированная сумма"}
                    </p>
                  </article>
                ))}
              </div>
            ) : (
              <EmptyState
                title="Активных скидок нет"
                description="Когда появятся скидки, они будут показаны в этом разделе."
              />
            )}
          </section>
        </div>
      ) : null}
    </DashboardLayout>
  );
}
