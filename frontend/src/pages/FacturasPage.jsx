import React, { useEffect, useState } from "react";

const FacturasPage = () => {
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(true);

  // Simulación de carga de datos (luego lo conectas a tu API)
  useEffect(() => {
   

    fetchFacturas();
  }, []);

  if (loading) return <p>Cargando facturas...</p>;

  return (
    <div style={{ padding: "20px" }}>
      <h2>📄 Facturas</h2>

      {facturas.length === 0 ? (
        <p>No hay facturas registradas.</p>
      ) : (
        <table border="1" cellPadding="10">
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Monto</th>
              <th>Fecha</th>
            </tr>
          </thead>
          <tbody>
            {facturas.map((f) => (
              <tr key={f.id}>
                <td>{f.id}</td>
                <td>{f.cliente}</td>
                <td>${f.monto}</td>
                <td>{f.fecha}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default FacturasPage;