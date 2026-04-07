import ImageUploadField from "../../ui/ImageUploadField";

export default function NewsEditorForm({
  form,
  onChange,
  onSubmit,
  onCancel,
  onImageUpload,
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
        <span>Заголовок</span>
        <input
          type="text"
          value={form.title}
          onChange={(event) => onChange("title", event.target.value)}
          required
        />
      </label>

      <label>
        <span>Текст новости</span>
        <textarea
          rows="8"
          value={form.content}
          onChange={(event) => onChange("content", event.target.value)}
          required
        />
      </label>

      <ImageUploadField
        label="Ссылка на изображение"
        placeholder="https://example.com/news.jpg"
        value={form.imageUrl}
        onValueChange={(value) => onChange("imageUrl", value)}
        onFileChange={onImageUpload}
        uploadLoading={uploadLoading}
        previewAlt="Предпросмотр новости"
      />

      <label>
        <span>Статус</span>
        <select
          value={form.status}
          onChange={(event) => onChange("status", event.target.value)}
        >
          <option value="PUBLISHED">Опубликовать</option>
          <option value="DRAFT">Черновик</option>
        </select>
      </label>

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
