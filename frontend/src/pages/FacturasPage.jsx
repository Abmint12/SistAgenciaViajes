import { useEffect, useState } from "react";
import { Search, Printer, Eye } from "lucide-react";

export default function FacturasPage() {
  const [facturas, setFacturas] = useState([]);
  const [busqueda, setBusqueda] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    obtenerFacturas();
  }, []);

  const obtenerFacturas = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/facturas");

      if (!response.ok) throw new Error();

      const data = await response.json();

      setFacturas(data);
    } catch (error) {
      console.error(error);

      // Datos de ejemplo mientras conectas el backend
      setFacturas([
        {
          id: 1,
          numeroFactura: "FAC-001",
          cliente: "Juan Pérez",
          fecha: "2026-07-08",
          total: 250,
          estado: "EMITIDA",
        },
        {
          id: 2,
          numeroFactura: "FAC-002",
          cliente: "María López",
          fecha: "2026-07-07",
          total: 480,
          estado: "EMITIDA",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const lista = facturas.filter((f) =>
    `${f.numeroFactura} ${f.cliente}`
      .toLowerCase()
      .includes(busqueda.toLowerCase())
  );

  return (
    <main className="flex-1 overflow-y-auto">

      <div className="mx-auto max-w-7xl px-10 py-8">

        <div className="flex items-center justify-between">

          <div>

            <h1 className="text-4xl font-bold text-navy-950">
              Facturas
            </h1>

            <p className="mt-2 text-slate-500">
              Administración de facturas emitidas.
            </p>

          </div>

          <div className="rounded-xl bg-green-600 px-6 py-3 font-semibold text-white">
            Total: {facturas.length}
          </div>

        </div>

        <div className="mt-8 rounded-2xl bg-white p-6 shadow">

          <div className="mb-6 flex items-center gap-3">

            <Search size={20} />

            <input
              type="text"
              placeholder="Buscar factura..."
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              className="w-full rounded-lg border p-3"
            />

          </div>

          {loading ? (

            <p>Cargando...</p>

          ) : (

            <table className="w-full">

              <thead>

                <tr className="border-b bg-slate-100">

                  <th className="p-3 text-left">Factura</th>
                  <th className="p-3 text-left">Cliente</th>
                  <th className="p-3 text-left">Fecha</th>
                  <th className="p-3 text-left">Total</th>
                  <th className="p-3 text-left">Estado</th>
                  <th className="p-3 text-center">Acciones</th>

                </tr>

              </thead>

              <tbody>

                {lista.map((f) => (

                  <tr
                    key={f.id}
                    className="border-b hover:bg-slate-50"
                  >

                    <td className="p-3 font-semibold">
                      {f.numeroFactura}
                    </td>

                    <td className="p-3">
                      {f.cliente}
                    </td>

                    <td className="p-3">
                      {f.fecha}
                    </td>

                    <td className="p-3">
                      ${f.total}
                    </td>

                    <td className="p-3">

                      <span className="rounded-full bg-green-100 px-3 py-1 text-sm text-green-700">
                        {f.estado}
                      </span>

                    </td>

                    <td className="p-3">

                      <div className="flex justify-center gap-2">

                        <button className="rounded-lg bg-blue-600 p-2 text-white hover:bg-blue-700">
                          <Eye size={18} />
                        </button>

                        <button className="rounded-lg bg-slate-700 p-2 text-white hover:bg-slate-800">
                          <Printer size={18} />
                        </button>

                      </div>

                    </td>

                  </tr>

                ))}

              </tbody>

            </table>

          )}

        </div>

      </div>

    </main>
  );
}