import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import { useEffect, useState } from "react";

import "./App.css";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import BookingList from "./components/bookings/BookingList";
import { getMe } from "./api/auth";

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");

  return token ? children : <Navigate to="/login" replace />;
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
      .then((res) => setUser(res.data))
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
        <Route
          path="/"
          element={<Navigate to={user ? "/bookings" : "/login"} replace />}
        />
        <Route
          path="/login"
          element={
            user ? <Navigate to="/bookings" replace /> : <Login onLogin={handleLogin} />
          }
        />
        <Route
          path="/register"
          element={user ? <Navigate to="/bookings" replace /> : <Register />}
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
          path="*"
          element={
            <div className="app-shell">
              <div className="card auth-card">
                <h2>Страница не найдена</h2>
                <p className="muted">Проверь адрес или вернись на страницу входа.</p>
              </div>
            </div>
          }
        />
      </Routes>
    </Router>
  );
}
