import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../../api/auth";
import AuthLayout from "../ui/AuthLayout";
import AlertMessage from "../ui/AlertMessage";

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
    setForm((current) => ({ ...current, [key]: value }));
  };

  const submit = async (event) => {
    event.preventDefault();
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
    <AuthLayout
      eyebrow="Новый гость"
      title="Создание аккаунта"
      description="Заполните данные, чтобы получить доступ к личному кабинету."
      footer={
        <>
          Уже есть аккаунт? <Link to="/login">Войти</Link>
        </>
      }
    >
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <form className="auth-form" onSubmit={submit}>
        <label>
          <span>Email</span>
          <input
            type="email"
            placeholder="guest@example.com"
            value={form.email}
            onChange={(event) => updateField("email", event.target.value)}
            required
          />
        </label>

        <label>
          <span>Пароль</span>
          <input
            type="password"
            placeholder="Не менее 6 символов"
            value={form.password}
            onChange={(event) => updateField("password", event.target.value)}
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
            onChange={(event) => updateField("firstName", event.target.value)}
          />
        </label>

        <label>
          <span>Фамилия</span>
          <input
            type="text"
            placeholder="Иванов"
            value={form.lastName}
            onChange={(event) => updateField("lastName", event.target.value)}
          />
        </label>

        <label>
          <span>Отчество</span>
          <input
            type="text"
            placeholder="Иванович"
            value={form.middleName}
            onChange={(event) => updateField("middleName", event.target.value)}
          />
        </label>

        <label>
          <span>Телефон</span>
          <input
            type="tel"
            placeholder="+79991234567"
            value={form.phoneNumber}
            onChange={(event) => updateField("phoneNumber", event.target.value)}
          />
        </label>

        <button type="submit" disabled={loading}>
          {loading ? "Создаём аккаунт..." : "Создать аккаунт"}
        </button>
      </form>
    </AuthLayout>
  );
}
