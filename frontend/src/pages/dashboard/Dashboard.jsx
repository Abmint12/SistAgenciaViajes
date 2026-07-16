import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";

export default function Dashboard() {

  const { usuario } = useContext(AuthContext);

  return (

    <main className="flex-1 overflow-y-auto">

      <div className="mx-auto max-w-7xl px-10 py-10">

        <div className="rounded-3xl bg-gradient-to-r from-blue-600 to-cyan-500 p-10 text-white shadow-lg">

          <p className="text-sm uppercase tracking-widest opacity-80">
            Sistema Administrativo
          </p>

          <h1 className="mt-3 text-5xl font-extrabold">
            ¡Bienvenido, {usuario?.nombreUsuario}!
          </h1>

          <p className="mt-4 text-lg opacity-90">
            Administre reservas, clientes, destinos, pagos y facturas desde un solo lugar.
          </p>

        </div>

      </div>

    </main>

  );

}