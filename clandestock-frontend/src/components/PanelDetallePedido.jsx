import { useVentaDetalle } from "../hooks/useVentaDetalle";

export default function PanelDetallePedido({ pedido, onBack }) {
  const {
    venta,
    categorias,
    categoriaSeleccionada,
    setCategoriaSeleccionada,
    productos,
    productosAgrupados,
    handleAgregarProducto,
    handleQuitarProducto,
    metodosPago,
    metodoSeleccionado,
    handleSeleccionarMetodoPago,
    handleCerrarVenta,
    mensaje,
  } = useVentaDetalle(pedido.idVenta);

  if (!venta) {
    return <p className="text-muted">Cargando venta...</p>;
  }

  return (
    <div className="p-4 h-100 d-flex flex-column">
      {/* Botón retroceso */}
      <button
        className="btn btn-link text-warning mb-3 d-flex align-items-center"
        onClick={onBack}
      >
        <i className="bi bi-arrow-left"></i>
        <span className="ms-2">Volver</span>
      </button>

      <h3 className="text-warning gothic-font mb-4">Detalle del Pedido</h3>

      {/* Datos básicos del pedido + productos seleccionados */}
      <div className="card shadow p-3 mb-4 flex-grow-1">
        <p>
          <strong>ID Venta:</strong> {venta.idVenta}
        </p>
        {venta.numMesa && (
          <p>
            <strong>Mesa:</strong> {venta.numMesa}
          </p>
        )}
        <p>
          <strong>Detalle de entrega:</strong> {venta.detalleEntrega}
        </p>

        {/* Productos en la venta */}
        <h5 className="text-warning gothic-font mt-4 mb-3">
          Productos en la venta
        </h5>
        {productosAgrupados.length === 0 ? (
          <p className="text-muted">No hay productos agregados</p>
        ) : (
          <>
            <div className="list-group">
              {productosAgrupados.map((prod) => (
                <div
                  key={prod.nombreProducto}
                  className="list-group-item d-flex justify-content-between align-items-center"
                >
                  <div>
                    <strong>{prod.nombreProducto}</strong>
                    <br />
                    <small className="text-muted">
                      Precio unitario: ${prod.precioProducto ?? 0} | Cantidad:{" "}
                      {prod.cantidad ?? 0} | Total:{" "}
                      {typeof prod.total === "number"
                        ? `$${prod.total.toLocaleString()}`
                        : "sin total"}
                    </small>
                  </div>
                  <div className="d-flex gap-2">
                    <button
                      className="btn btn-success btn-sm"
                      onClick={() => handleAgregarProducto(prod)}
                      disabled={!prod.id}
                    >
                      +
                    </button>
                    <span className="badge bg-secondary">{prod.cantidad}</span>
                    <button
                      className="btn btn-danger btn-sm"
                      onClick={() => handleQuitarProducto(prod)}
                    >
                      -
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Total dinámico desde backend */}
            <div className="mt-4 text-end">
              <h5 className="text-dark">
                <strong>Total: ${venta.precioTotal ?? 0}</strong>
              </h5>
            </div>
          </>
        )}

      </div>

      {/* Selección de categoría y productos disponibles */}
      <div className="card shadow p-3 mb-4">
        <h5 className="text-warning gothic-font mb-3">Agregar productos</h5>
        <div className="mb-3">
          <label className="form-label text-dark fw-bold">
            Seleccionar categoría
          </label>
          <select
            className="form-select border-warning shadow-sm"
            value={categoriaSeleccionada}
            onChange={(e) => setCategoriaSeleccionada(e.target.value)}
          >
            <option value="todos">Todos</option>
            {categorias.map((c) => (
              <option key={c.id} value={c.id}>
                {c.nombreCategoria} ({c.localID})
              </option>
            ))}
          </select>
        </div>

        {productos.length === 0 ? (
          <p className="text-muted">
            Seleccione una categoría para ver productos
          </p>
        ) : (
          <div className="list-group">
            {productos.map((prod) => (
              <div
                key={prod.id}
                className="list-group-item d-flex justify-content-between align-items-center"
              >
                <div>
                  <strong>{prod.nombreProducto}</strong>
                  <br />
                  <small className="text-muted">
                    Precio: ${prod.precio} | Stock: {prod.stockDisponible}
                  </small>
                </div>
                <button
                  className="btn btn-success btn-sm"
                  onClick={() => handleAgregarProducto(prod)}
                >
                  +
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Selección de método de pago */}
      <div className="card shadow p-3 mb-4">
        <h5 className="text-warning gothic-font mb-3">Método de Pago</h5>
        <div className="mb-3">
          <label className="form-label text-dark fw-bold">
            Seleccionar método
          </label>
          {productosAgrupados.length === 0 && (
            <small className="text-muted">
              Agregue productos a la venta para habilitar métodos de pago
            </small>
          )}

          {/* Mensaje de confirmación si ya hay método seleccionado */}
          {metodoSeleccionado && (
            <div className="alert alert-success py-1 mt-2 mb-2">
              Método de pago cargado:{" "}
              <strong>
                {
                  metodosPago.find(
                    (m) => m.id.toString() === metodoSeleccionado
                  )?.nombre_metodo_pago
                }
              </strong>
            </div>
          )}

          <select
            className="form-select border-warning shadow-sm"
            value={metodoSeleccionado}
            onChange={(e) => handleSeleccionarMetodoPago(e.target.value)}
            disabled={productosAgrupados.length === 0}
          >
            {/* Solo mostrar "-- Seleccionar --" si no hay método elegido */}
            {!metodoSeleccionado && <option value="">-- Seleccionar --</option>}
            {metodosPago.map((m) => (
              <option key={m.id} value={m.id.toString()}>
                {m.nombre_metodo_pago}
              </option>
            ))}
          </select>
        </div>

        {/* Mostrar total con método de pago */}
        {venta.precioTotalConMetodoDePago && (
          <div className="mt-3 text-end">
            <h5 className="text-dark">
              <strong>
                Total con método: ${venta.precioTotalConMetodoDePago}
              </strong>
            </h5>
          </div>
        )}
      </div>

      {/* Botones de acción */}
      <div className="d-flex justify-content-between mt-4">
        {!metodoSeleccionado && (
          <small className="text-muted">
            Seleccione un método de pago para cerrar la venta
          </small>
        )}
        <button
          className="btn btn-danger fw-bold"
          onClick={() => handleCerrarVenta(onBack)}
          disabled={!metodoSeleccionado} // 👈 clave
        >
          Cerrar venta - Cobrar
        </button>

        <button
          className="btn btn-success fw-bold"
          onClick={() =>
            alert("Funcionalidad de imprimir comanda aún no implementada")
          }
        >
          Imprimir comanda
        </button>
      </div>

      {/* Mensaje */}
      {mensaje && (
        <div className="alert alert-info text-center mt-3 gothic-font">
          {mensaje}
        </div>
      )}
    </div>
  );
}
