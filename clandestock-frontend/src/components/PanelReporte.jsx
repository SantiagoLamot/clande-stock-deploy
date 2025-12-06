import { useState, useContext, useEffect } from "react";
import { enviarReporte, obtenerHistorialReportes } from "../api/reporte";
import { AuthContext } from "../context/AuthContext";

export default function PanelReporte() {
  const [descripcion, setDescripcion] = useState("");
  const [estado, setEstado] = useState(null);
  const [reportes, setReportes] = useState([]);
  const { user } = useContext(AuthContext);

  const cargarHistorial = async () => {
    try {
      const data = await obtenerHistorialReportes();
      // Tomamos solo los últimos 10
      const ultimos = data.slice(-10).reverse();
      setReportes(ultimos);
    } catch (error) {
      console.error(
        "Error cargando historial:",
        error.response?.data || error.message
      );
    }
  };

  useEffect(() => {
    cargarHistorial();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await enviarReporte(descripcion, user?.username);
      setEstado({
        tipo: "success",
        mensaje: "Reporte enviado correctamente ✅",
      });
      setDescripcion("");
      cargarHistorial(); // refrescar lista
    } catch (error) {
      console.error(
        "Error enviando reporte:",
        error.response?.data || error.message
      );
      setEstado({ tipo: "error", mensaje: "Error al enviar el reporte ❌" });
    }
  };

  return (
    <div className="p-4">
      <h4 className="mb-3">📝 Enviar reporte</h4>
      <form onSubmit={handleSubmit} className="card shadow-sm p-3">
        <div className="mb-3">
          <label className="form-label fw-bold">Descripción del reporte</label>
          <textarea
            className="form-control"
            rows="4"
            value={descripcion}
            onChange={(e) => setDescripcion(e.target.value)}
            placeholder="Escribe aquí el detalle del reporte..."
            required
          />
        </div>
        <button type="submit" className="btn btn-primary w-100">
          📤 Enviar reporte
        </button>
      </form>

      {estado && (
        <div
          className={`alert mt-3 ${
            estado.tipo === "success" ? "alert-success" : "alert-danger"
          }`}
        >
          {estado.mensaje}
        </div>
      )}

      <hr className="my-4" />

      <h5>📑 Últimos reportes</h5>
      {reportes.length === 0 ? (
        <p className="text-muted">No hay reportes disponibles</p>
      ) : (
        <ul className="list-group">
          {reportes.map((r, index) => (
            <li
              key={index}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <div>
                <strong className="text-muted">Usuario: {r.usuarioEmisor}</strong>
                <br />
                <small className="text-muted">{r.fecha}</small>
                <br />
                <small>{r.descripcion}</small>
              </div>
              <span
                className={`badge ${
                  r.estado === "Leído" ? "bg-success" : "bg-secondary"
                }`}
              >
                {r.estado}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
