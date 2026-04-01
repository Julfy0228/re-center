export const initialServiceForm = {
  title: "",
  description: "",
  imageUrl: "",
  duration: "1",
  price: "",
  maxPeople: "1",
  categoryId: "",
};

export const initialNewsForm = {
  title: "",
  content: "",
  imageUrl: "",
  status: "PUBLISHED",
};

export function normalizeError(err, fallback) {
  if (typeof err.response?.data === "string") {
    return err.response.data;
  }

  return fallback;
}

export function toServiceForm(item, fallbackCategoryId = "") {
  return {
    title: item.title || "",
    description: item.description || "",
    imageUrl: item.imageUrl || "",
    duration: String(item.duration || 1),
    price: String(item.price || ""),
    maxPeople: String(item.maxPeople || 1),
    categoryId: String(item.categoryId || fallbackCategoryId || ""),
  };
}

export function toNewsForm(item) {
  return {
    title: item.title || "",
    content: item.content || "",
    imageUrl: item.imageUrl || "",
    status: item.status || "DRAFT",
  };
}

export function buildServicePayload(form) {
  return {
    title: form.title,
    description: form.description,
    imageUrl: form.imageUrl || null,
    duration: Number(form.duration || 1),
    price: form.price,
    maxPeople: Number(form.maxPeople || 1),
    categoryId: Number(form.categoryId),
  };
}

export function buildNewsPayload(form) {
  return {
    title: form.title,
    content: form.content,
    imageUrl: form.imageUrl || null,
    status: form.status,
  };
}

export function formatNewsStatus(status) {
  if (status === "PUBLISHED") {
    return "Опубликовано";
  }

  if (status === "DRAFT") {
    return "Черновик";
  }

  return status || "Неизвестно";
}
