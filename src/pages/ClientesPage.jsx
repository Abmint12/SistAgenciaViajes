import { useEffect, useMemo, useState } from 'react'
import Sidebar from '../components/Sidebar'
import StatCard from '../components/StatCard'
import ClientesTable from '../components/ClientesTable'
import ClienteFormPanel from '../components/ClienteFormPanel'
import {
  listarClientes,
  crearCliente,
  actualizarCliente,
  eliminarCliente
} from '../api/clientesApi'

export default function ClientesPage() {
  const [clientes, setClientes] = useState([])
  const [search, setSearch] = useState('')
  const [editingCliente, setEditingCliente] = useState(null)
  const [backendOnline, setBackendOnline] = useState(true)

  async function cargarClientes() {
  try {
    const data = await listarClientes()
    setClientes(data.sort((a, b) => b.id - a.id))
    setBackendOnline(true)

    alert("✅ Clientes actualizados correctamente.")
  } catch (error) {
    setBackendOnline(false)
    alert("❌ No se pudieron actualizar los clientes.")
  }
}

  useEffect(() => {
    cargarClientes()
  }, [])

  const clientesFiltrados = useMemo(() => {
    const q = search.toLowerCase()
    return clientes.filter((c) =>
      String(c.nombre ?? '').toLowerCase().includes(q) ||
      String(c.correo ?? '').toLowerCase().includes(q) ||
      String(c.telefono ?? '').toLowerCase().includes(q)
    )
  }, [clientes, search])

  async function guardarCliente(cliente) {
    try {
      if (editingCliente) {
        const actualizado = await actualizarCliente(editingCliente.id, cliente)
        setClientes(clientes.map((c) => c.id === editingCliente.id ? actualizado : c))
        setEditingCliente(null)
      } else {
        const nuevo = await crearCliente(cliente)
        setClientes([nuevo, ...clientes].sort((a, b) => b.id - a.id))
      }
    } catch (error) {
      alert('No se pudo guardar el cliente. Verifica que el backend esté encendido.')
    }
  }

  async function eliminar(id) {
    const confirmar = confirm('¿Seguro que deseas eliminar este cliente?')
    if (!confirmar) return

    try {
      await eliminarCliente(id)
      setClientes(clientes.filter((c) => c.id !== id))
    } catch (error) {
      alert('No se pudo eliminar el cliente.')
    }
  }

  return (
    <div className="dashboard">
      <Sidebar active="clientes" backendOnline={backendOnline} />

      <main className="content">
        <header className="header">
          <div>
            <span className="section-label">GESTIÓN DE CLIENTES</span>
            <h1>Clientes</h1>
          </div>
          <button className="primary-btn" onClick={() => setEditingCliente(null)}>
            + Nuevo cliente
          </button>
        </header>

        <section className="stats">
          <StatCard label="Total" value={clientes.length} />
          <StatCard label="Activos" value={clientes.length} />
          <StatCard label="Registrados hoy" value={clientes.length} />
        </section>

        <section className="toolbar">
          <input
            type="text"
            placeholder="Buscar por nombre, correo o teléfono"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button onClick={cargarClientes}>Actualizar</button>
        </section>

        <section className="work-area">
          <ClientesTable
            clientes={clientesFiltrados}
            onEdit={setEditingCliente}
            onDelete={eliminar}
          />

          <ClienteFormPanel
            onSave={guardarCliente}
            editingCliente={editingCliente}
            onCancel={() => setEditingCliente(null)}
          />
        </section>
      </main>
    </div>
  )
}