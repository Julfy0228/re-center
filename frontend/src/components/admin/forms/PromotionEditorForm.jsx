export default function PromotionEditorForm({
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
        <span>Заголовок</span>
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
        />
      </label>

      <div className="admin-form-row">
        <label>
          <span>Начало акции</span>
          <input
            type="datetime-local"
            value={form.startDate}
            onChange={(event) => onChange("startDate", event.target.value)}
          />
        </label>

        <label>
          <span>Окончание акции</span>
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
