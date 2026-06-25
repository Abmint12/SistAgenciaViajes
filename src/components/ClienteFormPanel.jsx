import { useEffect, useState } from 'react'

export default function ClienteFormPanel({
  onSave,
  editingCliente,
  onCancel
}) {
  const [form, setForm] = useState({
    nombre: '',
    apellido: '',
    cedula: '',
    telefono: '',
    correo: ''
  })

  const limpiarFormulario = () => {
    setForm({
      nombre: '',
      apellido: '',
      cedula: '',
      telefono: '',
      correo: ''
    })
  }

  useEffect(() => {
  if (editingCliente) {
    setForm({
      nombre: editingCliente.nombre ?? '',
      apellido: editingCliente.apellido ?? '',
      cedula: editingCliente.cedula ?? '',
      telefono: editingCliente.telefono ?? '',
      correo: editingCliente.correo ?? ''
    });
  } else {
    limpiarFormulario();
  }
}, [editingCliente]);

  
  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    await onSave(form)
    limpiarFormulario()
  }

  return (
    <form className="client-form" onSubmit={handleSubmit}>
      <h3>{editingCliente ? 'Editar Cliente' : 'Nuevo Cliente'}</h3>

      <input name="nombre" placeholder="Nombre" value={form.nombre} onChange={handleChange} />
      <input name="apellido" placeholder="Apellido" value={form.apellido} onChange={handleChange} />
      <input name="cedula" placeholder="Cédula" value={form.cedula} onChange={handleChange} />
      <input name="telefono" placeholder="Teléfono" value={form.telefono} onChange={handleChange} />
      <input name="correo" placeholder="Correo" value={form.correo} onChange={handleChange}/>

      <button type="submit">
        Guardar
      </button>

      {editingCliente && (
        <button type="button" onClick={onCancel}>
          Cancelar
        </button>
      )}
    </form>
  )
}