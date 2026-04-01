import { useEffect, useState } from "react";
import { createNews } from "../../api/news";
import { createService, getCategories } from "../../api/catalog";
import { uploadImage } from "../../api/uploads";
import DashboardLayout from "../layout/DashboardLayout";

const initialServiceForm = {
  title: "",
  description: "",
  imageUrl: "",
  duration: "1",
  price: "",
  maxPeople: "1",
  categoryId: "",
};

const initialNewsForm = {
  title: "",
  content: "",
  imageUrl: "",
  status: "PUBLISHED",
};

function normalizeError(err, fallback) {
  if (typeof err.response?.data === "string") {
    return err.response.data;
  }

  return fallback;
}

export default function AdminPanel({ user, onLogout }) {
  const [categories, setCategories] = useState([]);
  const [serviceForm, setServiceForm] = useState(initialServiceForm);
  const [newsForm, setNewsForm] = useState(initialNewsForm);
  const [loading, setLoading] = useState(true);
  const [serviceSaving, setServiceSaving] = useState(false);
  const [newsSaving, setNewsSaving] = useState(false);
  const [serviceUploadLoading, setServiceUploadLoading] = useState(false);
  const [newsUploadLoading, setNewsUploadLoading] = useState(false);
  const [serviceMessage, setServiceMessage] = useState("");
  const [newsMessage, setNewsMessage] = useState("");
  const [serviceError, setServiceError] = useState("");
  const [newsError, setNewsError] = useState("");

  useEffect(() => {
    getCategories()
      .then((res) => {
        setCategories(res.data);
        if (res.data.length > 0) {
          setServiceForm((current) => ({
            ...current,
            categoryId: current.categoryId || String(res.data[0].id),
          }));
        }
      })
      .catch(() => setServiceError("Не удалось загрузить категории для создания услуги."))
      .finally(() => setLoading(false));
  }, []);

  const updateServiceField = (key, value) => {
    setServiceForm((current) => ({ ...current, [key]: value }));
  };

  const updateNewsField = (key, value) => {
    setNewsForm((current) => ({ ...current, [key]: value }));
  };

  const handleServiceImageUpload = async (event) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    setServiceError("");
    setServiceMessage("");
    setServiceUploadLoading(true);

    try {
      const response = await uploadImage(file);
      updateServiceField("imageUrl", response.data.url);
      setServiceMessage("Изображение услуги загружено.");
    } catch (err) {
      setServiceError(normalizeError(err, "Не удалось загрузить изображение услуги."));
    } finally {
      setServiceUploadLoading(false);
      event.target.value = "";
    }
  };

  const handleNewsImageUpload = async (event) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    setNewsError("");
    setNewsMessage("");
    setNewsUploadLoading(true);

    try {
      const response = await uploadImage(file);
      updateNewsField("imageUrl", response.data.url);
      setNewsMessage("Изображение новости загружено.");
    } catch (err) {
      setNewsError(normalizeError(err, "Не удалось загрузить изображение новости."));
    } finally {
      setNewsUploadLoading(false);
      event.target.value = "";
    }
  };

  const submitService = async (event) => {
    event.preventDefault();
    setServiceMessage("");
    setServiceError("");
    setServiceSaving(true);

    try {
      await createService({
        title: serviceForm.title,
        description: serviceForm.description,
        imageUrl: serviceForm.imageUrl || null,
        duration: Number(serviceForm.duration),
        price: serviceForm.price,
        maxPeople: Number(serviceForm.maxPeople),
        categoryId: Number(serviceForm.categoryId),
      });

      setServiceMessage("Услуга создана и уже доступна в каталоге.");
      setServiceForm((current) => ({
        ...initialServiceForm,
        categoryId: current.categoryId,
      }));
    } catch (err) {
      setServiceError(normalizeError(err, "Не удалось создать услугу."));
    } finally {
      setServiceSaving(false);
    }
  };

  const submitNews = async (event) => {
    event.preventDefault();
    setNewsMessage("");
    setNewsError("");
    setNewsSaving(true);

    try {
      await createNews({
        title: newsForm.title,
        content: newsForm.content,
        imageUrl: newsForm.imageUrl || null,
        status: newsForm.status,
      });

      setNewsMessage(
        newsForm.status === "PUBLISHED"
          ? "Новость опубликована."
          : "Новость сохранена как черновик."
      );
      setNewsForm(initialNewsForm);
    } catch (err) {
      setNewsError(normalizeError(err, "Не удалось создать новость."));
    } finally {
      setNewsSaving(false);
    }
  };

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Управление контентом"
      subtitle="Добавляйте услуги и новости прямо из фронтенда."
    >
      <section className="admin-intro">
        <div className="card-like admin-banner">
          <p className="eyebrow">Панель менеджера</p>
          <h3>Быстрое наполнение сайта</h3>
          <p className="muted">
            Здесь можно опубликовать новость, добавить услугу с изображением и сразу
            проверить результат в клиентской части.
          </p>
        </div>
      </section>

      <section className="admin-grid">
        <article className="admin-card">
          <div className="admin-card-header">
            <p className="eyebrow">Услуга</p>
            <h3>Новая услуга</h3>
            <p className="muted">Создайте карточку домика, бани, экскурсии или пакета отдыха.</p>
          </div>

          {loading && <p className="muted">Загружаем категории...</p>}
          {serviceMessage && <p className="alert alert-success">{serviceMessage}</p>}
          {serviceError && <p className="alert alert-error">{serviceError}</p>}

          <form className="admin-form" onSubmit={submitService}>
            <label>
              <span>Название</span>
              <input
                type="text"
                value={serviceForm.title}
                onChange={(e) => updateServiceField("title", e.target.value)}
                required
              />
            </label>

            <label>
              <span>Описание</span>
              <textarea
                rows="5"
                value={serviceForm.description}
                onChange={(e) => updateServiceField("description", e.target.value)}
              />
            </label>

            <div className="upload-block">
              <label>
                <span>Ссылка на изображение</span>
                <input
                  type="url"
                  placeholder="https://example.com/service.jpg"
                  value={serviceForm.imageUrl}
                  onChange={(e) => updateServiceField("imageUrl", e.target.value)}
                />
              </label>

              <label className="upload-field">
                <span>Или загрузите файл</span>
                <input
                  type="file"
                  accept="image/png,image/jpeg,image/webp,image/gif"
                  onChange={handleServiceImageUpload}
                  disabled={serviceUploadLoading}
                />
              </label>

              {serviceForm.imageUrl && (
                <img
                  className="upload-preview"
                  src={serviceForm.imageUrl}
                  alt="Предпросмотр услуги"
                />
              )}
            </div>

            <div className="admin-form-row">
              <label>
                <span>Длительность, дней</span>
                <input
                  type="number"
                  min="1"
                  value={serviceForm.duration}
                  onChange={(e) => updateServiceField("duration", e.target.value)}
                />
              </label>

              <label>
                <span>Максимум гостей</span>
                <input
                  type="number"
                  min="1"
                  value={serviceForm.maxPeople}
                  onChange={(e) => updateServiceField("maxPeople", e.target.value)}
                />
              </label>
            </div>

            <div className="admin-form-row">
              <label>
                <span>Цена</span>
                <input
                  type="number"
                  min="1"
                  step="0.01"
                  value={serviceForm.price}
                  onChange={(e) => updateServiceField("price", e.target.value)}
                  required
                />
              </label>

              <label>
                <span>Категория</span>
                <select
                  value={serviceForm.categoryId}
                  onChange={(e) => updateServiceField("categoryId", e.target.value)}
                  required
                >
                  <option value="" disabled>
                    Выберите категорию
                  </option>
                  {categories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
              </label>
            </div>

            <button
              type="submit"
              disabled={serviceSaving || serviceUploadLoading || loading || !categories.length}
            >
              {serviceSaving
                ? "Сохраняем услугу..."
                : serviceUploadLoading
                  ? "Загружаем изображение..."
                  : "Создать услугу"}
            </button>
          </form>
        </article>

        <article className="admin-card">
          <div className="admin-card-header">
            <p className="eyebrow">Новости</p>
            <h3>Новая новость</h3>
            <p className="muted">Публикуйте акции, объявления и сезонные предложения.</p>
          </div>

          {newsMessage && <p className="alert alert-success">{newsMessage}</p>}
          {newsError && <p className="alert alert-error">{newsError}</p>}

          <form className="admin-form" onSubmit={submitNews}>
            <label>
              <span>Заголовок</span>
              <input
                type="text"
                value={newsForm.title}
                onChange={(e) => updateNewsField("title", e.target.value)}
                required
              />
            </label>

            <label>
              <span>Текст новости</span>
              <textarea
                rows="8"
                value={newsForm.content}
                onChange={(e) => updateNewsField("content", e.target.value)}
                required
              />
            </label>

            <div className="upload-block">
              <label>
                <span>Ссылка на изображение</span>
                <input
                  type="url"
                  placeholder="https://example.com/news.jpg"
                  value={newsForm.imageUrl}
                  onChange={(e) => updateNewsField("imageUrl", e.target.value)}
                />
              </label>

              <label className="upload-field">
                <span>Или загрузите файл</span>
                <input
                  type="file"
                  accept="image/png,image/jpeg,image/webp,image/gif"
                  onChange={handleNewsImageUpload}
                  disabled={newsUploadLoading}
                />
              </label>

              {newsForm.imageUrl && (
                <img
                  className="upload-preview"
                  src={newsForm.imageUrl}
                  alt="Предпросмотр новости"
                />
              )}
            </div>

            <label>
              <span>Статус</span>
              <select
                value={newsForm.status}
                onChange={(e) => updateNewsField("status", e.target.value)}
              >
                <option value="PUBLISHED">Опубликовать сразу</option>
                <option value="DRAFT">Сохранить как черновик</option>
              </select>
            </label>

            <button type="submit" disabled={newsSaving || newsUploadLoading}>
              {newsSaving
                ? "Сохраняем новость..."
                : newsUploadLoading
                  ? "Загружаем изображение..."
                  : "Создать новость"}
            </button>
          </form>
        </article>
      </section>
    </DashboardLayout>
  );
}
