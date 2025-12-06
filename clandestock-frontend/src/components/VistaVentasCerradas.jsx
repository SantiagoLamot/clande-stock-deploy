export default function VistaVentasCerradas({
  pedidos,
  cajaAbierta,
  setPedidoSeleccionado,
}) {
  const nombresPanel = {
    local: "Consumo en local",
    takeaway: "Takeaway",
    delivery: "Delivery",
  };

  const coloresPanel = {
    local: {
      border: "warning",
      bg: "warning",
      text: "dark",
      badge: "warning",
      icon: "🍽️",
    },
    takeaway: {
      border: "primary",
      bg: "primary",
      text: "white",
      badge: "primary",
      icon: "🛍️",
    },
    delivery: {
      border: "danger",
      bg: "danger",
      text: "white",
      badge: "danger",
      icon: "🚚",
    },
  };

  const formatDetalle = (tipo, pedido) => {
    if (tipo === "local") {
      return `${pedido.detalleEntrega} - Mesa ${pedido.numeroMesa || "?"}`;
    }
    if (tipo === "takeaway") {
      return `Nombre del cliente: ${pedido.detalleEntrega}`;
    }
    if (tipo === "delivery") {
      return `Dirección: ${pedido.detalleEntrega}`;
    }
    return pedido.detalleEntrega;
  };

  return (
    <div className="d-flex flex-column h-100">
      {["local", "takeaway", "delivery"].map((tipo) => (
        <div key={tipo} className="p-3" style={{ height: "350px" }}>
          <div
            className={`card h-100 border-${coloresPanel[tipo].border}`}
            style={{ position: "relative" }}
          >
            <div
              className={`card-header bg-${coloresPanel[tipo].bg} text-${coloresPanel[tipo].text}`}
            >
              {nombresPanel[tipo]}
            </div>

            {!cajaAbierta && (
              <div
                className="position-absolute w-100 h-100 bg-light opacity-75 d-flex justify-content-center align-items-center"
                style={{ top: 0, left: 0, zIndex: 10 }}
              >
                <span className="text-muted">Caja cerrada</span>
              </div>
            )}

            <div className="card-body" style={{ overflowY: "auto" }}>
              {pedidos[tipo].length === 0 ? (
                <p className="text-muted">Sin ventas cerradas</p>
              ) : (
                pedidos[tipo].map((p) => (
                  <div
                    key={p.idVenta}
                    className="mb-2 border-bottom pb-2 pedido-item d-flex justify-content-between align-items-center"
                    onClick={() => setPedidoSeleccionado?.(p)}
                    style={{ cursor: "pointer" }}
                  >
                    <div>
                      <strong>{formatDetalle(tipo, p)}</strong>
                      <br />
                      Total: ${p.precioTotalConMetodoDePago || p.precioTotal}
                      <br />
                      <small className="text-muted">
                        Cerrada: {new Date(p.fechaCierre).toLocaleString()}
                      </small>
                    </div>
                    <span
                      className={`badge bg-${coloresPanel[tipo].badge} text-light d-flex align-items-center gap-1`}
                    >
                      <span>{coloresPanel[tipo].icon}</span>
                      <span>{nombresPanel[tipo]}</span>
                    </span>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
