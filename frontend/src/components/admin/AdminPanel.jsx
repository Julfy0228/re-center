import { useState } from "react";
import { isAdmin } from "../../utils/permissions";
import DashboardLayout from "../layout/DashboardLayout";
import BookingManager from "./BookingManager";
import DiscountManager from "./DiscountManager";
import NewsManager from "./NewsManager";
import PaymentManager from "./PaymentManager";
import PromotionManager from "./PromotionManager";
import ReviewManager from "./ReviewManager";
import ServiceManager from "./ServiceManager";

const TABS = [
  { id: "bookings", label: "Бронирования" },
  { id: "services", label: "Услуги" },
  { id: "news", label: "Новости" },
  { id: "promotions", label: "Акции" },
  { id: "discounts", label: "Скидки" },
  { id: "reviews", label: "Отзывы" },
];

export default function AdminPanel({ user, onLogout }) {
  const [activeTab, setActiveTab] = useState("bookings");

  const tabsToShow = isAdmin(user) 
    ? [...TABS, { id: "payments", label: "Платежи" }]
    : TABS;

  const renderContent = () => {
    return (
      <>
        <div hidden={activeTab !== "bookings"}>
          <BookingManager user={user} />
        </div>
        <div hidden={activeTab !== "services"}>
          <ServiceManager />
        </div>
        <div hidden={activeTab !== "news"}>
          <NewsManager />
        </div>
        <div hidden={activeTab !== "promotions"}>
          <PromotionManager user={user} />
        </div>
        <div hidden={activeTab !== "discounts"}>
          <DiscountManager />
        </div>
        <div hidden={activeTab !== "reviews"}>
          <ReviewManager user={user} />
        </div>
        {isAdmin(user) ? (
          <div hidden={activeTab !== "payments"}>
            <PaymentManager />
          </div>
        ) : null}
      </>
    );
  };

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

      <div className="admin-tabs-container">
        <div className="admin-tabs">
          {tabsToShow.map((tab) => (
            <button
              key={tab.id}
              type="button"
              className={`admin-tab ${activeTab === tab.id ? "active" : ""}`}
              onClick={() => setActiveTab(tab.id)}
            >
              <span className="tab-label">{tab.label}</span>
            </button>
          ))}
        </div>
      </div>

      <section className="admin-content">
        {renderContent()}
      </section>
    </DashboardLayout>
  );
}
