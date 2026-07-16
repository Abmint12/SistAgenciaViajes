import { useEffect, useMemo, useState } from "react";
import { Plus, RefreshCw, Search } from "lucide-react";

import StatCard from "../components/StatCard";
import DestinosTable from "../components/DestinosTable";
import DestinoFormPanel from "../components/DestinoFormPanel";

import {
  listarDestinos,
  crearDestino,
  actualizarDestino,
  eliminarDestino,
} from "../api/destinosApi";

export default function DestinosPage() {
  const [destinos, setDestinos] = useState([]);
  const [search, setSearch] = useState("");
  const [editingDestino, setEditingDestino] = useState(null);
  const [backendOnline, setBackendOnline] = useState(true);
  const [loading, setLoading] = useState(true);

  async function cargarDestinos() {
    setLoading(true);

    try {
      const data = await listarDestinos();

      setDestinos(data.sort((a, b) => b.idDestino - a.idDestino));
      setBackendOnline(true);
    } catch (error) {
      console.error(error);
      setBackendOnline(false);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    cargarDestinos();
  }, []);

  const destinosFiltrados = useMemo(() => {
    const q = search.trim().toLowerCase();

    return destinos.filter(
      (d) =>
        String(d.nombre ?? "").toLowerCase().includes(q) ||
        String(d.pais ?? "").toLowerCase().includes(q) ||
        String(d.ciudad ?? "").toLowerCase().includes(q) ||
        String(d.descripcion ?? "").toLowerCase().includes(q)
    );
  }, [destinos, search]);

  async function guardarDestino(destino) {
    try {
      if (editingDestino) {
        const actualizado = await actualizarDestino(
          editingDestino.idDestino,
          destino
        );

        setDestinos((prev) =>
          prev.map((d) =>
            d.idDestino === editingDestino.idDestino ? actualizado : d
          )
        );

        setEditingDestino(null);
      } else {
        const nuevo = await crearDestino(destino);

        setDestinos((prev) =>
          [nuevo, ...prev].sort((a, b) => b.idDestino - a.idDestino)
        );
      }
    } catch (error) {
      console.error(error);
    }
  }

  async function eliminar(idDestino) {
    const confirmar = confirm(
      "¿Seguro que deseas eliminar este destino?"
    );

    if (!confirmar) return;

    try {
      await eliminarDestino(idDestino);

      setDestinos((prev) =>
        prev.filter((d) => d.idDestino !== idDestino)
      );
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <main className="flex-1 overflow-y-auto thin-scroll">

      <div className="mx-auto max-w-[1400px] px-10 py-8">

        <header className="mb-7 flex items-start justify-between">

          <div>

            <p className="text-xs font-semibold uppercase tracking-wide text-brand-600">
              Gestión de destinos
            </p>

            <h1 className="text-3xl font-extrabold text-navy-950">
              Destinos
            </h1>

          </div>

          <button
            onClick={() => setEditingDestino(null)}
            className="flex items-center gap-2 rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-brand-700"
          >
            <Plus size={16} />
            Nuevo destino
          </button>

        </header>

        <section className="mb-6 flex gap-5">

          <StatCard
            label="Total"
            value={destinos.length}
          />

          <StatCard
            label="Países"
            value={
              new Set(destinos.map((d) => d.pais)).size
            }
          />

          <StatCard
            label="Ciudades"
            value={
              new Set(destinos.map((d) => d.ciudad)).size
            }
          />

        </section>

        <section className="mb-6 flex items-center gap-3">

          <div className="relative flex-1">

            <Search
              size={16}
              className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
            />

            <input
              type="text"
              placeholder="Buscar destino, país o ciudad..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full rounded-xl border border-slate-200 bg-white py-2.5 pl-10 pr-4 text-sm text-slate-800 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
            />

          </div>

          <button
            onClick={cargarDestinos}
            className="rounded-xl border border-slate-200 bg-white p-2.5 text-slate-500 hover:text-navy-950"
          >
            <RefreshCw size={16} />
          </button>

        </section>

        <section className="grid grid-cols-[1fr_320px] gap-4">

          <div className="rounded-2xl border border-slate-200 bg-white">

            <div className="flex items-center justify-between border-b border-slate-100 px-6 py-4">

              <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                Listado
              </p>

              <p className="text-xs text-slate-400">
                {destinosFiltrados.length} resultados
              </p>

            </div>

            <DestinosTable
              destinos={destinosFiltrados}
              loading={loading}
              onEdit={setEditingDestino}
              onDelete={(d) => eliminar(d.idDestino)}
            />

          </div>

          <DestinoFormPanel
            onSave={guardarDestino}
            editingDestino={editingDestino}
            onCancel={() => setEditingDestino(null)}
          />

        </section>

      </div>

    </main>
  );
}