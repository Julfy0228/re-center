export default function ImageUploadField({
  label,
  placeholder,
  value,
  onValueChange,
  onFileChange,
  uploadLoading,
  previewAlt,
}) {
  return (
    <div className="upload-block">
      <label>
        <span>{label}</span>
        <input
          type="url"
          placeholder={placeholder}
          value={value}
          onChange={(event) => onValueChange(event.target.value)}
        />
      </label>

      <label className="upload-field">
        <input
          type="file"
          accept="image/png,image/jpeg,image/webp,image/gif"
          onChange={onFileChange}
          disabled={uploadLoading}
        />
        <span>Добавить файл</span>
      </label>

      {value ? <img className="upload-preview" src={value} alt={previewAlt} /> : null}
    </div>
  );
}
