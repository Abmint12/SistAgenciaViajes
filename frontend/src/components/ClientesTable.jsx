import { Pencil, Trash2 } from 'lucide-react'

export default function ClientesTable({ clientes, loading = false, onEdit, onDelete }) {
  if (loading) {
    return <div className="flex items-center justify-center py-16 text-sm text-slate-400">Cargando clientes...</div>
  }

  if (clientes.length === 0) {
    return <div className="flex items-center justify-center py-16 text-sm text-slate-400">No hay clientes para mostrar.</div>
  }

  return (
    <div className="overflow-hidden">
      <table className="w-full table-fixed text-left text-sm">
        <thead>
          <tr className="border-b border-slate-200 text-xs font-semibold uppercase tracking-wide text-slate-500">
            <th className="w-[5%] px-3 py-3">ID</th>
            <th className="w-[11%] px-3 py-3">Nombre</th>
            <th className="w-[11%] px-3 py-3">Apellido</th>
            <th className="w-[12%] px-3 py-3">Cédula</th>
            <th className="w-[12%] px-3 py-3">Teléfono</th>
            <th className="w-[20%] px-3 py-3">Correo</th>
            <th className="w-[21%] px-3 py-3">Dirección</th>
            <th className="w-[8%] px-3 py-3 text-right">Acciones</th>
          </tr>
        </thead>

        <tbody className="divide-y divide-slate-100">
          {clientes.map((cliente) => (
            <tr key={cliente.id} className="hover:bg-slate-50">
              <td className="px-3 py-4 text-slate-600">{cliente.id}</td>
              <td className="truncate px-3 py-4 font-medium text-navy-950">{cliente.nombre}</td>
              <td className="truncate px-3 py-4 text-slate-600">{cliente.apellido}</td>
              <td className="truncate px-3 py-4 text-slate-600">{cliente.cedula}</td>
              <td className="truncate px-3 py-4 text-slate-600">{cliente.telefono}</td>
              <td className="truncate px-3 py-4 text-slate-600" title={cliente.correo}>{cliente.correo}</td>
              <td className="truncate px-3 py-4 text-slate-600" title={cliente.direccion}>{cliente.direccion || '-'}</td>

              <td className="px-3 py-4">
                <div className="flex justify-end gap-1">
                  <button type="button" onClick={() => onEdit(cliente)} className="rounded-lg p-1.5 text-slate-500 hover:bg-slate-100 hover:text-navy-950">
                    <Pencil size={16} />
                  </button>
                  <button type="button" onClick={() => onDelete(cliente.id)} className="rounded-lg p-1.5 text-slate-500 hover:bg-rose-50 hover:text-rose-600">
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