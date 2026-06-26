import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/login/Login";
import DashboardLayout from "./layout/DashboardLayout";

import DashboardHome from "./pages/dashboard/Dashboard";
import Reservas from "./pages/reservas/ReservasPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Login />} />

        {/* LAYOUT */}
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<DashboardHome />} />
          <Route path="reservas" element={<Reservas />} />
        </Route>

      </Routes>
    </BrowserRouter>
  );
}