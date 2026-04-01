import { useEffect, useMemo, useState } from "react";
import DashboardLayout from "../layout/DashboardLayout";
import { getPublishedNews } from "../../api/news";
import { formatApiLongDateTime } from "../../utils/date";

function truncateText(value, maxLength = 220) {
  if (!value || value.length <= maxLength) {
    return value;
  }

  return `${value.slice(0, maxLength).trimEnd()}...`;
}

export default function NewsFeed({ user, onLogout }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    getPublishedNews()
      .then((res) => setItems(res.data))
      .catch(() =>
        setError("Не удалось загрузить новости. Проверьте backend и опубликованные записи.")
      )
      .finally(() => setLoading(false));
  }, []);

  const selectedItem = useMemo(
    () => items.find((item) => item.id === selectedId) || null,
    [items, selectedId]
  );

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Новости базы"
      subtitle="Акции, сезонные предложения и свежие объявления для гостей."
    >
      {loading && <p className="muted">Загружаем новости...</p>}
      {error && <p className="alert alert-error">{error}</p>}

      {!loading && !error && items.length === 0 && (
        <div className="empty-state">
          <h3>Пока новостей нет</h3>
          <p className="muted">Когда вы добавите опубликованные новости, они появятся здесь.</p>
        </div>
      )}

      <section className="news-grid">
        {items.map((item) => (
          <article key={item.id} className="news-card">
            <div className="news-image-wrap">
              {item.imageUrl ? (
                <img className="news-image" src={item.imageUrl} alt={item.title} />
              ) : (
                <div className="news-image news-image-fallback">
                  <span>Новость без изображения</span>
                </div>
              )}
            </div>

            <div className="news-content">
              <p className="booking-label">
                {formatApiLongDateTime(item.publishedAt || item.createdAt)}
              </p>
              <h3>{item.title}</h3>
              <p className="muted news-text">{truncateText(item.content)}</p>
              <div className="news-actions">
                <p className="news-author">
                  {item.authorEmail ? `Автор: ${item.authorEmail}` : "Команда Re-Center"}
                </p>
                <button type="button" onClick={() => setSelectedId(item.id)}>
                  Читать полностью
                </button>
              </div>
            </div>
          </article>
        ))}
      </section>

      {selectedItem && (
        <div className="modal-backdrop" onClick={() => setSelectedId(null)}>
          <article
            className="modal-card"
            onClick={(event) => event.stopPropagation()}
          >
            <button
              type="button"
              className="modal-close"
              onClick={() => setSelectedId(null)}
            >
              Закрыть
            </button>

            {selectedItem.imageUrl ? (
              <img
                className="modal-image"
                src={selectedItem.imageUrl}
                alt={selectedItem.title}
              />
            ) : null}

            <p className="booking-label">
              {formatApiLongDateTime(selectedItem.publishedAt || selectedItem.createdAt)}
            </p>
            <h3>{selectedItem.title}</h3>
            <p className="news-author">
              {selectedItem.authorEmail
                ? `Автор: ${selectedItem.authorEmail}`
                : "Команда Re-Center"}
            </p>
            <p className="news-text modal-text">{selectedItem.content}</p>
          </article>
        </div>
      )}
    </DashboardLayout>
  );
}
