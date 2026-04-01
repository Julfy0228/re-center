import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { getUnreadCount } from "../../api/notifications";
import { canManageContent } from "../../utils/permissions";

function getLinkClass({ isActive }) {
  return `nav-link ${isActive ? "nav-link-active" : ""}`;
}

export default function DashboardLayout({
  title,
  subtitle,
  user,
  onLogout,
  children,
}) {
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    let cancelled = false;

    if (!user) {
      setUnreadCount(0);
      return undefined;
    }

    getUnreadCount()
      .then((response) => {
        if (!cancelled) {
          setUnreadCount(Number(response.data) || 0);
        }
      })
      .catch(() => {
        if (!cancelled) {
          setUnreadCount(0);
        }
      });

    return () => {
      cancelled = true;
    };
  }, [user]);

  return (
    <div className="dashboard-shell">
      <aside className="sidebar card">
        <div>
          <p className="eyebrow">Re-Center</p>
          <h1 className="sidebar-title">База отдыха</h1>
          <p className="muted sidebar-copy">
            Каталог услуг, акции, новости, уведомления и личный кабинет в одном месте.
          </p>
        </div>

        <nav className="sidebar-nav">
          <NavLink to="/services" className={getLinkClass}>
            Каталог услуг
          </NavLink>
          <NavLink to="/offers" className={getLinkClass}>
            Акции и скидки
          </NavLink>
          <NavLink to="/news" className={getLinkClass}>
            Новости
          </NavLink>
          <NavLink to="/bookings" className={getLinkClass}>
            Мои бронирования
          </NavLink>
          <NavLink to="/notifications" className={getLinkClass}>
            <span className="nav-link-content">
              <span>Уведомления</span>
              {unreadCount ? <span className="nav-badge">{unreadCount}</span> : null}
            </span>
          </NavLink>
          <NavLink to="/profile" className={getLinkClass}>
            Профиль
          </NavLink>
          {canManageContent(user) ? (
            <NavLink to="/manage" className={getLinkClass}>
              Управление
            </NavLink>
          ) : null}
        </nav>

        <div className="sidebar-profile">
          <div className="profile-meta">
            <p className="profile-name">{user?.email || "Гость"}</p>
            {user?.role ? <p className="profile-role">{user.role}</p> : null}
          </div>
          <button type="button" className="secondary-button" onClick={onLogout}>
            Выйти
          </button>
        </div>
      </aside>

      <main className="content-panel card">
        <header className="page-header">
          <div>
            <p className="eyebrow">Личный кабинет</p>
            <h2>{title}</h2>
            <p className="muted">{subtitle}</p>
          </div>
        </header>
        {children}
      </main>
    </div>
  );
}
