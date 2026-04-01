import { NavLink } from "react-router-dom";
import { canManageContent } from "../../utils/permissions";

export default function DashboardLayout({
  title,
  subtitle,
  user,
  onLogout,
  children,
}) {
  return (
    <div className="dashboard-shell">
      <aside className="sidebar card">
        <div>
          <p className="eyebrow">Re-Center</p>
          <h1 className="sidebar-title">База отдыха</h1>
          <p className="muted sidebar-copy">
            Каталог домиков, новости, быстрые бронирования и личный кабинет в
            одном месте.
          </p>
        </div>

        <nav className="sidebar-nav">
          <NavLink
            to="/services"
            className={({ isActive }) => `nav-link ${isActive ? "nav-link-active" : ""}`}
          >
            Каталог услуг
          </NavLink>
          <NavLink
            to="/news"
            className={({ isActive }) => `nav-link ${isActive ? "nav-link-active" : ""}`}
          >
            Новости
          </NavLink>
          <NavLink
            to="/bookings"
            className={({ isActive }) => `nav-link ${isActive ? "nav-link-active" : ""}`}
          >
            Мои бронирования
          </NavLink>
          {canManageContent(user) ? (
            <NavLink
              to="/manage"
              className={({ isActive }) => `nav-link ${isActive ? "nav-link-active" : ""}`}
            >
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
