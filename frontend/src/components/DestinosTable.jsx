import { Pencil, Trash2 } from "lucide-react";

export default function DestinosTable({
  destinos,
  loading,
  onEdit,
  onDelete,
}) {
  if (loading) {
    return (
      <div className="p-8 text-center text-slate-500">
        Cargando destinos...
      </div>
    );
  }

  if (destinos.length === 0) {
    return (
      <div className="p-8 text-center text-slate-500">
        No existen destinos registrados.
      </div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full text-sm">

        <thead className="bg-slate-50">
          <tr>

            <th className="px-6 py-3 text-left font-semibold text-slate-600">
              Destino
            </th>

            <th className="px-6 py-3 text-left font-semibold text-slate-600">
              País
            </th>

            <th className="px-6 py-3 text-left font-semibold text-slate-600">
              Ciudad
            </th>

            <th className="px-6 py-3 text-left font-semibold text-slate-600">
              Descripción
            </th>

            <th className="px-6 py-3 text-center font-semibold text-slate-600">
              Acciones
            </th>

          </tr>
        </thead>

        <tbody>

          {destinos.map((destino) => (

            <tr
              key={destino.idDestino}
              className="border-t hover:bg-slate-50"
            >

              <td className="px-6 py-4 font-medium text-slate-900">
                {destino.nombre}
              </td>

              <td className="px-6 py-4">
                {destino.pais}
              </td>

              <td className="px-6 py-4">
                {destino.ciudad}
              </td>

              <td className="px-6 py-4">
                {destino.descripcion || "-"}
              </td>

              <td className="px-6 py-4">

                <div className="flex justify-center gap-2">

                  <button
                    onClick={() => onEdit(destino)}
                    className="rounded-lg bg-blue-500 p-2 text-white transition hover:bg-blue-600"
                    title="Editar"
                  >
                    <Pencil size={16} />
                  </button>

                  <button
                    onClick={() => onDelete(destino)}
                    className="rounded-lg bg-red-500 p-2 text-white transition hover:bg-red-600"
                    title="Eliminar"
                  >
                    <Trash2 size={16} />
                  </button>

                </div>

              </td>

            </tr>

          ))}

        </tbody>

      </table>
    </div>
  );
}