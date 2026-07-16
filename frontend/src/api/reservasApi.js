import { http } from './http'

const BASE = '/reservas'

// GET /api/reservas
export async function listarReservas() {
  const { data } = await http.get(BASE)
  return data
}

// GET /api/reservas/{id}
export async function obtenerReserva(id) {
  const { data } = await http.get(`${BASE}/${id}`)
  return data
}

// POST /api/reservas
// payload: { idCliente, idDestino, fechaViaje, cantPasajes, costoTotal, estado }
export async function crearReserva(payload) {
  const { data } = await http.post(BASE, payload)
  return data
}

// PUT /api/reservas/{id}
export async function actualizarReserva(id, payload) {
  const { data } = await http.put(`${BASE}/${id}`, payload)
  return data
}

// DELETE /api/reservas/{id}
export async function eliminarReserva(id) {
  await http.delete(`${BASE}/${id}`)
}
