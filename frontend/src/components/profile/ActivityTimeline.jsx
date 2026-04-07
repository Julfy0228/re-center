import { formatApiDateTime } from "../../utils/date";
import EmptyState from "../ui/EmptyState";

function formatActivityType(type) {
  if (type === "BOOK_SERVICE") {
    return "Бронирование";
  }

  if (type === "UPDATE_PROFILE") {
    return "Профиль";
  }

  if (type === "LOGIN") {
    return "Вход";
  }

  if (type === "LOGOUT") {
    return "Выход";
  }

  if (type === "VIEW_PAGE") {
    return "Просмотр";
  }

  if (type === "CANCEL_BOOKING") {
    return "Отмена бронирования";
  }

  if (type === "APPLY_DISCOUNT") {
    return "Скидка";
  }

  return type || "Событие";
}

export default function ActivityTimeline({ items }) {
  return (
    <section className="card-like profile-activity">
      <div className="section-heading">
        <div>
          <p className="eyebrow">История</p>
          <h3>Последние действия</h3>
        </div>
      </div>

      {items.length ? (
        <div className="activity-list">
          {items.map((item) => (
            <article key={item.id} className="activity-item">
              <div className="activity-head">
                <span className="detail-chip">{formatActivityType(item.type)}</span>
                <span className="offer-meta">{formatApiDateTime(item.createdAt)}</span>
              </div>
              <p className="muted">{item.details || "Подробности действия не указаны."}</p>
            </article>
          ))}
        </div>
      ) : (
        <EmptyState
          title="История пока пустая"
          description="После действий в системе они начнут отображаться в этом разделе."
        />
      )}
    </section>
  );
}
