export default function ClientesTable({
  clientes,
  onEdit,
  onDelete
}) {
  return (
    <div className="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Cédula</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Acciones</th>
          </tr>
        </thead>

        <tbody>
          {clientes.length === 0 ? (
            <tr>
              <td colSpan="7">
                No existen clientes registrados
              </td>
            </tr>
          ) : (
            clientes.map((cliente) => (
              <tr key={cliente.id}>
                <td>{cliente.id}</td>
                <td>{cliente.nombre}</td>
                <td>{cliente.apellido}</td>
                <td>{cliente.cedula}</td>
                <td>{cliente.telefono}</td>
                <td>{cliente.correo}</td>

                <td className="actions">
                  <button
                    className="edit-btn"
                    onClick={() => onEdit(cliente)}
                  >
                    Editar
                  </button>

                  <button
                    className="delete-btn"
                    onClick={() => onDelete(cliente.id)}
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}