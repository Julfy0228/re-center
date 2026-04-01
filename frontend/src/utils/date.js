function toDateFromParts(parts) {
  if (!Array.isArray(parts) || parts.length < 5) {
    return null;
  }

  const [year, month, day, hour, minute, second = 0, nano = 0] = parts;
  const milliseconds = Math.floor(Number(nano || 0) / 1000000);
  const date = new Date(
    Number(year),
    Number(month) - 1,
    Number(day),
    Number(hour),
    Number(minute),
    Number(second),
    milliseconds
  );

  return Number.isNaN(date.getTime()) ? null : date;
}

export function parseApiDate(value) {
  if (!value) {
    return null;
  }

  if (value instanceof Date) {
    return Number.isNaN(value.getTime()) ? null : value;
  }

  if (Array.isArray(value)) {
    return toDateFromParts(value);
  }

  if (typeof value === "number") {
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? null : date;
  }

  if (typeof value === "string") {
    const normalized = value.includes("T") ? value : value.replace(" ", "T");
    const date = new Date(normalized);
    return Number.isNaN(date.getTime()) ? null : date;
  }

  if (typeof value === "object") {
    const {
      year,
      monthValue,
      month,
      dayOfMonth,
      hour = 0,
      minute = 0,
      second = 0,
      nano = 0,
    } = value;

    const resolvedMonth =
      monthValue ||
      (typeof month === "number" ? month : null);

    if (year && resolvedMonth && dayOfMonth) {
      return toDateFromParts([
        year,
        resolvedMonth,
        dayOfMonth,
        hour,
        minute,
        second,
        nano,
      ]);
    }
  }

  return null;
}

export function formatApiDateTime(value, fallback = "-") {
  const date = parseApiDate(value);

  if (!date) {
    return fallback;
  }

  return new Intl.DateTimeFormat("ru-RU", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}

export function formatApiLongDateTime(value, fallback = "Скоро на сайте") {
  const date = parseApiDate(value);

  if (!date) {
    return fallback;
  }

  return new Intl.DateTimeFormat("ru-RU", {
    day: "2-digit",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}
