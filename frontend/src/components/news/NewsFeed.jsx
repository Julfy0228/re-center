import { useEffect, useMemo, useState } from "react";
import { getPublishedNews } from "../../api/news";
import { formatApiLongDateTime } from "../../utils/date";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";
import Modal from "../ui/Modal";

function truncateText(value, maxLength = 220) {
  if (!value || value.length <= maxLength) {
    return value;
  }

  return `${value.slice(0, maxLength).trimEnd()}...`;
}

const PAGE_SIZE = 6;

export default function NewsFeed({ user, onLogout }) {
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);
  const [pagination, setPagination] = useState({
    page: 0,
    totalPages: 0,
    totalItems: 0,
    first: true,
    last: true,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError("");

    getPublishedNews({ page, size: PAGE_SIZE })
      .then((response) => {
        const payload = response.data;
        setItems(payload.items || []);
        setPagination({
          page: payload.page ?? 0,
          totalPages: payload.totalPages ?? 0,
          totalItems: payload.totalItems ?? 0,
          first: Boolean(payload.first),
          last: Boolean(payload.last),
        });
      })
      .catch(() =>
        setError("Не удалось загрузить новости. Проверьте backend и опубликованные записи.")
      )
      .finally(() => setLoading(false));
  }, [page]);

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
      {loading ? <p className="muted">Загружаем новости...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !error && !items.length ? (
        <EmptyState
          title="Пока новостей нет"
          description="Когда вы добавите опубликованные новости, они появятся здесь."
        />
      ) : null}

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

      {!loading && pagination.totalPages > 1 ? (
        <section className="pagination-bar">
          <button
            type="button"
            className="secondary-button"
            onClick={() => setPage((current) => Math.max(current - 1, 0))}
            disabled={pagination.first}
          >
            Назад
          </button>
          <p className="muted pagination-copy">
            Страница {pagination.page + 1} из {pagination.totalPages}
            {pagination.totalItems ? `, всего новостей: ${pagination.totalItems}` : ""}
          </p>
          <button
            type="button"
            className="secondary-button"
            onClick={() => setPage((current) => current + 1)}
            disabled={pagination.last}
          >
            Вперёд
          </button>
        </section>
      ) : null}

      <Modal isOpen={Boolean(selectedItem)} onClose={() => setSelectedId(null)}>
        {selectedItem ? (
          <>
            <button type="button" className="modal-close" onClick={() => setSelectedId(null)}>
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
          </>
        ) : null}
      </Modal>
    </DashboardLayout>
  );
}
