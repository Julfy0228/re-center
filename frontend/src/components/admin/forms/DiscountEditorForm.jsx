export default function DiscountEditorForm({
  form,
  onChange,
  onSubmit,
  onCancel,
  submitLabel,
  submitLoadingLabel,
  isSubmitting,
  disabled,
  showCancel = false,
}) {
  return (
    <form className="admin-form" onSubmit={onSubmit}>
      <label>
        <span>Название скидки</span>
        <input
          type="text"
          value={form.title}
          onChange={(event) => onChange("title", event.target.value)}
          required
        />
      </label>

      <label>
        <span>Описание</span>
        <textarea
          rows="5"
          value={form.description}
          onChange={(event) => onChange("description", event.target.value)}
          required
        />
      </label>

      <div className="admin-form-row">
        <label>
          <span>Тип скидки</span>
          <select value={form.type} onChange={(event) => onChange("type", event.target.value)}>
            <option value="PERCENT">Процент</option>
            <option value="AMOUNT">Сумма</option>
          </select>
        </label>

        <label>
          <span>Размер</span>
          <input
            type="number"
            min="0"
            step="0.01"
            value={form.amount}
            onChange={(event) => onChange("amount", event.target.value)}
            required
          />
        </label>
      </div>

      <div className="admin-form-row">
        <label>
          <span>Начало скидки</span>
          <input
            type="datetime-local"
            value={form.startDate}
            onChange={(event) => onChange("startDate", event.target.value)}
          />
        </label>

        <label>
          <span>Окончание скидки</span>
          <input
            type="datetime-local"
            value={form.endDate}
            onChange={(event) => onChange("endDate", event.target.value)}
          />
        </label>
      </div>

      <div className="manage-actions">
        <button type="submit" disabled={disabled}>
          {isSubmitting ? submitLoadingLabel : submitLabel}
        </button>
        {showCancel ? (
          <button type="button" className="secondary-button" onClick={onCancel}>
            Отмена
          </button>
        ) : null}
      </div>
    </form>
  );
}
