import { useEffect, useState } from 'react'
import { X } from 'lucide-react'
import { ESTADOS, ESTADO_LABELS } from '../utils/format'

const EMPTY_FORM = {
  idCliente: '',
  idDestino: '',
  fechaViaje: '',
  cantPasajes: '',
  costoTotal: '',
  estado: 'PENDIENTE',
}

export default function ReservaFormPanel({
  clientes,
  destinos,
  editingReserva,
  onSubmit,
  onCancelEdit,
  submitting,
  error,
}) {
  const [form, setForm] = useState(EMPTY_FORM)
  const isEditing = Boolean(editingReserva)

  useEffect(() => {
    if (editingReserva) {
      setForm({
        idCliente: String(editingReserva.idCliente ?? ''),
        idDestino: String(editingReserva.idDestino ?? ''),
        fechaViaje: editingReserva.fechaViaje ?? '',
        cantPasajes: String(editingReserva.cantPasajes ?? ''),
        costoTotal: String(editingReserva.costoTotal ?? ''),
        estado: editingReserva.estado ?? 'PENDIENTE',
      })
    } else {
      setForm(EMPTY_FORM)
    }
  }, [editingReserva])

  function handleChange(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  function handleSubmit(e) {
    e.preventDefault()
    onSubmit({
      idCliente: Number(form.idCliente),
      idDestino: Number(form.idDestino),
      fechaViaje: form.fechaViaje,
      cantPasajes: Number(form.cantPasajes),
      costoTotal: Number(form.costoTotal),
      estado: form.estado,
    })
  }

  const isValid =
    form.idCliente && form.idDestino && form.fechaViaje && form.cantPasajes && form.costoTotal

  return (
    <div className="w-[360px] shrink-0 rounded-2xl border border-slate-200 bg-white p-6">
      <div className="mb-5 flex items-start justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-wide text-brand-600">Registro</p>
          <h2 className="text-lg font-bold text-navy-950">
            {isEditing ? 'Editar reserva' : 'Nueva reserva'}
          </h2>
        </div>
        {isEditing && (
          <button
            type="button"
            onClick={onCancelEdit}
            className="rounded-lg p-1 text-slate-400 hover:bg-slate-100 hover:text-slate-600"
            title="Cancelar edición"
          >
            <X size={18} />
          </button>
        )}
      </div>

      <form className="space-y-4" onSubmit={handleSubmit}>
        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">Cliente</label>
          <select
            className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
            value={form.idCliente}
            onChange={(e) => handleChange('idCliente', e.target.value)}
            required
          >
            <option value="">Selecciona un cliente</option>
            {clientes.map((c) => (
              <option key={c.id} value={c.id}>
                {c.nombre} {c.apellido} — {c.cedula}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">Destino</label>
          <select
            className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
            value={form.idDestino}
            onChange={(e) => handleChange('idDestino', e.target.value)}
            required
          >
            <option value="">Selecciona un destino</option>
            {destinos.map((d) => (
              <option key={d.idDestino} value={d.idDestino}>
                {d.nombre} — {d.ciudad}, {d.pais}
              </option>
            ))}
          </select>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="mb-1 block text-sm font-medium text-slate-700">Fecha viaje</label>
            <input
              type="date"
              className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
              value={form.fechaViaje}
              onChange={(e) => handleChange('fechaViaje', e.target.value)}
              required
            />
          </div>
          <div>
            <label className="mb-1 block text-sm font-medium text-slate-700">Pasajes</label>
            <input
              type="number"
              min="1"
              className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
              value={form.cantPasajes}
              onChange={(e) => handleChange('cantPasajes', e.target.value)}
              required
            />
          </div>
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">Costo total (USD)</label>
          <input
            type="number"
            min="0"
            step="0.01"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
            value={form.costoTotal}
            onChange={(e) => handleChange('costoTotal', e.target.value)}
            required
          />
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">Estado</label>
          <select
            className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
            value={form.estado}
            onChange={(e) => handleChange('estado', e.target.value)}
          >
            {ESTADOS.map((estado) => (
              <option key={estado} value={estado}>
                {ESTADO_LABELS[estado]}
              </option>
            ))}
          </select>
        </div>

        {error && (
          <p className="rounded-lg bg-rose-50 px-3 py-2 text-sm text-rose-600">{error}</p>
        )}

        <button
          type="submit"
          disabled={!isValid || submitting}
          className="w-full rounded-lg bg-brand-600 py-2.5 text-sm font-semibold text-white transition-colors hover:bg-brand-700 disabled:cursor-not-allowed disabled:opacity-50"
        >
          {submitting
            ? 'Guardando…'
            : isEditing
            ? 'Actualizar reserva'
            : 'Guardar reserva'}
        </button>
      </form>
    </div>
  )
}
