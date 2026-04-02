export default function ServiceCard({ service, onOpen }) {
  return (
    <article className="service-card service-card-fixed">
      <div className="service-image-wrap">
        {service.imageUrl ? (
          <img className="service-image" src={service.imageUrl} alt={service.title} />
        ) : (
          <div className="service-image service-image-fallback">
            <span>Нет фото</span>
          </div>
        )}
      </div>

      <p className="booking-label">Услуга #{service.id}</p>
      <h3>{service.title}</h3>
      <p className="muted">{service.description || "Описание пока не добавлено."}</p>
      <div className="service-meta">
        <span>{new Intl.NumberFormat("ru-RU").format(service.price || 0)} ₽</span>
        <span>До {service.maxPeople || 1} гостей</span>
        <span>{service.duration || 1} дней</span>
      </div>
      <button type="button" onClick={() => onOpen(service.id)}>
        Подробнее
      </button>
    </article>
  );
}
