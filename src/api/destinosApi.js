import { http } from './http'

const BASE = '/destinos'

// GET /api/destinos
export async function listarDestinos() {
  const { data } = await http.get(BASE)
  return data
}
