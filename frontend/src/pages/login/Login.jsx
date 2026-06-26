import { useState, useContext } from "react";
import { loginRequest } from "../../api/auth";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import "./Login.css";

export default function Login() {
  const [nombreUsuario, setNombreUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const res = await loginRequest(nombreUsuario, contrasena);

      login(res.data);
      navigate("/dashboard");
    } catch (error) {
      alert("Credenciales incorrectas");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">

        {/* HEADER */}
        <div className="login-header">
          <div className="logo">LOGO</div>
          <h1>Agencia de Viajes</h1>
          <p>Sistema de Reservas, Pagos y Facturación</p>
        </div>

        {/* FORM */}
        <form className="login-form" onSubmit={handleLogin}>
          <label>👤 Usuario</label>
          <input
            type="text"
            placeholder="Usuario"
            value={nombreUsuario}
            onChange={(e) => setNombreUsuario(e.target.value)}
          />

          <label>🔒 Contraseña</label>
          <input
            type="password"
            placeholder="Contraseña"
            value={contrasena}
            onChange={(e) => setContrasena(e.target.value)}
          />

          <button type="submit">Ingresar</button>
        </form>

        {/* FOOTER */}
        <div className="footer">
          Pantalla de inicio de sesión | Sistema Administrativo
        </div>

      </div>
    </div>
  );
}