import { useEffect, useState } from "react";
import {
  createDiscount,
  deleteDiscount,
  getDiscounts,
  updateDiscount,
} from "../../api/discounts";
import { formatApiDateTime } from "../../utils/date";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";
import DiscountEditorForm from "./forms/DiscountEditorForm";
import {
  buildDiscountPayload,
  formatDiscountType,
  initialDiscountForm,
  normalizeError,
  toDiscountForm,
} from "./adminUtils";

function formatAmount(item) {
  if (item.type === "PERCENT") {
    return `${item.amount}%`;
  }

  return `${new Intl.NumberFormat("ru-RU").format(item.amount || 0)} ₽`;
}

function formatPeriod(startDate, endDate) {
  if (!startDate && !endDate) {
    return "Сроки скидки не указаны";
  }

  return `${formatApiDateTime(startDate, "без даты")} - ${formatApiDateTime(
    endDate,
    "без даты"
  )}`;
}

export default function DiscountManager() {
  const [discounts, setDiscounts] = useState([]);
  const [discountForm, setDiscountForm] = useState(initialDiscountForm);
  const [editingDiscountId, setEditingDiscountId] = useState(null);
  const [editingDiscountForm, setEditingDiscountForm] = useState(initialDiscountForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getDiscounts()
      .then((response) => setDiscounts(response.data))
      .catch(() => setError("Не удалось загрузить скидки для управления."))
      .finally(() => setLoading(false));
  }, []);

  const updateForm = (setter) => (key, value) => {
    setter((current) => ({ ...current, [key]: value }));
  };

  const updateDiscountField = updateForm(setDiscountForm);
  const updateEditingDiscountField = updateForm(setEditingDiscountForm);

  const submitDiscount = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await createDiscount(buildDiscountPayload(discountForm));
      setDiscounts((current) => [response.data, ...current]);
      setDiscountForm(initialDiscountForm);
      setMessage("Скидка создана.");
    } catch (err) {
      setError(normalizeError(err, "Не удалось создать скидку."));
    } finally {
      setSaving(false);
    }
  };

  const beginEdit = (item) => {
    setEditingDiscountId(item.id);
    setEditingDiscountForm(toDiscountForm(item));
    setMessage("");
    setError("");
  };

  const cancelEdit = () => {
    setEditingDiscountId(null);
    setEditingDiscountForm(initialDiscountForm);
  };

  const saveEdit = async (event, discountId) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await updateDiscount(discountId, buildDiscountPayload(editingDiscountForm));
      setDiscounts((current) =>
        current.map((item) => (item.id === discountId ? response.data : item))
      );
      setMessage(`Скидка #${discountId} обновлена.`);
      cancelEdit();
    } catch (err) {
      setError(normalizeError(err, "Не удалось обновить скидку."));
    } finally {
      setSaving(false);
    }
  };

  const removeDiscount = async (discountId) => {
    if (!window.confirm(`Удалить скидку #${discountId}?`)) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await deleteDiscount(discountId);
      setDiscounts((current) => current.filter((item) => item.id !== discountId));
      setMessage(`Скидка #${discountId} удалена.`);
      if (editingDiscountId === discountId) {
        cancelEdit();
      }
    } catch (err) {
      setError(normalizeError(err, "Не удалось удалить скидку."));
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Скидки</p>
        <h3>Управление скидками</h3>
        <p className="muted">Настраивайте проценты и фиксированные суммы со сроками действия.</p>
      </div>

      {loading ? <p className="muted">Загружаем данные...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <DiscountEditorForm
        form={discountForm}
        onChange={updateDiscountField}
        onSubmit={submitDiscount}
        submitLabel="Создать скидку"
        submitLoadingLabel="Сохраняем скидку..."
        isSubmitting={saving}
        disabled={saving}
      />

      <div className="manage-list">
        <h4>Существующие скидки</h4>
        {!discounts.length ? (
          <EmptyState
            title="Пока нет скидок"
            description="После создания они появятся в этом списке."
          />
        ) : null}

        {discounts.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Скидка #{item.id}</p>
              <div className="manage-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => beginEdit(item)}
                >
                  Редактировать
                </button>
                <button type="button" onClick={() => removeDiscount(item.id)}>
                  Удалить
                </button>
              </div>
            </div>

            <h4>{item.title}</h4>
            <p className="muted">{item.description}</p>
            <div className="detail-chips">
              <span className="detail-chip">Тип: {formatDiscountType(item.type)}</span>
              <span className="detail-chip">Размер: {formatAmount(item)}</span>
            </div>
            <p className="booking-label">{formatPeriod(item.startDate, item.endDate)}</p>

            {editingDiscountId === item.id ? (
              <div className="inline-editor">
                <DiscountEditorForm
                  form={editingDiscountForm}
                  onChange={updateEditingDiscountField}
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
