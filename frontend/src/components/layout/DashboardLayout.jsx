export default function DashboardLayout({ title, subtitle, children }) {
  return (
    <main className="content-panel card">
      <header className="page-header">
        <div>
          <p className="eyebrow">Личный кабинет</p>
          <h2>{title}</h2>
          <p className="muted">{subtitle}</p>
        </div>
      </header>
      {children}
    </main>
  );
}
