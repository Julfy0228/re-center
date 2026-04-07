import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getMe, login } from "../../api/auth";
import AuthLayout from "../ui/AuthLayout";
import AlertMessage from "../ui/AlertMessage";

export default function Login({ onLogin }) {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await login({ email, password });
      localStorage.setItem("token", response.data.token);

      const me = await getMe();
      onLogin?.(me.data);
      navigate("/services", { replace: true });
    } catch (err) {
      localStorage.removeItem("token");
      setError(
        err.response?.status === 401
          ? "Неверный email или пароль."
          : "Не удалось войти. Проверьте, что backend запущен на порту 8080."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout
      eyebrow="База отдыха"
      title="Вход в личный кабинет"
      description="Войдите, чтобы смотреть свои бронирования, новости и доступные услуги."
      footer={
        <>
          Нет аккаунта? <Link to="/register">Зарегистрироваться</Link>
        </>
      }
    >
      <AlertMessage type="error">{error}</AlertMessage>

      <form className="auth-form" onSubmit={submit}>
        <label>
          <span>Email</span>
          <input
            type="email"
            placeholder="guest@example.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </label>

        <label>
          <span>Пароль</span>
          <input
            type="password"
            placeholder="Введите пароль"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </label>

        <button type="submit" disabled={loading}>
          {loading ? "Входим..." : "Войти"}
        </button>
      </form>
    </AuthLayout>
  );
}
