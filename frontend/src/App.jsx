import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/login/Login";
import DashboardLayout from "./layout/DashboardLayout";

import DashboardHome from "./pages/dashboard/Dashboard";
import Reservas from "./pages/reservas/ReservasPage";
import ClientesPage from "./pages/clientes/ClientesPage";

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
        </Route>

      </Routes>
    </BrowserRouter>
  );
}