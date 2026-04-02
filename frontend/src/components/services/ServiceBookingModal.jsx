import { useState } from "react";
import { createActivity } from "../../api/activities";
import { createBooking } from "../../api/bookings";
import Modal from "../ui/Modal";
import AlertMessage from "../ui/AlertMessage";

function toApiDateTime(value) {
  return `${value}:00`;
}

export default function ServiceBookingModal({ isOpen, onClose, service, user }) {
  const [form, setForm] = useState({
    startDate: "",
    endDate: "",
    peopleCount: 1,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const resetState = () => {
    setForm({
      startDate: "",
      endDate: "",
      peopleCount: 1,
    });
    setLoading(false);
    setError("");
    setMessage("");
  };

  const handleClose = () => {
    resetState();
    onClose();
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setMessage("");
    setLoading(true);

    try {
      await createBooking({
        serviceId: Number(service.id),
        startDate: toApiDateTime(form.startDate),
        endDate: toApiDateTime(form.endDate),
        peopleCount: Number(form.peopleCount),
      });

      setMessage("Бронирование создано. Оно уже появится в разделе с вашими бронями.");

      if (user?.id) {
        createActivity({
          userId: user.id,
          type: "BOOK_SERVICE",
          details: `Создана бронь на услугу "${service.title}"`,
        }).catch(() => {});
      }
    } catch (err) {
      setError(
        typeof err.response?.data === "string"
          ? err.response.data
          : "Не удалось создать бронирование."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={handleClose}>
      <button type="button" className="modal-close" onClick={handleClose}>
        Закрыть
      </button>

      <p className="eyebrow">Быстрое бронирование</p>
      <h3>{service?.title || "Услуга"}</h3>
      <p className="muted">
        Заполните даты и количество гостей. Подтверждение брони придёт после обработки менеджером.
      </p>

      <AlertMessage type="success">{message}</AlertMessage>
      <AlertMessage type="error">{error}</AlertMessage>

      <form className="booking-form" onSubmit={handleSubmit}>
        <label>
          <span>Дата заезда</span>
          <input
            type="datetime-local"
            value={form.startDate}
            onChange={(event) =>
              setForm((current) => ({ ...current, startDate: event.target.value }))
            }
            required
          />
        </label>

        <label>
          <span>Дата выезда</span>
          <input
            type="datetime-local"
            value={form.endDate}
            onChange={(event) =>
              setForm((current) => ({ ...current, endDate: event.target.value }))
            }
            required
          />
        </label>

        <label>
          <span>Количество гостей</span>
          <input
            type="number"
            min="1"
            value={form.peopleCount}
            onChange={(event) =>
              setForm((current) => ({ ...current, peopleCount: event.target.value }))
            }
          />
        </label>

        <button type="submit" disabled={loading}>
          {loading ? "Создаём бронь..." : "Забронировать"}
        </button>
      </form>
    </Modal>
  );
}
