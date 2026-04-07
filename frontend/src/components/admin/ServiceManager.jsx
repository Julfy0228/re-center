import { useEffect, useState } from "react";
import {
  createService,
  deleteService,
  getCategories,
  getServices,
  updateService,
} from "../../api/catalog";
import { uploadImage } from "../../api/uploads";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";
import ServiceEditorForm from "./forms/ServiceEditorForm";
import {
  buildServicePayload,
  initialServiceForm,
  normalizeError,
  toServiceForm,
} from "./adminUtils";

export default function ServiceManager() {
  const [categories, setCategories] = useState([]);
  const [services, setServices] = useState([]);
  const [serviceForm, setServiceForm] = useState(initialServiceForm);
  const [editingServiceId, setEditingServiceId] = useState(null);
  const [editingServiceForm, setEditingServiceForm] = useState(initialServiceForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploadLoading, setUploadLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getCategories(), getServices()])
      .then(([categoriesRes, servicesRes]) => {
        const fetchedCategories = categoriesRes.data;
        setCategories(fetchedCategories);
        setServices(servicesRes.data);

        if (fetchedCategories.length > 0) {
          setServiceForm((current) => ({
            ...current,
            categoryId: current.categoryId || String(fetchedCategories[0].id),
          }));
        }
      })
      .catch(() => setError("Не удалось загрузить данные по услугам."))
      .finally(() => setLoading(false));
  }, []);

  const updateForm = (setter) => (key, value) => {
    setter((current) => ({ ...current, [key]: value }));
  };

  const updateServiceField = updateForm(setServiceForm);
  const updateEditingServiceField = updateForm(setEditingServiceForm);

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
        updateEditingServiceField("imageUrl", response.data.url);
      } else {
        updateServiceField("imageUrl", response.data.url);
      }
      setMessage("Изображение услуги загружено.");
    } catch (err) {
      setError(normalizeError(err, "Не удалось загрузить изображение услуги."));
    } finally {
      setUploadLoading(false);
      event.target.value = "";
    }
  };

  const submitService = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await createService(buildServicePayload(serviceForm));
      setServices((current) => [response.data, ...current]);
      setMessage("Услуга создана и добавлена в список.");
      setServiceForm((current) => ({
        ...initialServiceForm,
        categoryId: current.categoryId,
      }));
    } catch (err) {
      setError(normalizeError(err, "Не удалось создать услугу."));
    } finally {
      setSaving(false);
    }
  };

  const beginEdit = (item) => {
    const fallbackCategoryId = serviceForm.categoryId || categories[0]?.id || "";
    setEditingServiceId(item.id);
    setEditingServiceForm(toServiceForm(item, fallbackCategoryId));
    setMessage("");
    setError("");
  };

  const cancelEdit = () => {
    setEditingServiceId(null);
    setEditingServiceForm(initialServiceForm);
  };

  const saveEdit = async (event, serviceId) => {
    event.preventDefault();
    setMessage("");
    setError("");
    setSaving(true);

    try {
      const response = await updateService(
        serviceId,
        buildServicePayload(editingServiceForm)
      );
      setServices((current) =>
        current.map((item) => (item.id === serviceId ? response.data : item))
      );
      setMessage(`Услуга #${serviceId} обновлена.`);
      cancelEdit();
    } catch (err) {
      setError(normalizeError(err, "Не удалось обновить услугу."));
    } finally {
      setSaving(false);
    }
  };

  const removeService = async (serviceId) => {
    if (!window.confirm(`Удалить услугу #${serviceId}?`)) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await deleteService(serviceId);
      setServices((current) => current.filter((item) => item.id !== serviceId));
      setMessage(`Услуга #${serviceId} удалена.`);
      if (editingServiceId === serviceId) {
        cancelEdit();
      }
    } catch (err) {
      setError(normalizeError(err, "Не удалось удалить услугу."));
    }
  };

  return (
    <article className="admin-card">
      <div className="admin-card-header">
        <p className="eyebrow">Услуга</p>
        <h3>Управление услугами</h3>
        <p className="muted">Создавайте и обновляйте карточки домиков, бань и пакетов отдыха.</p>
      </div>

      {loading ? <p className="muted">Загружаем данные...</p> : null}
      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <ServiceEditorForm
        form={serviceForm}
        onChange={updateServiceField}
        onSubmit={submitService}
        onImageUpload={(event) => handleImageUpload(event, false)}
        categories={categories}
        submitLabel="Создать услугу"
        submitLoadingLabel="Сохраняем услугу..."
        isSubmitting={saving}
        disabled={saving || uploadLoading || loading || !categories.length}
        uploadLoading={uploadLoading}
      />

      <div className="manage-list">
        <h4>Существующие услуги</h4>
        {!services.length ? (
          <EmptyState
            title="Пока нет услуг"
            description="После создания они появятся в этом списке."
          />
        ) : null}

        {services.map((item) => (
          <article key={item.id} className="manage-item">
            <div className="manage-head">
              <p className="booking-label">Услуга #{item.id}</p>
              <div className="manage-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => beginEdit(item)}
                >
                  Редактировать
                </button>
                <button type="button" onClick={() => removeService(item.id)}>
                  Удалить
                </button>
              </div>
            </div>

            <h4>{item.title}</h4>
            <p className="muted">{item.description || "Без описания"}</p>

            {editingServiceId === item.id ? (
              <div className="inline-editor">
                <ServiceEditorForm
                  form={editingServiceForm}
                  onChange={updateEditingServiceField}
                  onSubmit={(event) => saveEdit(event, item.id)}
                  onCancel={cancelEdit}
                  onImageUpload={(event) => handleImageUpload(event, true)}
                  categories={categories}
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
