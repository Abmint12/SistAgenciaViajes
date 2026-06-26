import { useEffect, useState } from 'react'

export default function ClienteFormPanel({ onSave, editingCliente, onCancel }) {
  const emptyForm = {
    nombre: '',
    apellido: '',
    cedula: '',
    telefono: '',
    correo: '',
    direccion: ''
  }

  const [form, setForm] = useState(emptyForm)

  useEffect(() => {
    if (editingCliente) {
      setForm({
        nombre: editingCliente.nombre ?? '',
        apellido: editingCliente.apellido ?? '',
        cedula: editingCliente.cedula ?? '',
        telefono: editingCliente.telefono ?? '',
        correo: editingCliente.correo ?? '',
        direccion: editingCliente.direccion ?? ''
      })
    } else {
      setForm(emptyForm)
    }
  }, [editingCliente])

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    await onSave(form)

    if (!editingCliente) {
      setForm(emptyForm)
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="w-[330px] shrink-0 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
    >
      <p className="text-xs font-semibold uppercase tracking-wide text-brand-600">
        Registro
      </p>

      <h2 className="mb-5 text-lg font-extrabold text-navy-950">
        {editingCliente ? 'Editar cliente' : 'Nuevo cliente'}
      </h2>

      <div className="space-y-3">
        <input
          name="nombre"
          placeholder="Nombre"
          value={form.nombre}
          onChange={handleChange}
          className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
        />

        <input
          name="apellido"
          placeholder="Apellido"
          value={form.apellido}
          onChange={handleChange}
          className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
        />

        <input
          name="cedula"
          placeholder="Cédula"
          value={form.cedula}
          onChange={handleChange}
          className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
        />

        <input
          name="telefono"
          placeholder="Teléfono"
          value={form.telefono}
          onChange={handleChange}
          className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
        />

        <input
          name="correo"
          placeholder="Correo"
          value={form.correo}
          onChange={handleChange}
          className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
        />

        <input
          name="direccion"
          placeholder="Dirección"
          value={form.direccion}
          onChange={handleChange}
          className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
        />

        <button
          type="submit"
          className="mt-2 w-full rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-brand-700"
        >
          {editingCliente ? 'Actualizar cliente' : 'Guardar cliente'}
        </button>

        {editingCliente && (
          <button
            type="button"
            onClick={onCancel}
            className="w-full rounded-lg border border-slate-200 px-4 py-2.5 text-sm font-semibold text-slate-600 hover:bg-slate-50"
          >
            Cancelar
          </button>
        )}
      </div>
    </form>
  )
}