import { toDateTimeLocalValue } from "../../utils/date";

function toApiDateTime(value) {
  if (!value) {
    return null;
  }

  return value.length === 16 ? `${value}:00` : value;
}

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

export const initialPromotionForm = {
  title: "",
  description: "",
  startDate: "",
  endDate: "",
};

export const initialDiscountForm = {
  title: "",
  description: "",
  startDate: "",
  endDate: "",
  type: "PERCENT",
  amount: "",
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

export function toPromotionForm(item) {
  return {
    title: item.title || "",
    description: item.description || "",
    startDate: toDateTimeLocalValue(item.startDate),
    endDate: toDateTimeLocalValue(item.endDate),
  };
}

export function toDiscountForm(item) {
  return {
    title: item.title || "",
    description: item.description || "",
    startDate: toDateTimeLocalValue(item.startDate),
    endDate: toDateTimeLocalValue(item.endDate),
    type: item.type || "PERCENT",
    amount: String(item.amount || ""),
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

export function buildPromotionPayload(form) {
  return {
    title: form.title,
    description: form.description,
    startDate: toApiDateTime(form.startDate),
    endDate: toApiDateTime(form.endDate),
  };
}

export function buildDiscountPayload(form) {
  return {
    title: form.title,
    description: form.description,
    startDate: toApiDateTime(form.startDate),
    endDate: toApiDateTime(form.endDate),
    type: form.type,
    amount: form.amount,
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

export function formatDiscountType(type) {
  if (type === "PERCENT") {
    return "Процент";
  }

  if (type === "AMOUNT") {
    return "Сумма";
  }

  return type || "Неизвестно";
}
