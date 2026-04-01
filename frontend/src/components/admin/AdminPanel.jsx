import DashboardLayout from "../layout/DashboardLayout";
import DiscountManager from "./DiscountManager";
import NewsManager from "./NewsManager";
import PromotionManager from "./PromotionManager";
import ServiceManager from "./ServiceManager";

export default function AdminPanel({ user, onLogout }) {
  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Управление контентом"
      subtitle="Создавайте, редактируйте и удаляйте услуги, новости, акции и скидки."
    >
      <section className="admin-intro">
        <div className="card-like admin-banner">
          <p className="eyebrow">Панель менеджера</p>
          <h3>Контент и предложения под полным контролем</h3>
          <p className="muted">
            Здесь можно управлять карточками услуг, новостями и маркетинговыми предложениями,
            не заходя в базу данных вручную.
          </p>
        </div>
      </section>

      <section className="admin-grid">
        <ServiceManager />
        <NewsManager />
        <PromotionManager user={user} />
        <DiscountManager />
      </section>
    </DashboardLayout>
  );
}
