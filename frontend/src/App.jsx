import { useEffect, useMemo, useRef, useState } from "react";
import {
  BrowserRouter as Router,
  Navigate,
  NavLink,
  Outlet,
  Route,
  Routes,
  useLocation,
} from "react-router-dom";
import "./App.css";
import { getMe } from "./api/auth";
import { getUnreadCount } from "./api/notifications";
import AdminPanel from "./components/admin/AdminPanel";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import BookingList from "./components/bookings/BookingList";
import NewsFeed from "./components/news/NewsFeed";
import NotificationsPage from "./components/notifications/NotificationsPage";
import OffersPage from "./components/offers/OffersPage";
import ProfilePage from "./components/profile/ProfilePage";
import ServiceCatalog from "./components/services/ServiceCatalog";
import ServiceDetailsPage from "./components/services/ServiceDetailsPage";
import { canManageContent } from "./utils/permissions";

function getLinkClass({ isActive }) {
  return `nav-link ${isActive ? "nav-link-active" : ""}`;
}

function getPageTitle(pathname) {
  if (pathname.startsWith("/services/")) return "Услуга";
  if (pathname === "/services") return "Каталог";
  if (pathname === "/offers") return "Акции и скидки";
  if (pathname === "/news") return "Новости";
  if (pathname === "/bookings") return "Бронирования";
  if (pathname === "/notifications") return "Уведомления";
  if (pathname === "/profile") return "Профиль";
  if (pathname === "/manage") return "Управление";
  if (pathname === "/login") return "Вход";
  if (pathname === "/register") return "Регистрация";
  return "Главная";
}

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" replace />;
}

function RoleRoute({ user, children }) {
  return canManageContent(user) ? children : <Navigate to="/services" replace />;
}

function NavigationLinks({ unreadCount, user, onNavigate, includeProfileLink = false }) {
  return (
    <>
      <NavLink to="/services" className={getLinkClass} onClick={onNavigate}>
        Каталог
      </NavLink>
      <NavLink to="/offers" className={getLinkClass} onClick={onNavigate}>
        Акции и скидки
      </NavLink>
      <NavLink to="/news" className={getLinkClass} onClick={onNavigate}>
        Новости
      </NavLink>
      <NavLink to="/bookings" className={getLinkClass} onClick={onNavigate}>
        Бронирования
      </NavLink>
      <NavLink to="/notifications" className={getLinkClass} onClick={onNavigate}>
        <span className="nav-link-content">
          <span>Уведомления</span>
          {unreadCount ? <span className="nav-badge">{unreadCount}</span> : null}
        </span>
      </NavLink>
      {includeProfileLink ? (
        <NavLink to="/profile" className={getLinkClass} onClick={onNavigate}>
          Профиль
        </NavLink>
      ) : null}
      {canManageContent(user) ? (
        <NavLink to="/manage" className={getLinkClass} onClick={onNavigate}>
          Управление
        </NavLink>
      ) : null}
    </>
  );
}

function ProfilePopover({ user, onLogout, onClose }) {
  return (
    <div className="profile-popover card">
      <div className="profile-popover-head">
        <p className="profile-name">{user?.email || "Гость"}</p>
        {user?.role ? <p className="profile-role">{user.role}</p> : null}
      </div>

      <div className="profile-popover-actions">
        <NavLink to="/profile" className="popover-link" onClick={onClose}>
          Открыть профиль
        </NavLink>
        <button type="button" className="secondary-button popover-button" onClick={onLogout}>
          Выйти
        </button>
      </div>
    </div>
  );
}

function AppShell({ user, onLogout }) {
  const location = useLocation();
  const [unreadCount, setUnreadCount] = useState(0);
  const [menuOpen, setMenuOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);
  const profileRef = useRef(null);

  const pageTitle = useMemo(() => getPageTitle(location.pathname), [location.pathname]);

  useEffect(() => {
    document.title = `Re-Center - ${pageTitle}`;
  }, [pageTitle]);

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
  }, [user, location.pathname]);

  useEffect(() => {
    setMenuOpen(false);
    setProfileOpen(false);
  }, [location.pathname]);

  useEffect(() => {
    if (!menuOpen) {
      document.body.style.overflow = "";
      document.body.style.touchAction = "";
      return undefined;
    }

    document.body.style.overflow = "hidden";
    document.body.style.touchAction = "none";

    return () => {
      document.body.style.overflow = "";
      document.body.style.touchAction = "";
    };
  }, [menuOpen]);

  useEffect(() => {
    if (!profileOpen) {
      return undefined;
    }

    const handlePointerDown = (event) => {
      if (profileRef.current && !profileRef.current.contains(event.target)) {
        setProfileOpen(false);
      }
    };

    document.addEventListener("mousedown", handlePointerDown);
    return () => document.removeEventListener("mousedown", handlePointerDown);
  }, [profileOpen]);

  const closeMenu = () => {
    setMenuOpen(false);
  };

  return (
    <div className="dashboard-shell">
      <header className="dashboard-topbar card">
        <div className="topbar-left">
          <NavLink to="/services" className="topbar-logo topbar-logo-desktop" aria-label="Re-Center">
            <img src="/logo-small.svg" alt="Re-Center" className="topbar-logo-image" />
          </NavLink>

          <button
            type="button"
            className="secondary-button menu-trigger menu-trigger-icon"
            onClick={() => setMenuOpen(true)}
            aria-label="Открыть меню"
          >
            <span />
            <span />
            <span />
          </button>

          <nav className="topbar-nav" aria-label="Основная навигация">
            <NavigationLinks unreadCount={unreadCount} user={user} onNavigate={closeMenu} />
          </nav>
        </div>

        <NavLink
          to="/services"
          className="topbar-logo topbar-logo-mobile"
          aria-label="Re-Center"
        >
          <img src="/logo-small.svg" alt="Re-Center" className="topbar-logo-image" />
        </NavLink>

        <div className="topbar-actions">
          <div className="profile-menu" ref={profileRef}>
            <button
              type="button"
              className={`secondary-button profile-trigger ${profileOpen ? "profile-trigger-open" : ""}`}
              aria-label="Открыть меню профиля"
              onClick={() => setProfileOpen((current) => !current)}
            >
              <span className="profile-trigger-icon" />
            </button>

            {profileOpen ? (
              <ProfilePopover
                user={user}
                onLogout={onLogout}
                onClose={() => setProfileOpen(false)}
              />
            ) : null}
          </div>
        </div>
      </header>

      <div className="dashboard-main">
        <Outlet />
      </div>

      <div
        className={`mobile-drawer-backdrop ${menuOpen ? "mobile-drawer-backdrop-open" : ""}`}
        onClick={closeMenu}
      >
        <aside
          className={`mobile-drawer card ${menuOpen ? "mobile-drawer-open" : ""}`}
          onClick={(event) => event.stopPropagation()}
        >
          <div className="mobile-drawer-header">
            <div>
              <img src="/logo-small.svg" alt="Re-Center" className="mobile-drawer-logo" />
              <h2>Навигация</h2>
            </div>
            <button type="button" className="secondary-button" onClick={closeMenu}>
              Закрыть
            </button>
          </div>

          <nav className="mobile-drawer-nav" aria-label="Мобильная навигация">
            <NavigationLinks
              unreadCount={unreadCount}
              user={user}
              onNavigate={closeMenu}
              includeProfileLink
            />
          </nav>

          <div className="mobile-drawer-profile">
            <p className="profile-name">{user?.email || "Гость"}</p>
            {user?.role ? <p className="profile-role">{user.role}</p> : null}
            <button type="button" className="secondary-button" onClick={onLogout}>
              Выйти
            </button>
          </div>
        </aside>
      </div>
    </div>
  );
}

function NotFoundPage() {
  useEffect(() => {
    document.title = "Re-Center - Страница не найдена";
  }, []);

  return (
    <div className="app-shell">
      <div className="card auth-card">
        <h2>Страница не найдена</h2>
        <p className="muted">
          Проверьте адрес или вернитесь в каталог услуг через навигацию.
        </p>
      </div>
    </div>
  );
}

export default function App() {
  const [user, setUser] = useState(null);
  const [authChecked, setAuthChecked] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      setAuthChecked(true);
      return;
    }

    getMe()
      .then((response) => setUser(response.data))
      .catch(() => {
        localStorage.removeItem("token");
        setUser(null);
      })
      .finally(() => setAuthChecked(true));
  }, []);

  const handleLogin = (nextUser) => {
    setUser(nextUser);
  };

  const handleUserUpdate = (nextUser) => {
    setUser(nextUser);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  if (!authChecked) {
    return (
      <div className="app-shell">
        <div className="card auth-card">
          <h1>Re-Center</h1>
          <p className="muted">Проверяем авторизацию...</p>
        </div>
      </div>
    );
  }

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to={user ? "/services" : "/login"} replace />} />
        <Route
          path="/login"
          element={user ? <Navigate to="/services" replace /> : <Login onLogin={handleLogin} />}
        />
        <Route
          path="/register"
          element={user ? <Navigate to="/services" replace /> : <Register />}
        />

        <Route
          element={
            <ProtectedRoute>
              <AppShell user={user} onLogout={handleLogout} />
            </ProtectedRoute>
          }
        >
          <Route
            path="/services"
            element={<ServiceCatalog user={user} onLogout={handleLogout} />}
          />
          <Route
            path="/services/:serviceId"
            element={<ServiceDetailsPage user={user} onLogout={handleLogout} />}
          />
          <Route path="/offers" element={<OffersPage />} />
          <Route path="/news" element={<NewsFeed user={user} onLogout={handleLogout} />} />
          <Route
            path="/bookings"
            element={<BookingList user={user} onLogout={handleLogout} />}
          />
          <Route
            path="/notifications"
            element={<NotificationsPage user={user} onLogout={handleLogout} />}
          />
          <Route
            path="/profile"
            element={<ProfilePage user={user} onUserUpdate={handleUserUpdate} />}
          />
          <Route
            path="/manage"
            element={
              <RoleRoute user={user}>
                <AdminPanel user={user} onLogout={handleLogout} />
              </RoleRoute>
            }
          />
        </Route>

        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Router>
  );
}
