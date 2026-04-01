import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login, getMe } from "../../api/auth";

export default function Login({ onLogin }) {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await login({ email, password });
      localStorage.setItem("token", res.data.token);

      const me = await getMe();
      onLogin?.(me.data);
      navigate("/bookings", { replace: true });
    } catch (err) {
      localStorage.removeItem("token");
      setError(
        err.response?.status === 401
          ? "Неверный email или пароль."
          : "Не удалось войти. Проверь, что backend запущен на порту 8080."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-shell">
      <div className="card auth-card">
        <div className="auth-header">
          <p className="eyebrow">База отдыха</p>
          <h1>Вход в личный кабинет</h1>
          <p className="muted">Войдите, чтобы смотреть свои бронирования и управлять ими.</p>
        </div>

        {error && <p className="alert alert-error">{error}</p>}

        <form className="auth-form" onSubmit={submit}>
          <label>
            <span>Email</span>
            <input
              type="email"
              placeholder="guest@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </label>

          <label>
            <span>Пароль</span>
            <input
              type="password"
              placeholder="Введите пароль"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>

          <button type="submit" disabled={loading}>
            {loading ? "Входим..." : "Войти"}
          </button>
        </form>

        <p className="muted auth-footer">
          Нет аккаунта? <Link to="/register">Зарегистрироваться</Link>
        </p>
      </div>
    </div>
  );
}
