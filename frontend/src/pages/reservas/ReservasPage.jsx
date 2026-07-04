import { useEffect, useMemo, useState } from 'react'
import { Plus, RefreshCw, Search } from 'lucide-react'
import StatCard from '../../components/StatCard'
import ReservasTable from '../../components/ReservasTable'
import ReservaFormPanel from '../../components/ReservaFormPanel'
import ConfirmDialog from '../../components/ConfirmDialog'
import { listarReservas, crearReserva, actualizarReserva, eliminarReserva } from '../../api/reservasApi'
import { listarClientes } from '../../api/clientesApi'
import { listarDestinos } from '../../api/destinosApi'
import { getErrorMessage } from '../../api/http'
import { ESTADOS, ESTADO_LABELS } from '../../utils/format'

export default function ReservasPage() {
  const [reservas, setReservas] = useState([])
  const [clientes, setClientes] = useState([])
  const [destinos, setDestinos] = useState([])

  const [loading, setLoading] = useState(true)
  const [backendOnline, setBackendOnline] = useState(true)

  const [search, setSearch] = useState('')
  const [estadoFiltro, setEstadoFiltro] = useState('TODOS')

  const [editingReserva, setEditingReserva] = useState(null)
  const [submitting, setSubmitting] = useState(false)
  const [formError, setFormError] = useState('')

  const [toDelete, setToDelete] = useState(null)
  const [deleting, setDeleting] = useState(false)

  const [toast, setToast] = useState(null)

  async function cargarTodo() {
    setLoading(true)
    try {
      const [reservasData, clientesData, destinosData] = await Promise.all([
        listarReservas(),
        listarClientes(),
        listarDestinos(),
      ])
      setReservas(reservasData)
      setClientes(clientesData)
      setDestinos(destinosData)
      setBackendOnline(true)
    } catch (err) {
      setBackendOnline(false)
      showToast(getErrorMessage(err), 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    cargarTodo()
  }, [])

  function showToast(message, type = 'success') {
    setToast({ message, type })
    window.clearTimeout(showToast._t)
    showToast._t = window.setTimeout(() => setToast(null), 3500)
  }

  async function handleSubmit(payload) {
    setSubmitting(true)
    setFormError('')
    try {
      if (editingReserva) {
        const actualizada = await actualizarReserva(editingReserva.id, payload)
        setReservas((prev) => prev.map((r) => (r.id === actualizada.id ? actualizada : r)))
        showToast('Reserva actualizada correctamente.')
      } else {
        const creada = await crearReserva(payload)
        setReservas((prev) => [creada, ...prev])
        showToast('Reserva creada correctamente.')
      }
      setEditingReserva(null)
    } catch (err) {
      setFormError(getErrorMessage(err))
    } finally {
      setSubmitting(false)
    }
  }

  async function confirmarEliminar() {
    if (!toDelete) return
    setDeleting(true)
    try {
      await eliminarReserva(toDelete.id)
      setReservas((prev) => prev.filter((r) => r.id !== toDelete.id))
      showToast('Reserva eliminada.')
      setToDelete(null)
    } catch (err) {
      showToast(getErrorMessage(err), 'error')
    } finally {
      setDeleting(false)
    }
  }

  const reservasFiltradas = useMemo(() => {
    const q = search.trim().toLowerCase()
    return reservas.filter((r) => {
      const matchesEstado = estadoFiltro === 'TODOS' || r.estado === estadoFiltro
      const matchesQuery =
        !q ||
        r.nombreCliente?.toLowerCase().includes(q) ||
        r.nombreDestino?.toLowerCase().includes(q)
      return matchesEstado && matchesQuery
    })
  }, [reservas, search, estadoFiltro])

  const stats = useMemo(() => {
    const total = reservas.length
    const confirmadas = reservas.filter((r) => r.estado === 'CONFIRMADA').length
    const pendientes = reservas.filter((r) => r.estado === 'PENDIENTE').length
    return { total, confirmadas, pendientes }
  }, [reservas])

  return (
    <div className="flex h-screen bg-page text-slate-900">

      <main className="overflow-y-auto thin-scroll">
        <div className="mx-auto max-w-[1400px] px-10 py-8">
          <header className="mb-7 flex items-start justify-between">
            <div>
              <p className="text-xs font-semibold uppercase tracking-wide text-brand-600">
                Gestión de reservas
              </p>
              <h1 className="text-3xl font-extrabold text-navy-950">Reservas</h1>
            </div>
            <button
              type="button"
              onClick={() => {
                setEditingReserva(null)
                setFormError('')
              }}
              className="flex items-center gap-2 rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-brand-700"
            >
              <Plus size={16} />
              Nueva reserva
            </button>
          </header>

          <section className="mb-6 flex gap-5">
            <StatCard label="Total" value={stats.total} />
            <StatCard label="Confirmadas" value={stats.confirmadas} />
            <StatCard label="Pendientes" value={stats.pendientes} />
          </section>

          <section className="mb-6 flex items-center gap-3">
            <div className="relative flex-1">
              <Search size={16} className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Buscar por cliente o destino"
                className="w-full rounded-xl border border-slate-200 bg-white py-2.5 pl-10 pr-4 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
              />
            </div>
            <select
              value={estadoFiltro}
              onChange={(e) => setEstadoFiltro(e.target.value)}
              className="rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm text-slate-700 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
            >
              <option value="TODOS">Todos los estados</option>
              {ESTADOS.map((estado) => (
                <option key={estado} value={estado}>
                  {ESTADO_LABELS[estado]}
                </option>
              ))}
            </select>
            <button
              type="button"
              onClick={cargarTodo}
              className="rounded-xl border border-slate-200 bg-white p-2.5 text-slate-500 hover:text-navy-950"
              title="Recargar"
            >
              <RefreshCw size={16} />
            </button>
          </section>

          <section className="flex items-start gap-6">
            <div className="flex-1 rounded-2xl border border-slate-200 bg-white">
              <div className="flex items-center justify-between border-b border-slate-100 px-6 py-4">
                <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Listado
                </p>
                <p className="text-xs text-slate-400">{reservasFiltradas.length} resultados</p>
              </div>
              <ReservasTable
                reservas={reservasFiltradas}
                loading={loading}
                onEdit={(r) => {
                  setEditingReserva(r)
                  setFormError('')
                }}
                onDelete={(r) => setToDelete(r)}
              />
            </div>

            <ReservaFormPanel
              clientes={clientes}
              destinos={destinos}
              editingReserva={editingReserva}
              onSubmit={handleSubmit}
              onCancelEdit={() => setEditingReserva(null)}
              submitting={submitting}
              error={formError}
            />
          </section>
        </div>
      </main>

      <ConfirmDialog
        open={Boolean(toDelete)}
        title="Eliminar reserva"
        message={`¿Seguro que deseas eliminar la reserva de ${toDelete?.nombreCliente ?? 'este cliente'}? Esta acción no se puede deshacer.`}
        onConfirm={confirmarEliminar}
        onCancel={() => setToDelete(null)}
        loading={deleting}
      />

      {toast && (
        <div
          className={[
            'fixed bottom-6 right-6 z-50 rounded-xl px-4 py-3 text-sm font-medium text-white shadow-lg',
            toast.type === 'error'
              ? 'bg-rose-600'
              : 'bg-brand-600',
          ].join(' ')}
        >
          {toast.message}
        </div>
      )}
    </div>
  )
}
