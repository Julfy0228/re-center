import { useEffect, useState } from "react";
import {
  createPromotion,
  deletePromotion,
  getPromotions,
  updatePromotion,
} from "../../api/promotions";
import { formatApiDateTime } from "../../utils/date";
import { isAdmin } from "../../utils/permissions";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";
import PromotionEditorForm from "./forms/PromotionEditorForm";
import {
  buildPromotionPayload,
  initialPromotionForm,
  normalizeError,
  toPromotionForm,
} from "./adminUtils";

function formatPeriod(startDate, endDate) {
  if (!startDate && !endDate) {
    return "Сроки акции не указаны";
  }

  return `${formatApiDateTime(startDate, "без даты")} - ${formatApiDateTime(
    endDate,
    "без даты"
  )}`;
}

export default function PromotionManager({ user }) {
  const [promotions, setPromotions] = useState([]);
  const [promotionForm, setPromotionForm] = useState(initialPromotionForm);
  const [editingPromotionId, setEditingPromotionId] = useState(null);
  const [editingPromotionForm, setEditingPromotionForm] = useState(initialPromotionForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getPromotions()
      .then((response) => setPromotions(response.data))
      .catch(() => setError("Не удалось загрузить акции для управления."))
      .finally(() => setLoading(false));
  }, []);

  const updateForm = (setter) => (key, value) => {
    setter((current) => ({ ...current, [key]: value }));
  };

  const updatePromotionField = updateForm(setPromotionForm);
  const updateEditingPromotionField = updateForm(setEditingPromotionForm);

  const submitPromotion = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await createPromotion(buildPromotionPayload(promotionForm));
      setPromotions((current) => [response.data, ...current]);
      setPromotionForm(initialPromotionForm);
      setMessage("Акция создана.");
    } catch (err) {
      setError(normalizeError(err, "Не удалось создать акцию."));
    } finally {
      setSaving(false);
    }
  };

  const beginEdit = (item) => {
    setEditingPromotionId(item.id);
    setEditingPromotionForm(toPromotionForm(item));
    setMessage("");
    setError("");
  };

  const cancelEdit = () => {
    setEditingPromotionId(null);
    setEditingPromotionForm(initialPromotionForm);
  };

  const saveEdit = async (event, promotionId) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await updatePromotion(
        promotionId,
        buildPromotionPayload(editingPromotionForm)
      );
      setPromotions((current) =>
        current.map((item) => (item.id === promotionId ? response.data : item))
      );
      setMessage(`Акция #${promotionId} обновлена.`);
      cancelEdit();
    } catch (err) {
      setError(normalizeError(err, "Не удалось обновить акцию."));
    } finally {
      setSaving(false);
    }
  };

  const removePromotion = async (promotionId) => {
    if (!window.confirm(`Удалить акцию #${promotionId}?`)) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await deletePromotion(promotionId);
      setPromotions((current) => current.filter((item) => item.id !== promotionId));
      setMessage(`Акция #${promotionId} удалена.`);
      if (editingPromotionId === promotionId) {
        cancelEdit();
      }
    } catch (err) {
      setError(normalizeError(err, "Не удалось удалить акцию."));
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Промо</p>
        <h3>Управление акциями</h3>
        <p className="muted">Добавляйте сезонные кампании и фиксируйте сроки их действия.</p>
      </div>

      {loading ? <p className="muted">Загружаем данные...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <PromotionEditorForm
        form={promotionForm}
        onChange={updatePromotionField}
        onSubmit={submitPromotion}
        submitLabel="Создать акцию"
        submitLoadingLabel="Сохраняем акцию..."
        isSubmitting={saving}
        disabled={saving}
      />

      <div className="manage-list">
        <h4>Существующие акции</h4>
        {!promotions.length ? (
          <EmptyState
            title="Пока нет акций"
            description="После создания они появятся в этом списке."
          />
        ) : null}

        {promotions.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Акция #{item.id}</p>
              <div className="manage-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => beginEdit(item)}
                >
                  Редактировать
                </button>
                {isAdmin(user) ? (
                  <button type="button" onClick={() => removePromotion(item.id)}>
                    Удалить
                  </button>
                ) : null}
              </div>
            </div>

            <h4>{item.title}</h4>
            <p className="muted">{item.description || "Описание пока не добавлено."}</p>
            <p className="booking-label">{formatPeriod(item.startDate, item.endDate)}</p>

            {editingPromotionId === item.id ? (
              <div className="inline-editor">
                <PromotionEditorForm
                  form={editingPromotionForm}
                  onChange={updateEditingPromotionField}
                  onSubmit={(event) => saveEdit(event, item.id)}
                  onCancel={cancelEdit}
                  submitLabel="Сохранить"
                  submitLoadingLabel="Сохраняем..."
                  isSubmitting={saving}
                  disabled={saving}
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
