import ImageUploadField from "../../ui/ImageUploadField";

export default function ServiceEditorForm({
  form,
  onChange,
  onSubmit,
  onCancel,
  onImageUpload,
  categories,
  submitLabel,
  submitLoadingLabel,
  isSubmitting,
  disabled,
  uploadLoading,
  showCancel = false,
}) {
  return (
    <form className="admin-form" onSubmit={onSubmit}>
      <label>
        <span>Название</span>
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

      <ImageUploadField
        label="Ссылка на изображение"
        placeholder="https://example.com/service.jpg"
        value={form.imageUrl}
        onValueChange={(value) => onChange("imageUrl", value)}
        onFileChange={onImageUpload}
        uploadLoading={uploadLoading}
        previewAlt="Предпросмотр услуги"
      />

      <div className="admin-form-row">
        <label>
          <span>Длительность, дней</span>
          <input
            type="number"
            min="1"
            value={form.duration}
            onChange={(event) => onChange("duration", event.target.value)}
          />
        </label>

        <label>
          <span>Максимум гостей</span>
          <input
            type="number"
            min="1"
            value={form.maxPeople}
            onChange={(event) => onChange("maxPeople", event.target.value)}
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
            value={form.price}
            onChange={(event) => onChange("price", event.target.value)}
            required
          />
        </label>

        <label>
          <span>Категория</span>
          <select
            value={form.categoryId}
            onChange={(event) => onChange("categoryId", event.target.value)}
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
