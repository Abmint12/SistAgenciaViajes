export default function Sidebar({ active = 'clientes', backendOnline = true }) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-icon">AV</div>
        <div>
          <h3>Agencia Viajes</h3>
          <p>Panel operativo</p>
        </div>
      </div>

      <nav>
        <button className={`nav-link ${active === 'clientes' ? 'active' : ''}`}>Clientes</button>
        <button className="nav-link">Destinos</button>
        <button className="nav-link">Reservas</button>
        <button className="nav-link">Pagos</button>
      </nav>

      <div className="status">
        <span className={backendOnline ? 'online' : 'offline'}></span>
        <div>
          <strong>{backendOnline ? 'Backend conectado' : 'Backend desconectado'}</strong>
          <small>{backendOnline ? 'Datos reales' : 'Verifica servidor'}</small>
        </div>
      </div>
    </aside>
  )
}