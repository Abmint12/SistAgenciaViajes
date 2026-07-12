import { useEffect, useState } from "react";
import { Search, Printer, Eye, Plus, Ban } from "lucide-react";
import {useNavigate} from "react-router-dom";

export default function FacturasPage() {
  const navigate=useNavigate();
  const [facturas, setFacturas] = useState([]);
  const [pagos, setPagos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busqueda, setBusqueda] = useState("");

  const [mostrarModal, setMostrarModal] = useState(false);

  const [form, setForm] = useState({
    pagoId: "",
    numeroFactura: "",
    subtotal: "",
    impuesto: "",
    total: ""
  });

  useEffect(() => {
    obtenerFacturas();
    obtenerPagos();
  }, []);

  const obtenerFacturas = async () => {

    try {

      const response = await fetch(
        "http://localhost:8080/api/facturas"
      );

      const data = await response.json();

      setFacturas(data);

    } catch (e) {

      console.error(e);

    } finally {

      setLoading(false);

    }

  };

  const obtenerPagos = async () => {

    try {

      const response = await fetch(
        "http://localhost:8080/api/pagos/pagados"
      );

      const data = await response.json();

      setPagos(data);

    } catch (e) {

      console.error(e);

    }

  };

  const handleChange = (e) => {

    setForm({
      ...form,
      [e.target.name]: e.target.value
    });

  };

  const emitirFactura = async () => {

    try {

      const response = await fetch(
        "http://localhost:8080/api/facturas",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({

            numeroFactura: form.numeroFactura,

            subtotal: Number(form.subtotal),

            impuesto: Number(form.impuesto),

            total: Number(form.total),

            pago: {
              id: Number(form.pagoId)
            }

          })

        }
      );

      if (!response.ok)
        throw new Error();

      alert("Factura emitida correctamente.");

      setMostrarModal(false);

      obtenerFacturas();

      setForm({
        pagoId: "",
        numeroFactura: "",
        subtotal: "",
        impuesto: "",
        total: ""
      });

    } catch (e) {

      alert("No se pudo emitir la factura.");

    }

  };

  const anularFactura = async (id) => {

    if (!window.confirm("¿Desea anular la factura?"))
      return;

    try {

      await fetch(
        `http://localhost:8080/api/facturas/${id}`,
        {
          method: "DELETE"
        }
      );

      obtenerFacturas();

    } catch (e) {

      alert("Error al anular la factura.");

    }

  };

  const lista = facturas.filter((f) =>

    `${f.numeroFactura}
     ${f.pago?.reserva?.cliente?.nombre ?? ""}
     ${f.pago?.reserva?.cliente?.apellido ?? ""}`

      .toLowerCase()
      .includes(busqueda.toLowerCase())

  );
  return (
    <main className="flex-1 overflow-y-auto">
      <div className="mx-auto max-w-7xl px-10 py-8">

        {/* Encabezado */}

        <div className="mb-8 flex items-center justify-between">

          <div>

            <h1 className="text-4xl font-bold text-navy-950">
              Facturas
            </h1>

            <p className="mt-2 text-slate-500">
              Emisión, consulta y anulación de facturas.
            </p>

          </div>

          <div className="flex items-center gap-4">
            <button
            onClick={()=>navigate ("/Dashboard/pagos")}
            className="rounded-x1 bg-slate-600 px-5 py-3 font-semibold text-white transition hover:bg-slate-700"
            >
              ← Regresar
            </button>
            <div className="rounded-xl bg-green-600 px-5 py-3 font-semibold text-white">
              Total: {facturas.length}
            </div>

            <button
              onClick={() => setMostrarModal(true)}
              className="flex items-center gap-2 rounded-xl bg-brand-600 px-5 py-3 font-semibold text-white hover:bg-brand-700"
            >
              <Plus size={20} />
              Emitir Factura
            </button>

          </div>

        </div>

        {/* Buscador */}

        <div className="mb-6 rounded-2xl bg-white p-6 shadow">

          <div className="flex items-center gap-3">

            <Search size={20} />

            <input
              type="text"
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              placeholder="Buscar factura..."
              className="w-full rounded-lg border p-3"
            />

          </div>

        </div>

        {/* Tabla */}

        <div className="overflow-hidden rounded-2xl bg-white shadow">

          {loading ? (

            <div className="p-8 text-center">
              Cargando...
            </div>

          ) : (

            <table className="w-full">

              <thead className="bg-slate-100">

                <tr>

                  <th className="p-4 text-left">Factura</th>

                  <th className="p-4 text-left">Cliente</th>

                  <th className="p-4 text-left">Fecha</th>

                  <th className="p-4 text-left">Total</th>

                  <th className="p-4 text-left">Estado</th>

                  <th className="p-4 text-center">
                    Acciones
                  </th>

                </tr>

              </thead>

              <tbody>

                {lista.map((f) => (

                  <tr
                    key={f.id}
                    className="border-b hover:bg-slate-50"
                  >

                    <td className="p-4 font-semibold">

                      {f.numeroFactura}

                    </td>

                    <td className="p-4">

                      {f.pago?.reserva?.cliente?.nombre}{" "}
                      {f.pago?.reserva?.cliente?.apellido}

                    </td>

                    <td className="p-4">

                      {f.fechaEmision?.substring(0, 10)}

                    </td>

                    <td className="p-4">

                      ${f.total}

                    </td>

                    <td className="p-4">

                      <span
                        className={`rounded-full px-3 py-1 text-sm font-semibold
                        ${f.estado === "ANULADA"
                            ? "bg-red-100 text-red-700"
                            : "bg-green-100 text-green-700"
                          }`}
                      >
                        {f.estado}
                      </span>

                    </td>

                    <td className="p-4">

                      <div className="flex justify-center gap-2">

                        <button
                          className="rounded-lg bg-blue-600 p-2 text-white hover:bg-blue-700"
                        >
                          <Eye size={18} />
                        </button>

                        <button
                          className="rounded-lg bg-slate-700 p-2 text-white hover:bg-slate-800"
                        >
                          <Printer size={18} />
                        </button>

                        <button
                          onClick={() => anularFactura(f.id)}
                          className="rounded-lg bg-red-600 p-2 text-white hover:bg-red-700"
                        >
                          <Ban size={18} />
                        </button>

                      </div>

                    </td>

                  </tr>

                ))}

              </tbody>

            </table>

          )}

        </div>
        {/* Modal Emitir Factura */}

        {mostrarModal && (

          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">

            <div className="w-full max-w-xl rounded-2xl bg-white p-8 shadow-xl">

              <h2 className="mb-6 text-2xl font-bold text-navy-950">
                Emitir Factura
              </h2>

              <div className="grid gap-4">

                <div>

                  <label className="mb-2 block font-semibold">
                    Pago
                  </label>

                  <select
                    name="pagoId"
                    value={form.pagoId}
                    onChange={handleChange}
                    className="w-full rounded-lg border p-3"
                  >

                    <option value="">
                      Seleccione un pago
                    </option>

                    {pagos.map((pago) => (

                      <option
                        key={pago.id}
                        value={pago.id}
                      >
                        Pago #{pago.id} -
                        {pago.reserva?.cliente?.nombre}{" "}
                        {pago.reserva?.cliente?.apellido}
                      </option>

                    ))}

                  </select>

                </div>

                <div>

                  <label className="mb-2 block font-semibold">
                    Número Factura
                  </label>

                  <input
                    name="numeroFactura"
                    value={form.numeroFactura}
                    onChange={handleChange}
                    className="w-full rounded-lg border p-3"
                    placeholder="FAC-000001"
                  />

                </div>

                <div className="grid grid-cols-3 gap-4">

                  <div>

                    <label className="mb-2 block font-semibold">
                      Subtotal
                    </label>

                    <input
                      type="number"
                      step="0.01"
                      name="subtotal"
                      value={form.subtotal}
                      onChange={handleChange}
                      className="w-full rounded-lg border p-3"
                    />

                  </div>

                  <div>

                    <label className="mb-2 block font-semibold">
                      IVA
                    </label>

                    <input
                      type="number"
                      step="0.01"
                      name="impuesto"
                      value={form.impuesto}
                      onChange={handleChange}
                      className="w-full rounded-lg border p-3"
                    />

                  </div>

                  <div>

                    <label className="mb-2 block font-semibold">
                      Total
                    </label>

                    <input
                      type="number"
                      step="0.01"
                      name="total"
                      value={form.total}
                      onChange={handleChange}
                      className="w-full rounded-lg border p-3"
                    />

                  </div>

                </div>

              </div>

              <div className="mt-8 flex justify-end gap-4">

                <button
                  onClick={() => setMostrarModal(false)}
                  className="rounded-xl border px-6 py-3 font-semibold"
                >
                  Cancelar
                </button>

                <button
                  onClick={emitirFactura}
                  className="rounded-xl bg-green-600 px-6 py-3 font-semibold text-white hover:bg-green-700"
                >
                  Emitir Factura
                </button>

              </div>

            </div>

          </div>

        )}

      </div>

    </main>

  );

}