import { useNavigate } from "react-router-dom";
import { CreditCard, Receipt } from "lucide-react";

export default function PagosHome() {

  const navigate = useNavigate();

  return (

    <main className="flex-1 overflow-y-auto">

      <div className="mx-auto max-w-6xl px-10 py-10">

        <div className="mb-10">

          <p className="text-xs font-semibold uppercase tracking-widest text-brand-600">
            Gestión Financiera
          </p>

          <h1 className="mt-2 text-4xl font-extrabold text-navy-950">
            Pagos
          </h1>

          <p className="mt-2 text-slate-500">
            Seleccione la operación que desea realizar.
          </p>

        </div>

        <div className="grid gap-8 md:grid-cols-2">

          {/* Registrar Pago */}

          <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm transition hover:shadow-xl">

            <div className="mb-6 flex h-16 w-16 items-center justify-center rounded-2xl bg-brand-100">

              <CreditCard
                size={34}
                className="text-brand-600"
              />

            </div>

            <h2 className="text-2xl font-bold text-navy-950">

              Registrar Pago

            </h2>

            <p className="mt-3 text-slate-500">

              Registrar el pago correspondiente a una reserva.

            </p>

            <button

              onClick={() => navigate("/dashboard/pagos/registrar")}

              className="mt-8 rounded-xl bg-brand-600 px-6 py-3 font-semibold text-white hover:bg-brand-700"

            >

              Ingresar

            </button>

          </div>

          {/* Emitir Factura */}

          <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm transition hover:shadow-xl">

            <div className="mb-6 flex h-16 w-16 items-center justify-center rounded-2xl bg-green-100">

              <Receipt
                size={34}
                className="text-green-600"
              />

            </div>

            <h2 className="text-2xl font-bold text-navy-950">

              Emitir Factura

            </h2>

            <p className="mt-3 text-slate-500">

              Generar la factura electrónica de un pago registrado.

            </p>

            <button

              onClick={() => navigate("/dashboard/pagos/facturas")}

              className="mt-8 rounded-xl bg-green-600 px-6 py-3 font-semibold text-white hover:bg-green-700"

            >

              Ingresar

            </button>

          </div>

        </div>

      </div>

    </main>

  );

}