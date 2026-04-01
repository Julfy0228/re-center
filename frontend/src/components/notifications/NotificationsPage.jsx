import { useEffect, useState } from "react";
import {
  deleteNotification,
  getMyNotifications,
  getUnreadCount,
  getUnreadNotifications,
  markNotificationAsRead,
} from "../../api/notifications";
import { formatApiDateTime } from "../../utils/date";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

function formatNotificationType(type) {
  if (type === "SUCCESS") {
    return "Успех";
  }

  if (type === "WARNING") {
    return "Предупреждение";
  }

  if (type === "ERROR") {
    return "Ошибка";
  }

  return "Информация";
}

export default function NotificationsPage({ user, onLogout }) {
  const [notifications, setNotifications] = useState([]);
  const [filter, setFilter] = useState("all");
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [actionId, setActionId] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadData() {
      setLoading(true);
      setError("");

      try {
        const [notificationsRes, unreadCountRes] = await Promise.all([
          filter === "unread" ? getUnreadNotifications() : getMyNotifications(),
          getUnreadCount(),
        ]);

        if (cancelled) {
          return;
        }

        setNotifications(notificationsRes.data);
        setUnreadCount(Number(unreadCountRes.data) || 0);
      } catch (err) {
        if (!cancelled) {
          setError("Не удалось загрузить уведомления.");
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadData();

    return () => {
      cancelled = true;
    };
  }, [filter]);

  const handleMarkRead = async (notificationId) => {
    setActionId(`read-${notificationId}`);
    setError("");
    setMessage("");

    try {
      const response = await markNotificationAsRead(notificationId);
      const updated = response.data;

      setNotifications((current) => {
        if (filter === "unread") {
          return current.filter((item) => item.id !== notificationId);
        }

        return current.map((item) => (item.id === notificationId ? updated : item));
      });
      setUnreadCount((current) => Math.max(current - 1, 0));
      setMessage("Уведомление отмечено как прочитанное.");
    } catch (err) {
      setError("Не удалось обновить уведомление.");
    } finally {
      setActionId(null);
    }
  };

  const handleDelete = async (notificationId) => {
    if (!window.confirm(`Удалить уведомление #${notificationId}?`)) {
      return;
    }

    setActionId(`delete-${notificationId}`);
    setError("");
    setMessage("");

    try {
      const currentItem = notifications.find((item) => item.id === notificationId);
      await deleteNotification(notificationId);
      setNotifications((current) => current.filter((item) => item.id !== notificationId));

      if (currentItem && !currentItem.read) {
        setUnreadCount((current) => Math.max(current - 1, 0));
      }

      setMessage("Уведомление удалено.");
    } catch (err) {
      setError("Не удалось удалить уведомление.");
    } finally {
      setActionId(null);
    }
  };

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Уведомления"
      subtitle="Следите за важными обновлениями по бронированиям и системным сообщениям."
    >
      <section className="card-like notification-summary">
        <div>
          <p className="eyebrow">Центр уведомлений</p>
          <h3>{unreadCount ? `${unreadCount} непрочитанных` : "Все уведомления прочитаны"}</h3>
          <p className="muted">
            Можно быстро переключаться между полной лентой и только непрочитанными сообщениями.
          </p>
        </div>

        <div className="filter-toggle" role="tablist" aria-label="Фильтр уведомлений">
          <button
            type="button"
            className={filter === "all" ? "secondary-button toggle-active" : "secondary-button"}
            onClick={() => setFilter("all")}
          >
            Все
          </button>
          <button
            type="button"
            className={
              filter === "unread" ? "secondary-button toggle-active" : "secondary-button"
            }
            onClick={() => setFilter("unread")}
          >
            Непрочитанные
          </button>
        </div>
      </section>

      {loading ? <p className="muted">Загружаем уведомления...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !notifications.length ? (
        <EmptyState
          title={filter === "unread" ? "Непрочитанных уведомлений нет" : "Уведомлений пока нет"}
          description="Когда система отправит новые сообщения, они появятся на этой странице."
        />
      ) : null}

      <section className="notification-list">
        {notifications.map((item) => (
          <article
            key={item.id}
            className={`notification-card notification-${(item.type || "INFO").toLowerCase()} ${
              item.read ? "notification-read" : "notification-unread"
            }`}
          >
            <div className="notification-head">
              <div>
                <p className="booking-label">{formatNotificationType(item.type)}</p>
                <h3>{item.title}</h3>
              </div>
              <p className="offer-meta">{formatApiDateTime(item.createdAt)}</p>
            </div>

            <p className="news-text">{item.message}</p>

            <div className="notification-actions">
              {!item.read ? (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => handleMarkRead(item.id)}
                  disabled={actionId === `read-${item.id}`}
                >
                  {actionId === `read-${item.id}` ? "Обновляем..." : "Отметить прочитанным"}
                </button>
              ) : (
                <span className="detail-chip">Прочитано</span>
              )}

              <button
                type="button"
                onClick={() => handleDelete(item.id)}
                disabled={actionId === `delete-${item.id}`}
              >
                {actionId === `delete-${item.id}` ? "Удаляем..." : "Удалить"}
              </button>
            </div>
          </article>
        ))}
      </section>
    </DashboardLayout>
  );
}
