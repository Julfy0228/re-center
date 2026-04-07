export default function AuthLayout({
  eyebrow,
  title,
  description,
  footer,
  children,
}) {
  return (
    <div className="app-shell">
      <div className="card auth-card">
        <div className="auth-header">
          <p className="eyebrow">{eyebrow}</p>
          <h1>{title}</h1>
          {description ? <p className="muted">{description}</p> : null}
        </div>

        {children}

        {footer ? <p className="muted auth-footer">{footer}</p> : null}
      </div>
    </div>
  );
}
