import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCategories, getServices, getServicesByCategory } from "../../api/catalog";
import DashboardLayout from "../layout/DashboardLayout";
import ServiceCard from "./ServiceCard";
import AlertMessage from "../ui/AlertMessage";
import EmptyState from "../ui/EmptyState";

export default function ServiceCatalog({ user, onLogout }) {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [services, setServices] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getCategories(), getServices()])
      .then(([categoriesRes, servicesRes]) => {
        setCategories(categoriesRes.data);
        setServices(servicesRes.data);
      })
      .catch(() => setError("Не удалось загрузить каталог услуг. Проверьте backend."))
      .finally(() => setLoading(false));
  }, []);

  const handleCategoryChange = async (event) => {
    const nextCategory = event.target.value;
    setSelectedCategory(nextCategory);
    setError("");

    try {
      const response =
        nextCategory === "all"
          ? await getServices()
          : await getServicesByCategory(nextCategory);

      setServices(response.data);
    } catch (err) {
      setError("Не удалось обновить список услуг.");
    }
  };

  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Каталог услуг"
      subtitle="Выберите подходящий вариант отдыха и откройте подробную страницу с описанием и отзывами."
    >
      <section className="card-like catalog-hero">
        <div>
          <p className="eyebrow">Каталог</p>
          <h3>Выберите формат отдыха</h3>
          <p className="muted">
            Домики, баня и специальные пакеты отдыха теперь открываются на отдельной странице
            услуги, где можно спокойно прочитать детали и только потом перейти к бронированию.
          </p>
        </div>

        <label className="filter-field">
          <span>Категория</span>
          <select value={selectedCategory} onChange={handleCategoryChange}>
              <option value="all">Все категории</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
          </select>
        </label>
      </section>

      {loading ? <p className="muted">Загружаем каталог...</p> : null}
      <AlertMessage type="error">{error}</AlertMessage>

      {!loading && !services.length ? (
        <EmptyState
          title="Услуги не найдены"
          description="Попробуйте выбрать другую категорию или добавьте услуги через админку."
        />
      ) : null}

      <section className="catalog-grid catalog-grid-fixed">
        {services.map((service) => (
          <ServiceCard
            key={service.id}
            service={service}
            onOpen={(serviceId) => navigate(`/services/${serviceId}`)}
          />
        ))}
      </section>
    </DashboardLayout>
  );
}
