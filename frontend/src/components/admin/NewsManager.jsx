import { useEffect, useState } from "react";
import { createNews, deleteNews, getAllNews, updateNews } from "../../api/news";
import { uploadImage } from "../../api/uploads";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";
import NewsEditorForm from "./forms/NewsEditorForm";
import {
  buildNewsPayload,
  formatNewsStatus,
  initialNewsForm,
  normalizeError,
  toNewsForm,
} from "./adminUtils";

export default function NewsManager() {
  const [newsItems, setNewsItems] = useState([]);
  const [newsForm, setNewsForm] = useState(initialNewsForm);
  const [editingNewsId, setEditingNewsId] = useState(null);
  const [editingNewsForm, setEditingNewsForm] = useState(initialNewsForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploadLoading, setUploadLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getAllNews()
      .then((response) => setNewsItems(response.data))
      .catch(() => setError("Не удалось загрузить новости для управления."))
      .finally(() => setLoading(false));
  }, []);

  const updateForm = (setter) => (key, value) => {
    setter((current) => ({ ...current, [key]: value }));
  };

  const updateNewsField = updateForm(setNewsForm);
  const updateEditingNewsField = updateForm(setEditingNewsForm);

  const handleImageUpload = async (event, isEditing = false) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    setError("");
    setMessage("");
    setUploadLoading(true);

    try {
      const response = await uploadImage(file);
      if (isEditing) {
        updateEditingNewsField("imageUrl", response.data.url);
      } else {
        updateNewsField("imageUrl", response.data.url);
      }
      setMessage("Изображение новости загружено.");
    } catch (err) {
      setError(normalizeError(err, "Не удалось загрузить изображение новости."));
    } finally {
      setUploadLoading(false);
      event.target.value = "";
    }
  };

  const submitNews = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await createNews(buildNewsPayload(newsForm));
      setNewsItems((current) => [response.data, ...current]);
      setMessage(
        newsForm.status === "PUBLISHED"
          ? "Новость опубликована."
          : "Новость сохранена как черновик."
      );
      setNewsForm(initialNewsForm);
    } catch (err) {
      setError(normalizeError(err, "Не удалось создать новость."));
    } finally {
      setSaving(false);
    }
  };

  const beginEdit = (item) => {
    setEditingNewsId(item.id);
    setEditingNewsForm(toNewsForm(item));
    setMessage("");
    setError("");
  };

  const cancelEdit = () => {
    setEditingNewsId(null);
    setEditingNewsForm(initialNewsForm);
  };

  const saveEdit = async (event, newsId) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await updateNews(newsId, buildNewsPayload(editingNewsForm));
      setNewsItems((current) =>
        current.map((item) => (item.id === newsId ? response.data : item))
      );
      setMessage(`Новость #${newsId} обновлена.`);
      cancelEdit();
    } catch (err) {
      setError(normalizeError(err, "Не удалось обновить новость."));
    } finally {
      setSaving(false);
    }
  };

  const removeNews = async (newsId) => {
    if (!window.confirm(`Удалить новость #${newsId}?`)) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await deleteNews(newsId);
      setNewsItems((current) => current.filter((item) => item.id !== newsId));
      setMessage(`Новость #${newsId} удалена.`);
      if (editingNewsId === newsId) {
        cancelEdit();
      }
    } catch (err) {
      setError(normalizeError(err, "Не удалось удалить новость."));
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Новость</p>
        <h3>Управление новостями</h3>
        <p className="muted">Публикуйте акции, объявления и сезонные предложения.</p>
      </div>

      {loading ? <p className="muted">Загружаем данные...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <NewsEditorForm
        form={newsForm}
        onChange={updateNewsField}
        onSubmit={submitNews}
        onImageUpload={(event) => handleImageUpload(event, false)}
        submitLabel="Создать новость"
        submitLoadingLabel="Сохраняем новость..."
        isSubmitting={saving}
        disabled={saving || uploadLoading}
        uploadLoading={uploadLoading}
      />

      <div className="manage-list">
        <h4>Существующие новости</h4>
        {!newsItems.length ? (
          <EmptyState
            title="Пока нет новостей"
            description="После публикации они появятся в этом списке."
          />
        ) : null}

        {newsItems.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Новость #{item.id}</p>
              <div className="manage-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => beginEdit(item)}
                >
                  Редактировать
                </button>
                <button type="button" onClick={() => removeNews(item.id)}>
                  Удалить
                </button>
              </div>
            </div>

            <h4>{item.title}</h4>
            <p className="muted">
              {item.content?.length > 180
                ? `${item.content.slice(0, 180).trimEnd()}...`
                : item.content}
            </p>
            <p className="booking-label">{formatNewsStatus(item.status)}</p>

            {editingNewsId === item.id ? (
              <div className="inline-editor">
                <NewsEditorForm
                  form={editingNewsForm}
                  onChange={updateEditingNewsField}
                  onSubmit={(event) => saveEdit(event, item.id)}
                  onCancel={cancelEdit}
                  onImageUpload={(event) => handleImageUpload(event, true)}
                  submitLabel="Сохранить"
                  submitLoadingLabel="Сохраняем..."
                  isSubmitting={saving}
                  disabled={saving || uploadLoading}
                  uploadLoading={uploadLoading}
                  showCancel
                />
              </div>
            ) : null}
          </article>
        ))}
      </div>
    </article>
  );
}
