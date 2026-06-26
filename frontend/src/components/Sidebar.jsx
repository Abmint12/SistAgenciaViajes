import { Users, MapPin, CalendarCheck, CreditCard } from 'lucide-react'

const NAV_ITEMS = [
  { key: 'clientes', label: 'Clientes', icon: Users },
  { key: 'destinos', label: 'Destinos', icon: MapPin },
  { key: 'reservas', label: 'Reservas', icon: CalendarCheck },
  { key: 'pagos', label: 'Pagos', icon: CreditCard },
]

export default function Sidebar({ active = 'reservas', backendOnline }) {
  return (
    <aside className="flex h-full w-[270px] shrink-0 flex-col bg-navy-950 text-slate-300">
      <div className="flex items-center gap-3 px-6 py-6">
        <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-brand-500/90 text-sm font-bold text-white">
          AV
        </div>
        <div>
          <p className="text-sm font-semibold text-white">Agencia Viajes</p>
          <p className="text-xs text-slate-400">Panel operativo</p>
        </div>
      </div>

      <nav className="mt-4 flex-1 space-y-1 px-4">
        {NAV_ITEMS.map(({ key, label, icon: Icon }) => {
          const isActive = key === active
          return (
            <button
              key={key}
              type="button"
              disabled={!isActive}
              title={isActive ? undefined : 'Módulo gestionado por otro integrante del equipo'}
              className={[
                'flex w-full items-center gap-3 rounded-lg px-4 py-2.5 text-sm font-medium transition-colors',
                isActive
                  ? 'bg-white text-navy-950 shadow-sm'
                  : 'text-slate-400 cursor-default opacity-70',
              ].join(' ')}
            >
              <Icon size={17} strokeWidth={2} />
              {label}
            </button>
          )
        })}
      </nav>

      <div className="mx-4 mb-6 flex items-center gap-3 rounded-xl bg-navy-900 px-4 py-3">
        <span
          className={[
            'h-2.5 w-2.5 rounded-full',
            backendOnline ? 'bg-emerald-400' : 'bg-rose-400',
          ].join(' ')}
        />
        <div>
          <p className="text-xs font-semibold text-white">
            {backendOnline ? 'Backend conectado' : 'Backend desconectado'}
          </p>
          <p className="text-[11px] text-slate-400">
            {backendOnline ? 'Datos reales' : 'Verifica el servidor'}
          </p>
        </div>
      </div>
    </aside>
  )
}
