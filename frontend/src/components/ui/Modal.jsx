export default function Modal({ isOpen, onClose, children }) {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <article className="modal-card" onClick={(event) => event.stopPropagation()}>
        {children}
      </article>
    </div>
  );
}
