import axios from 'axios'

/**
 * Backend Spring Boot corre por defecto en localhost:8080 (ver
 * src/main/resources/application.properties del proyecto backend, que no
 * define server.port). Todos los controllers cuelgan de /api/**.
 *
 * Se puede sobreescribir con una variable de entorno VITE_API_URL en un
 * archivo .env si el backend corre en otra máquina/puerto.
 */
const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export const http = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Normaliza errores de Axios a un mensaje legible para mostrar en la UI.
export function getErrorMessage(error) {
  if (error.response) {
    const { status, data } = error.response
    if (data?.message) return data.message
    if (status === 404) return 'Recurso no encontrado.'
    if (status === 400) return 'Datos inválidos. Revisa el formulario.'
    if (status >= 500) return 'Error interno del servidor.'
    return `Error ${status} al comunicarse con el backend.`
  }
  if (error.request) {
    return 'No se pudo conectar con el backend. Verifica que esté corriendo en ' + baseURL + '.'
  }
  return error.message || 'Ocurrió un error inesperado.'
}
