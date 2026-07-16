import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/login/Login";
import DashboardLayout from "./layout/DashboardLayout";

import DashboardHome from "./pages/dashboard/Dashboard";
import Reservas from "./pages/reservas/ReservasPage";
import ClientesPage from "./pages/clientes/ClientesPage";
import DestinosPage from "./pages/DestinosPage";
import PagosHome from "./pages/pagos/PagosHome";
import PagosPage from "./pages/pagos/PagosPage";
import FacturasPage from "./pages/FacturasPage"

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* LOGIN */}
        <Route path="/" element={<Login />} />

        {/* DASHBOARD */}
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<DashboardHome />} />
          <Route path="reservas" element={<Reservas />} />
          <Route path="clientes" element={<ClientesPage />} />
          <Route path="destinos" element={<DestinosPage />} />
          <Route path="pagos" element={<PagosHome />} />

          <Route
            path="pagos/registrar"
            element={<PagosPage />}
          />

          <Route
            path="pagos/facturas"
            element={<FacturasPage />}
          />

        </Route>
      </Routes>
    </BrowserRouter>
  );
}