import { useEffect, useState } from "react";
import { getMyProfile, updateUser } from "../../api/users";
import { formatApiDateTime } from "../../utils/date";
import DashboardLayout from "../layout/DashboardLayout";
import AlertMessage from "../ui/AlertMessage";

function mapUserToForm(profile) {
  return {
    email: profile?.email || "",
    firstName: profile?.firstName || "",
    lastName: profile?.lastName || "",
    middleName: profile?.middleName || "",
    phoneNumber: profile?.phoneNumber || "",
    role: profile?.role || "",
    createdAt: profile?.createdAt || null,
  };
}

export default function ProfilePage({ user, onLogout, onUserUpdate }) {
  const [profileId, setProfileId] = useState(user?.id || null);
  const [form, setForm] = useState(() => mapUserToForm(user));
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    getMyProfile()
      .then((response) => {
        setProfileId(response.data.id);
        setForm(mapUserToForm(response.data));
        onUserUpdate?.(response.data);
      })
      .catch(() => setError("Не удалось загрузить профиль."))
      .finally(() => setLoading(false));
  }, [onUserUpdate]);

  const handleChange = (key, value) => {
    setForm((current) => ({ ...current, [key]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setMessage("");
    setSaving(true);

    try {
      const response = await updateUser(profileId, {
        firstName: form.firstName,
        lastName: form.lastName,
        middleName: form.middleName,
        phoneNumber: form.phoneNumber,
      });

      setProfileId(response.data.id);
      setForm(mapUserToForm(response.data));
      onUserUpdate?.(response.data);
      setMessage("Профиль обновлён.");
    } catch (err) {
      setError("Не удалось сохранить изменения профиля.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Профиль"
      subtitle="Здесь можно обновить контактные данные, чтобы менеджеру было проще связаться с вами."
    >
      <div className="profile-grid">
        <section className="card-like profile-summary">
          <p className="eyebrow">Аккаунт</p>
          <h3>{form.email || "Пользователь"}</h3>
          <div className="detail-chips">
            {form.role ? <span className="detail-chip">{form.role}</span> : null}
            {form.createdAt ? (
              <span className="detail-chip">С нами с {formatApiDateTime(form.createdAt)}</span>
            ) : null}
          </div>
          <p className="muted">
            Данные из этого раздела используются в личном кабинете и при работе с бронированиями.
          </p>
        </section>

        <section className="card-like profile-editor">
          <p className="eyebrow">Редактирование</p>
          <h3>Контактная информация</h3>

          {loading ? <p className="muted">Загружаем профиль...</p> : null}
          <AlertMessage type="success">{message}</AlertMessage>
          <AlertMessage type="error">{error}</AlertMessage>

          <form className="admin-form" onSubmit={handleSubmit}>
            <label>
              <span>Email</span>
              <input type="email" value={form.email} disabled />
            </label>

            <div className="admin-form-row">
              <label>
                <span>Имя</span>
                <input
                  type="text"
                  value={form.firstName}
                  onChange={(event) => handleChange("firstName", event.target.value)}
                />
              </label>

              <label>
                <span>Фамилия</span>
                <input
                  type="text"
                  value={form.lastName}
                  onChange={(event) => handleChange("lastName", event.target.value)}
                />
              </label>
            </div>

            <div className="admin-form-row">
              <label>
                <span>Отчество</span>
                <input
                  type="text"
                  value={form.middleName}
                  onChange={(event) => handleChange("middleName", event.target.value)}
                />
              </label>

              <label>
                <span>Телефон</span>
                <input
                  type="tel"
                  value={form.phoneNumber}
                  onChange={(event) => handleChange("phoneNumber", event.target.value)}
                />
              </label>
            </div>

            <div className="manage-actions">
              <button type="submit" disabled={saving || loading || !profileId}>
                {saving ? "Сохраняем профиль..." : "Сохранить изменения"}
              </button>
            </div>
          </form>
        </section>
      </div>
    </DashboardLayout>
  );
}
