import { http } from './http'

const BASE = '/clientes'

export async function listarClientes() {
  const { data } = await http.get(BASE)
  return data
}

export async function crearCliente(payload) {
  const { data } = await http.post(BASE, payload)
  return data
}

export async function actualizarCliente(id, payload) {
  const { data } = await http.put(`${BASE}/${id}`, payload)
  return data
}

export async function eliminarCliente(id) {
  await http.delete(`${BASE}/${id}`)
}