import { NavLink } from 'react-router-dom'
import { Users, MapPin, CalendarCheck, CreditCard } from 'lucide-react'

const NAV_ITEMS = [
  { key: 'clientes', label: 'Clientes', icon: Users, path: '/dashboard/clientes' },
  { key: 'destinos', label: 'Destinos', icon: MapPin, path: '/dashboard/destinos' },
  { key: 'reservas', label: 'Reservas', icon: CalendarCheck, path: '/dashboard/reservas' },
  { key: 'pagos', label: 'Pagos', icon: CreditCard, path: '/dashboard/pagos' },
]

export default function Sidebar({ backendOnline }) {
  return (
    <aside className="flex h-full w-[270px] flex-col bg-navy-950 text-slate-300">

      {/* HEADER */}
      <div className="flex items-center gap-3 px-6 py-6">
        <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-brand-500 text-white font-bold">
          AV
        </div>
        <div>
          <p className="text-white font-semibold">Agencia Viajes</p>
          <p className="text-xs text-slate-400">Panel operativo</p>
        </div>
      </div>

      {/* NAV */}
      <nav className="flex-1 space-y-1 px-4">
        {NAV_ITEMS.map(({ key, label, icon: Icon, path }) => (
          <NavLink
            key={key}
            to={path}
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-lg px-4 py-2.5 text-sm font-medium transition
              ${
                isActive
                  ? 'bg-white text-navy-950'
                  : 'text-slate-300 hover:bg-navy-900'
              }`
            }
          >
            <Icon size={18} />
            {label}
          </NavLink>
        ))}
      </nav>

      {/* STATUS */}
      <div className="mx-4 mb-6 flex items-center gap-3 rounded-xl bg-navy-900 px-4 py-3">
        <span className={`h-2.5 w-2.5 rounded-full ${backendOnline ? 'bg-green-400' : 'bg-red-400'}`} />
        <div>
          <p className="text-xs font-semibold text-white">
            {backendOnline ? 'Backend conectado' : 'Backend desconectado'}
          </p>
        </div>
      </div>

    </aside>
  )
}