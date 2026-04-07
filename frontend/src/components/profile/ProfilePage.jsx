import { useEffect, useState } from "react";
import { createActivity, getMyActivities } from "../../api/activities";
import { getMyProfile, updateUser } from "../../api/users";
import { formatApiDateTime } from "../../utils/date";
import ActivityTimeline from "./ActivityTimeline";
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

function formatRole(role) {
  if (role === "ADMIN") {
    return "Администратор";
  }

  if (role === "MANAGER") {
    return "Менеджер";
  }

  return "Гость";
}

export default function ProfilePage({ user, onUserUpdate }) {
  const [profileId, setProfileId] = useState(user?.id || null);
  const [form, setForm] = useState(() => mapUserToForm(user));
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [activityError, setActivityError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    Promise.allSettled([getMyProfile(), getMyActivities()]).then(
      ([profileRes, activitiesRes]) => {
        if (profileRes.status === "fulfilled") {
          setProfileId(profileRes.value.data.id);
          setForm(mapUserToForm(profileRes.value.data));
          onUserUpdate?.(profileRes.value.data);
        } else {
          setError("Не удалось загрузить профиль.");
        }

        if (activitiesRes.status === "fulfilled") {
          setActivities(activitiesRes.value.data);
        } else {
          setActivityError("Не удалось загрузить историю действий.");
        }

        setLoading(false);
      }
    );
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

      try {
        const activityResponse = await createActivity({
          userId: response.data.id,
          type: "UPDATE_PROFILE",
          details: "Обновлены контактные данные в профиле",
        });

        setActivities((current) => [activityResponse.data, ...current]);
      } catch {
        setActivityError(
          "Профиль обновлён, но не удалось записать событие в историю."
        );
      }
    } catch {
      setError("Не удалось сохранить изменения профиля.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <DashboardLayout
      title="Профиль"
      subtitle="Здесь можно обновить контактные данные и посмотреть недавнюю активность."
    >
      <div className="profile-grid">
        <section className="card-like profile-summary profile-summary-card">
          <div className="profile-summary-head">
            <div>
              <p className="eyebrow">Аккаунт</p>
              <h3>{form.email || "Пользователь"}</h3>
            </div>
            {form.role ? <span className="detail-chip">{formatRole(form.role)}</span> : null}
          </div>

          <div className="profile-account-list">
            <div className="profile-account-item">
              <span className="profile-account-label">Имя</span>
              <strong className="profile-account-value">
                {[form.firstName, form.lastName].filter(Boolean).join(" ") || "Не указано"}
              </strong>
            </div>
            <div className="profile-account-item">
              <span className="profile-account-label">Телефон</span>
              <strong className="profile-account-value">
                {form.phoneNumber || "Не указан"}
              </strong>
            </div>
            <div className="profile-account-item">
              <span className="profile-account-label">С нами с</span>
              <strong className="profile-account-value">
                {form.createdAt ? formatApiDateTime(form.createdAt) : "Недавно"}
              </strong>
            </div>
          </div>

          <p className="muted">
            Эти данные используются в личном кабинете, уведомлениях и при работе с
            бронированиями.
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

      <AlertMessage type="error">{activityError}</AlertMessage>
      <ActivityTimeline items={activities} />
    </DashboardLayout>
  );
}
