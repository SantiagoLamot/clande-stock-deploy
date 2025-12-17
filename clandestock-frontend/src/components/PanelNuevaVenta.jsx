import { useEffect, useState } from "react";

export default function PanelNuevaVenta() {
  const [tipoVenta, setTipoVenta] = useState("");
  const [detalleEntrega, setDetalleEntrega] = useState("");
  const [mesas, setMesas] = useState([]);
  const [numeroMesa, setNumeroMesa] = useState(null);
  const [mozos, setMozos] = useState([]);
  const [mozoSeleccionado, setMozoSeleccionado] = useState("");
  const [mensaje, setMensaje] = useState("");

  const token = localStorage.getItem("access_token");

  // 🔐 Cargar mesas y mozos si es consumo local
  useEffect(() => {
    if (tipoVenta === "CONSUMO_LOCAL") {
      // Mesas
      fetch("http://localhost:8080/mesas", {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((res) => res.json())
        .then((data) => {
          const ordenadas = data.sort((a, b) => a.numeroMesa - b.numeroMesa);
          setMesas(ordenadas);
        })
        .catch((err) => console.error("Error cargando mesas:", err));

      // Mozos
      fetch("http://localhost:8080/mozos", {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((res) => res.json())
        .then((data) => {
          setMozos(data);
        })
        .catch((err) => console.error("Error cargando mozos:", err));
    }
  }, [tipoVenta, token]);

  const handleCrearVenta = async () => {
    const body = {
      tipoVenta,
      detalleEntrega:
        tipoVenta === "CONSUMO_LOCAL"
          ? `Mozo: ${mozoSeleccionado}`
          : detalleEntrega || null,
      numeroMesa: tipoVenta === "CONSUMO_LOCAL" ? numeroMesa : null,
    };

    try {
      const response = await fetch("http://localhost:8080/venta/nueva", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) throw new Error("Error creando venta");

      setMensaje("✅ Venta generada correctamente");
      setTipoVenta("");
      setDetalleEntrega("");
      setNumeroMesa(null);
      setMozoSeleccionado("");
      setMesas([]);
      setMozos([]);
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al generar la venta");
    }
  };

  const handleSeleccionMesa = (mesa) => {
    if (mesa.ocupada) {
      setMensaje(
        `⚠️ La Mesa ${mesa.numeroMesa} está ocupada y no se puede seleccionar`
      );
      return;
    }
    setNumeroMesa(mesa.numeroMesa);
    setMensaje("");
  };

  return (
    <div className="p-4">
      <h3 className="text-warning gothic-font mb-4">Nueva Venta</h3>

      {/* Selección tipo de venta */}
      <div className="mb-3">
        <label className="form-label text-dark fw-bold">Tipo de Venta</label>
        <select
          className="form-select border-warning shadow-sm"
          value={tipoVenta}
          onChange={(e) => setTipoVenta(e.target.value)}
        >
          <option value="">-- Seleccionar --</option>
          <option value="CONSUMO_LOCAL">Consumo Local</option>
          <option value="ENVIO_DOMICILIO">Envío a Domicilio</option>
          <option value="TAKE_AWAY">Take Away</option>
        </select>
      </div>

      {/* Inputs dinámicos */}
      {tipoVenta === "ENVIO_DOMICILIO" && (
        <div className="mb-3">
          <label className="form-label text-dark fw-bold">
            Domicilio de entrega
          </label>
          <input
            type="text"
            className="form-control border-warning shadow-sm"
            value={detalleEntrega}
            onChange={(e) => setDetalleEntrega(e.target.value)}
            placeholder="Ingrese domicilio"
          />
        </div>
      )}

      {tipoVenta === "TAKE_AWAY" && (
        <div className="mb-3">
          <label className="form-label text-dark fw-bold">
            Nombre del cliente
          </label>
          <input
            type="text"
            className="form-control border-warning shadow-sm"
            value={detalleEntrega}
            onChange={(e) => setDetalleEntrega(e.target.value)}
            placeholder="Ingrese nombre del cliente"
          />
        </div>
      )}

      {tipoVenta === "CONSUMO_LOCAL" && (
        <>
          {/* Dropdown mozo */}
          <div className="mb-3">
            <label className="form-label text-dark fw-bold">
              Seleccione Mozo
            </label>
            <select
              className="form-select border-warning shadow-sm"
              value={mozoSeleccionado}
              onChange={(e) => setMozoSeleccionado(e.target.value)}
            >
              <option value="">-- Seleccionar Mozo --</option>
              {mozos.map((mozo) => (
                <option key={mozo.id} value={mozo.nombre}>
                  {mozo.nombre}
                </option>
              ))}
            </select>
          </div>

          {/* Mesas */}
          <div className="mb-3">
            <label className="form-label text-dark fw-bold">
              Seleccione mesa
            </label>
            <div className="d-flex flex-wrap gap-2">
              {mesas.map((mesa) => (
                <button
                  key={mesa.id}
                  className={`btn ${
                    mesa.ocupada
                      ? "btn-danger"
                      : numeroMesa === mesa.numeroMesa
                      ? "btn-warning"
                      : "btn-success"
                  }`}
                  onClick={() => handleSeleccionMesa(mesa)}
                >
                  Mesa {mesa.numeroMesa}
                </button>
              ))}
            </div>
          </div>
        </>
      )}

      {/* Botón crear venta */}
      <div className="text-center mt-4">
        <button
          className="btn btn-warning px-4"
          onClick={handleCrearVenta}
          disabled={
            !tipoVenta ||
            (tipoVenta === "CONSUMO_LOCAL" &&
              (!numeroMesa || !mozoSeleccionado))
          }
        >
          Generar Venta
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
