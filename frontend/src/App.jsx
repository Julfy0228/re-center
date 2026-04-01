import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import { useEffect, useState } from "react";

import "./App.css";
import { getMe } from "./api/auth";
import AdminPanel from "./components/admin/AdminPanel";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import BookingList from "./components/bookings/BookingList";
import NewsFeed from "./components/news/NewsFeed";
import ServiceCatalog from "./components/services/ServiceCatalog";
import { canManageContent } from "./utils/permissions";

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");

  return token ? children : <Navigate to="/login" replace />;
}

function RoleRoute({ user, children }) {
  return canManageContent(user) ? children : <Navigate to="/services" replace />;
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
          path="/services"
          element={
            <ProtectedRoute>
              <ServiceCatalog user={user} onLogout={handleLogout} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/news"
          element={
            <ProtectedRoute>
              <NewsFeed user={user} onLogout={handleLogout} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/bookings"
          element={
            <ProtectedRoute>
              <BookingList user={user} onLogout={handleLogout} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/manage"
          element={
            <ProtectedRoute>
              <RoleRoute user={user}>
                <AdminPanel user={user} onLogout={handleLogout} />
              </RoleRoute>
            </ProtectedRoute>
          }
        />
        <Route
          path="*"
          element={
            <div className="app-shell">
              <div className="card auth-card">
                <h2>Страница не найдена</h2>
                <p className="muted">
                  Проверьте адрес или вернитесь в каталог услуг.
                </p>
              </div>
            </div>
          }
        />
      </Routes>
    </Router>
  );
}
