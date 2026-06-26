import { useEffect, useMemo, useState } from 'react'
import { Plus, RefreshCw, Search } from 'lucide-react'
import Sidebar from '../../components/Sidebar'
import StatCard from '../../components/StatCard'
import ClientesTable from '../../components/ClientesTable'
import ClienteFormPanel from '../../components/ClienteFormPanel'
import {
  listarClientes,
  crearCliente,
  actualizarCliente,
  eliminarCliente
} from '../../api/clientesApi'

export default function ClientesPage() {
  const [clientes, setClientes] = useState([])
  const [search, setSearch] = useState('')
  const [editingCliente, setEditingCliente] = useState(null)
  const [backendOnline, setBackendOnline] = useState(true)
  const [loading, setLoading] = useState(true)

  async function cargarClientes() {
    setLoading(true)

    try {
      const data = await listarClientes()
      setClientes(data.sort((a, b) => b.id - a.id))
      setBackendOnline(true)
    } catch (error) {
      setBackendOnline(false)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    cargarClientes()
  }, [])

  const clientesFiltrados = useMemo(() => {
    const q = search.trim().toLowerCase()

    return clientes.filter((c) =>
      String(c.nombre ?? '').toLowerCase().includes(q) ||
      String(c.apellido ?? '').toLowerCase().includes(q) ||
      String(c.cedula ?? '').toLowerCase().includes(q) ||
      String(c.correo ?? '').toLowerCase().includes(q) ||
      String(c.telefono ?? '').toLowerCase().includes(q)
    )
  }, [clientes, search])

  async function guardarCliente(cliente) {
    try {
      if (editingCliente) {
        const actualizado = await actualizarCliente(editingCliente.id, cliente)

        setClientes((prev) =>
          prev.map((c) =>
            c.id === editingCliente.id ? actualizado : c
          )
        )

        setEditingCliente(null)

      } else {

        const nuevo = await crearCliente(cliente)

        setClientes((prev) =>
          [nuevo, ...prev].sort((a, b) => b.id - a.id)
        )

      }

    } catch (error) {
      console.error(error)
    }
  }

  async function eliminar(id) {

    const confirmar = confirm('¿Seguro que deseas eliminar este cliente?')

    if (!confirmar) return

    try {

      await eliminarCliente(id)

      setClientes((prev) =>
        prev.filter((c) => c.id !== id)
      )

    } catch (error) {
      console.error(error)
    }

  }

  return (
    <div className="flex h-screen bg-page text-slate-900">

      <Sidebar
        active="clientes"
        backendOnline={backendOnline}
      />

      <main className="flex-1 overflow-y-auto thin-scroll">

        <div className="mx-auto max-w-[1400px] px-10 py-8">

          <header className="mb-7 flex items-start justify-between">

            <div>

              <p className="text-xs font-semibold uppercase tracking-wide text-brand-600">
                Gestión de clientes
              </p>

              <h1 className="text-3xl font-extrabold text-navy-950">
                Clientes
              </h1>

            </div>

            <button
              type="button"
              onClick={() => setEditingCliente(null)}
              className="flex items-center gap-2 rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-brand-700"
            >
              <Plus size={16} />
              Nuevo cliente
            </button>

          </header>

          <section className="mb-6 flex gap-5">

            <StatCard
              label="Total"
              value={clientes.length}
            />

            <StatCard
              label="Activos"
              value={clientes.length}
            />

            <StatCard
              label="Registrados hoy"
              value={clientes.length}
            />

          </section>

          <section className="mb-6 flex items-center gap-3">

            <div className="relative flex-1">

              <Search
                size={16}
                className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
              />

              <input
                type="text"
                placeholder="Buscar por nombre, apellido, cédula, correo o teléfono"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-white py-2.5 pl-10 pr-4 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
              />

            </div>

            <button
              type="button"
              onClick={cargarClientes}
              className="rounded-xl border border-slate-200 bg-white p-2.5 text-slate-500 hover:text-navy-950"
              title="Recargar"
            >
              <RefreshCw size={16} />
            </button>

          </section>

          {/* TABLA + FORMULARIO */}

          <section className="grid grid-cols-[1fr_300px] items-start gap-4">

            <div className="min-w-0 rounded-2xl border border-slate-200 bg-white">

              <div className="flex items-center justify-between border-b border-slate-100 px-6 py-4">

                <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Listado
                </p>

                <p className="text-xs text-slate-400">
                  {clientesFiltrados.length} resultados
                </p>

              </div>

              <ClientesTable
                clientes={clientesFiltrados}
                loading={loading}
                onEdit={setEditingCliente}
                onDelete={eliminar}
              />

            </div>

            <ClienteFormPanel
              onSave={guardarCliente}
              editingCliente={editingCliente}
              onCancel={() => setEditingCliente(null)}
            />

          </section>

        </div>

      </main>

    </div>
  )
}