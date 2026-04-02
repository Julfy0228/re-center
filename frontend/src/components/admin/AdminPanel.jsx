import { isAdmin } from "../../utils/permissions";
import DashboardLayout from "../layout/DashboardLayout";
import BookingManager from "./BookingManager";
import DiscountManager from "./DiscountManager";
import NewsManager from "./NewsManager";
import PaymentManager from "./PaymentManager";
import PromotionManager from "./PromotionManager";
import ReviewManager from "./ReviewManager";
import ServiceManager from "./ServiceManager";

export default function AdminPanel({ user, onLogout }) {
  return (
    <DashboardLayout
      user={user}
      onLogout={onLogout}
      title="Управление контентом"
      subtitle="Создавайте, редактируйте и модерируйте всё, что видят пользователи."
    >
      <section className="admin-intro">
        <div className="card-like admin-banner">
          <p className="eyebrow">Панель менеджера</p>
          <h3>Контент, бронирования и предложения под контролем</h3>
          <p className="muted">
            Здесь можно подтверждать брони, управлять услугами, новостями, акциями, скидками и
            следить за отзывами пользователей.
          </p>
        </div>
      </section>

      <section className="admin-grid">
        <BookingManager user={user} />
        <ServiceManager />
        <NewsManager />
        <PromotionManager user={user} />
        <DiscountManager />
        <ReviewManager user={user} />
        {isAdmin(user) ? <PaymentManager /> : null}
      </section>
    </DashboardLayout>
  );
}
