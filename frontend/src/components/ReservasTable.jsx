import { Pencil, Trash2 } from 'lucide-react'
import { formatCurrency, formatDate, ESTADO_STYLES, ESTADO_LABELS } from '../utils/format'

export default function ReservasTable({ reservas, loading, onEdit, onDelete }) {
  if (loading) {
    return (
      <div className="flex items-center justify-center py-16 text-sm text-slate-400">
        Cargando reservas…
      </div>
    )
  }

  if (reservas.length === 0) {
    return (
      <div className="flex items-center justify-center py-16 text-sm text-slate-400">
        No hay reservas para mostrar.
      </div>
    )
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-left text-sm">
        <thead>
          <tr className="border-b border-slate-200 text-xs font-semibold uppercase tracking-wide text-slate-500">
            <th className="px-6 py-3">Cliente</th>
            <th className="px-6 py-3">Destino</th>
            <th className="px-6 py-3">Fecha viaje</th>
            <th className="px-6 py-3">Pasajes</th>
            <th className="px-6 py-3">Costo total</th>
            <th className="px-6 py-3">Estado</th>
            <th className="px-6 py-3 text-right">Acciones</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {reservas.map((r) => (
            <tr key={r.id} className="hover:bg-slate-50">
              <td className="px-6 py-3.5 font-medium text-navy-950">
                {r.nombreCliente || `Cliente #${r.idCliente}`}
              </td>
              <td className="px-6 py-3.5 text-slate-600">
                {r.nombreDestino || `Destino #${r.idDestino}`}
              </td>
              <td className="px-6 py-3.5 text-slate-600">{formatDate(r.fechaViaje)}</td>
              <td className="px-6 py-3.5 text-slate-600">{r.cantPasajes}</td>
              <td className="px-6 py-3.5 text-slate-600">{formatCurrency(r.costoTotal)}</td>
              <td className="px-6 py-3.5">
                <span
                  className={[
                    'inline-flex rounded-full px-2.5 py-1 text-xs font-medium',
                    ESTADO_STYLES[r.estado] || 'bg-slate-100 text-slate-600',
                  ].join(' ')}
                >
                  {ESTADO_LABELS[r.estado] || r.estado}
                </span>
              </td>
              <td className="px-6 py-3.5">
                <div className="flex justify-end gap-2">
                  <button
                    type="button"
                    onClick={() => onEdit(r)}
                    className="rounded-lg p-1.5 text-slate-500 hover:bg-slate-100 hover:text-navy-950"
                    title="Editar reserva"
                  >
                    <Pencil size={16} />
                  </button>
                  <button
                    type="button"
                    onClick={() => onDelete(r)}
                    className="rounded-lg p-1.5 text-slate-500 hover:bg-rose-50 hover:text-rose-600"
                    title="Eliminar reserva"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
