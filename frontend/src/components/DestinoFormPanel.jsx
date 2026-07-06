import { useEffect, useState } from "react";

const initialState = {
  nombre: "",
  pais: "",
  ciudad: "",
  descripcion: "",
};

export default function DestinoFormPanel({
  onSave,
  editingDestino,
  onCancel,
}) {
  const [form, setForm] = useState(initialState);

  useEffect(() => {
    if (editingDestino) {
      setForm({
        nombre: editingDestino.nombre ?? "",
        pais: editingDestino.pais ?? "",
        ciudad: editingDestino.ciudad ?? "",
        descripcion: editingDestino.descripcion ?? "",
      });
    } else {
      setForm(initialState);
    }
  }, [editingDestino]);

  function handleChange(e) {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  function handleSubmit(e) {
    e.preventDefault();

    if (
      !form.nombre.trim() ||
      !form.pais.trim() ||
      !form.ciudad.trim()
    ) {
      alert("Complete todos los campos obligatorios.");
      return;
    }

    onSave(form);

    if (!editingDestino) {
      setForm(initialState);
    }
  }

  return (
    <aside className="w-[320px] rounded-2xl border border-slate-200 bg-white p-6">

      <h2 className="mb-6 text-xl font-bold text-navy-950">
        {editingDestino ? "Editar destino" : "Nuevo destino"}
      </h2>

      <form
        onSubmit={handleSubmit}
        className="space-y-4"
      >

        <div>
          <label className="mb-1 block text-sm font-medium">
            Nombre *
          </label>

          <input
            name="nombre"
            value={form.nombre}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2"
          />
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium">
            País *
          </label>

          <input
            name="pais"
            value={form.pais}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2"
          />
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium">
            Ciudad *
          </label>

          <input
            name="ciudad"
            value={form.ciudad}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2"
          />
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium">
            Descripción
          </label>

          <textarea
            rows="4"
            name="descripcion"
            value={form.descripcion}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2"
          />
        </div>

        <div className="flex gap-3 pt-2">

          <button
            type="submit"
            className="flex-1 rounded-lg bg-brand-600 py-2 font-semibold text-white hover:bg-brand-700"
          >
            {editingDestino ? "Actualizar" : "Guardar"}
          </button>

          {editingDestino && (
            <button
              type="button"
              onClick={onCancel}
              className="rounded-lg border border-slate-300 px-4 hover:bg-slate-100"
            >
              Cancelar
            </button>
          )}

        </div>

      </form>

    </aside>
  );
}