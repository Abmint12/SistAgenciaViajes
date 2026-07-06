import { CreditCard, Receipt } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function PagosPage() {
  const navigate = useNavigate();

  return (
    <div className="mx-auto max-w-5xl px-10 py-8">
      <h1 className="mb-8 text-3xl font-bold text-navy-950">
        Gestión de Pagos
      </h1>

      <div className="grid gap-6 md:grid-cols-2">

        <div
          onClick={() => navigate("/dashboard/pagos/registrar")}
          className="cursor-pointer rounded-2xl border border-slate-200 bg-white p-8 shadow-sm transition hover:shadow-lg"
        >
          <CreditCard className="mb-4 text-brand-600" size={40} />

          <h2 className="text-xl font-semibold">
            Registrar Pago
          </h2>

          <p className="mt-2 text-slate-500">
            Registrar pagos realizados por los clientes.
          </p>
        </div>

        <div
          onClick={() => navigate("/dashboard/facturas")}
          className="cursor-pointer rounded-2xl border border-slate-200 bg-white p-8 shadow-sm transition hover:shadow-lg"
        >
          <Receipt className="mb-4 text-brand-600" size={40} />

          <h2 className="text-xl font-semibold">
            Emitir Factura
          </h2>

          <p className="mt-2 text-slate-500">
            Generar la factura de una reserva pagada.
          </p>
        </div>

      </div>
    </div>
  );
}