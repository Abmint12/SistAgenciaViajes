import { http } from './http'

const BASE = '/clientes'

// GET /api/clientes
export async function listarClientes() {
  const { data } = await http.get(BASE)
  return data
}
