import DashboardLayout from "../layout/DashboardLayout";
import NewsManager from "./NewsManager";
import ServiceManager from "./ServiceManager";

export default function AdminPanel({ user, onLogout }) {
  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Управление контентом"
      subtitle="Создавайте, редактируйте и удаляйте услуги и новости."
    >
      <section className="admin-intro">
        <div className="card-like admin-banner">
          <p className="eyebrow">Панель менеджера</p>
          <h3>Контент под полным контролем</h3>
          <p className="muted">
            Здесь можно быстро управлять карточками услуг и новостями, не заходя в
            базу данных вручную.
          </p>
        </div>
      </section>

      <section className="admin-grid">
        <ServiceManager />
        <NewsManager />
      </section>
    </DashboardLayout>
  );
}
