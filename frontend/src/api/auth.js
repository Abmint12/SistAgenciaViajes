import axios from "axios";

const API = "http://localhost:8080/api/auth";

export const loginRequest = (nombreUsuario, contrasena) => {
  return axios.post(`${API}/login`, {
    nombreUsuario,
    contrasena
  });
};