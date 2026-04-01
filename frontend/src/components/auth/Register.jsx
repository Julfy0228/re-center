import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../../api/auth";

const initialForm = {
  email: "",
  password: "",
  firstName: "",
  lastName: "",
  middleName: "",
  phoneNumber: "",
};

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const updateField = (key, value) => {
    setForm((prev) => ({ ...prev, [key]: value }));
  };

  const submit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");
    setLoading(true);

    try {
      await register({
        ...form,
        middleName: form.middleName || null,
        phoneNumber: form.phoneNumber || null,
      });

      setMessage("Регистрация прошла успешно. Теперь можно войти.");
      setForm(initialForm);
      setTimeout(() => navigate("/login", { replace: true }), 1200);
    } catch (err) {
      if (err.response?.data === "Email already in use") {
        setError("Пользователь с таким email уже существует.");
      } else if (err.response?.status === 400) {
        setError(
          "Проверьте данные. Пароль должен быть не короче 6 символов, телефон в формате +79991234567."
        );
      } else {
        setError("Не удалось зарегистрироваться. Проверьте, что backend запущен.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-shell">
      <div className="card auth-card">
        <div className="auth-header">
          <p className="eyebrow">Новый гость</p>
          <h1>Создание аккаунта</h1>
          <p className="muted">
            Заполните данные, чтобы получить доступ к личному кабинету.
          </p>
        </div>

        {message && <p className="alert alert-success">{message}</p>}
        {error && <p className="alert alert-error">{error}</p>}

        <form className="auth-form" onSubmit={submit}>
          <label>
            <span>Email</span>
            <input
              type="email"
              placeholder="guest@example.com"
              value={form.email}
              onChange={(e) => updateField("email", e.target.value)}
              required
            />
          </label>

          <label>
            <span>Пароль</span>
            <input
              type="password"
              placeholder="Не менее 6 символов"
              value={form.password}
              onChange={(e) => updateField("password", e.target.value)}
              minLength={6}
              required
            />
          </label>

          <label>
            <span>Имя</span>
            <input
              type="text"
              placeholder="Иван"
              value={form.firstName}
              onChange={(e) => updateField("firstName", e.target.value)}
            />
          </label>

          <label>
            <span>Фамилия</span>
            <input
              type="text"
              placeholder="Иванов"
              value={form.lastName}
              onChange={(e) => updateField("lastName", e.target.value)}
            />
          </label>

          <label>
            <span>Отчество</span>
            <input
              type="text"
              placeholder="Иванович"
              value={form.middleName}
              onChange={(e) => updateField("middleName", e.target.value)}
            />
          </label>

          <label>
            <span>Телефон</span>
            <input
              type="tel"
              placeholder="+79991234567"
              value={form.phoneNumber}
              onChange={(e) => updateField("phoneNumber", e.target.value)}
            />
          </label>

          <button type="submit" disabled={loading}>
            {loading ? "Создаём аккаунт..." : "Создать аккаунт"}
          </button>
        </form>

        <p className="muted auth-footer">
          Уже есть аккаунт? <Link to="/login">Войти</Link>
        </p>
      </div>
    </div>
  );
}
