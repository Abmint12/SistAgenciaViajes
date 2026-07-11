import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function PagosPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    reservaId: "",
    metodoPago: "EFECTIVO",
    monto: "",
    estado: "PAGADO",
    fechaPago: new Date().toISOString().split("T")[0],
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const guardarPago = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/pagos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          reserva: {
            id: Number(formData.reservaId),
          },
          metodoPago: formData.metodoPago,
          monto: Number(formData.monto),
          estado: formData.estado,
          fechaPago: formData.fechaPago,
        }),
      });

      if (!response.ok) {
        throw new Error("Error al registrar el pago");
      }

      alert("Pago registrado correctamente.");

      navigate("/dashboard/pagos");

    } catch (error) {
      console.error(error);
      alert("No se pudo registrar el pago.");
    }
  };

  return (
    <div className="mx-auto max-w-4xl px-10 py-8">

      <h1 className="mb-8 text-3xl font-bold text-navy-950">
        Registrar Pago
      </h1>

      <form
        onSubmit={guardarPago}
        className="rounded-2xl bg-white p-8 shadow space-y-6"
      >

        <div>

          <label className="mb-2 block font-semibold">
            ID Reserva
          </label>

          <input
            type="number"
            name="reservaId"
            value={formData.reservaId}
            onChange={handleChange}
            className="w-full rounded-lg border p-3"
            required
          />

        </div>

        <div>

          <label className="mb-2 block font-semibold">
            Método de Pago
          </label>

          <select
            name="metodoPago"
            value={formData.metodoPago}
            onChange={handleChange}
            className="w-full rounded-lg border p-3"
          >

            <option value="EFECTIVO">Efectivo</option>
            <option value="TARJETA">Tarjeta</option>
            <option value="TRANSFERENCIA">Transferencia</option>

          </select>

        </div>

        <div>

          <label className="mb-2 block font-semibold">
            Monto
          </label>

          <input
            type="number"
            step="0.01"
            name="monto"
            value={formData.monto}
            onChange={handleChange}
            className="w-full rounded-lg border p-3"
            required
          />

        </div>

        <div>

          <label className="mb-2 block font-semibold">
            Estado
          </label>

          <select
            name="estado"
            value={formData.estado}
            onChange={handleChange}
            className="w-full rounded-lg border p-3"
          >

            <option value="PENDIENTE">Pendiente</option>
            <option value="PAGADO">Pagado</option>

          </select>

        </div>

        <div>

          <label className="mb-2 block font-semibold">
            Fecha
          </label>

          <input
            type="date"
            name="fechaPago"
            value={formData.fechaPago}
            onChange={handleChange}
            className="w-full rounded-lg border p-3"
          />

        </div>

        <div className="flex gap-4">

          <button
            type="submit"
            className="rounded-xl bg-brand-600 px-6 py-3 font-semibold text-white hover:bg-brand-700"
          >
            Guardar Pago
          </button>

          <button
            type="button"
            onClick={() => navigate("/dashboard/pagos")}
            className="rounded-xl bg-gray-300 px-6 py-3 font-semibold hover:bg-gray-400"
          >
            Cancelar
          </button>

        </div>

      </form>

    </div>
  );
}