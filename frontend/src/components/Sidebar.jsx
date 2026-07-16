import { NavLink, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  Plane,
  Users,
  MapPin,
  CalendarCheck,
  CreditCard,
  Receipt,
  LogOut,
} from "lucide-react";

const NAV_ITEMS = [
  {
    label: "Dashboard",
    icon: LayoutDashboard,
    path: "/dashboard",
  },
  {
    label: "Clientes",
    icon: Users,
    path: "/dashboard/clientes",
  },
  {
    label: "Destinos",
    icon: MapPin,
    path: "/dashboard/destinos",
  },
  {
    label: "Reservas",
    icon: CalendarCheck,
    path: "/dashboard/reservas",
  },
  {
    label: "Pagos",
    icon: CreditCard,
    path: "/dashboard/pagos",
  },
  
];

export default function Sidebar({ backendOnline = true }) {
  const navigate = useNavigate();

  return (
    <aside className="flex h-screen w-[270px] flex-col bg-navy-950 text-slate-300">

      {/* Logo */}
      <div className="flex items-center gap-3 border-b border-navy-800 px-6 py-6">
        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-brand-500">
          <Plane size={22} className="text-white" />
        </div>

        <div>
          <h2 className="font-bold text-white">
            Agencia Viajes
          </h2>

          <p className="text-xs text-slate-400">
            Panel Administrativo
          </p>
        </div>
      </div>

      {/* Navegación */}
      <nav className="flex-1 px-4 py-5">

        <p className="mb-3 px-3 text-xs uppercase tracking-widest text-slate-500">
          Navegación
        </p>

        <div className="space-y-2">

          {NAV_ITEMS.map(({ label, icon: Icon, path }) => (
            <NavLink
              key={path}
              to={path}
              end={path === "/dashboard"}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition-all duration-200 ${
                  isActive
                    ? "bg-brand-500 text-white shadow-md"
                    : "text-slate-300 hover:bg-navy-900 hover:text-white"
                }`
              }
            >
              <Icon size={18} />
              <span>{label}</span>
            </NavLink>
          ))}

        </div>

      </nav>

      {/* Estado Backend */}
      <div className="mx-4 mb-4 rounded-xl bg-navy-900 p-4">

        <div className="flex items-center gap-3">

          <span
            className={`h-3 w-3 rounded-full ${
              backendOnline ? "bg-green-400" : "bg-red-500"
            }`}
          />

          <div>

            <p className="text-sm font-semibold text-white">
              {backendOnline
                ? "Backend conectado"
                : "Backend desconectado"}
            </p>

            <p className="text-xs text-slate-400">
              Estado del servidor
            </p>

          </div>

        </div>

      </div>

      {/* Botón Cerrar sesión */}
      <div className="border-t border-navy-800 p-4">

        <button
          onClick={() => navigate("/")}
          className="flex w-full items-center gap-3 rounded-xl px-4 py-3 text-slate-300 transition-all duration-200 hover:bg-red-600 hover:text-white"
        >
          <LogOut size={18} />
          <span>Cerrar sesión</span>
        </button>

      </div>

    </aside>
  );
}