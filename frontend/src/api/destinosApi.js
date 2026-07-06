import { http } from "./http";
const URL = "/destinos";

export async function listarDestinos() {
  const { data } = await http.get(URL);
  return data;
}

export async function obtenerDestino(id) {
  const { data } = await http.get(`${URL}/${id}`);
  return data;
}

export async function crearDestino(destino) {
  const { data } = await http.post(URL, destino);
  return data;
}

export async function actualizarDestino(id, destino) {
  const { data } = await http.put(`${URL}/${id}`, destino);
  return data;
}

export async function eliminarDestino(id) {
  await http.delete(`${URL}/${id}`);
}