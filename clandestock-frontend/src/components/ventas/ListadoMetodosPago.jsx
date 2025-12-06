import { useEffect, useState } from "react";
import { getMetodos, ActualizarMetodoPago } from "../../api/metodoPago";

const locales = [
    { id: "1", nombre: "Tenedor Libre" },
    { id: "2", nombre: "Termas" },
    { id: "3", nombre: "Heladería" }
];

export const ListadoMetodosPago = ({ estado, setEstado }) => {
    const [metodos, setMetodos] = useState([]);
    const [localSeleccionado, setLocalSeleccionado] = useState("");
    const [editId, setEditId] = useState(null);
    const [editData, setEditData] = useState({
        nombre_metodo_pago: "",
        incremento: "",
        descuento: "",
        estado: "activo",
        local_id: ""
    });

    useEffect(() => {
        const cargar = async () => {
            try {
                const data = await getMetodos();
                setMetodos(data);
            } catch (err) {
                console.error("Error cargando métodos:", err);
            }
        };
        cargar();
    }, []);

    const actualizarMetodo = async (id) => {
        try {
            if (editData.incremento && editData.descuento) {
                setEstado({
                    tipo: "error",
                    mensaje: "No se puede tener incremento y descuento juntos ❌"
                });
                return;
            }

            const dto = {
                id,
                nombre_metodo_pago: editData.nombre_metodo_pago,
                incremento: editData.incremento || "0",
                descuento: editData.descuento || "0",
                estado: editData.estado === "activo" ? "true" : "false",
                local_id: localSeleccionado
            };

            const actualizado = await ActualizarMetodoPago(id, dto);
            setMetodos(metodos.map((m) => (m.id === id ? actualizado : m)));
            setEditId(null);
            setEstado({ tipo: "success", mensaje: "Método actualizado ✅" });
        } catch (err) {
            console.error("Error actualizando método:", err);
            setEstado({ tipo: "error", mensaje: "Error al actualizar ❌" });
        }
    };

    const metodosFiltrados = metodos.filter((m) => m.local_id === localSeleccionado);

    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <div className="card-header bg-info text-dark">Métodos de pago</div>
            <div className="card-body overflow-auto">
                <div className="mb-3">
                    <label className="form-label">Seleccionar local</label>
                    <select
                        className="form-select"
                        value={localSeleccionado}
                        onChange={(e) => {
                            setLocalSeleccionado(e.target.value);
                            setEstado(null);
                        }}
                    >
                        <option value="">-- Seleccione un local --</option>
                        {locales.map((l) => (
                            <option key={l.id} value={l.id}>
                                {l.nombre}
                            </option>
                        ))}
                    </select>
                </div>

                {localSeleccionado && (
                    <ul className="list-group">
                        {metodosFiltrados.map((m) => (
                            <li
                                key={m.id}
                                className="list-group-item d-flex justify-content-between align-items-center"
                            >
                                {editId === m.id ? (
                                    <div className="w-100">
                                        <input
                                            type="text"
                                            className="form-control mb-2"
                                            value={editData.nombre_metodo_pago}
                                            onChange={(e) =>
                                                setEditData({ ...editData, nombre_metodo_pago: e.target.value })
                                            }
                                            placeholder="Nombre del método"
                                        />
                                        <div className="d-flex gap-2 mb-2">
                                            <input
                                                type="number"
                                                className="form-control"
                                                value={editData.incremento}
                                                onChange={(e) =>
                                                    setEditData({ ...editData, incremento: e.target.value, descuento: "" })
                                                }
                                                disabled={!!editData.descuento}
                                                placeholder="Incremento (%)"
                                            />
                                            <input
                                                type="number"
                                                className="form-control"
                                                value={editData.descuento}
                                                onChange={(e) =>
                                                    setEditData({ ...editData, descuento: e.target.value, incremento: "" })
                                                }
                                                disabled={!!editData.incremento}
                                                placeholder="Descuento (%)"
                                            />
                                        </div>
                                        <div className="d-flex gap-2">
                                            <button
                                                className="btn btn-sm btn-success"
                                                onClick={() => actualizarMetodo(m.id)}
                                            >
                                                Guardar
                                            </button>
                                            <button
                                                className="btn btn-sm btn-secondary"
                                                onClick={() => setEditId(null)}
                                            >
                                                Cancelar
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <>
                                        <span>
                                            {m.nombre_metodo_pago} - Inc: {m.incremento}% / Desc:{" "}
                                            {m.descuento}%
                                        </span>
                                        <div className="d-flex gap-2">
                                            <button
                                                className="btn btn-sm btn-outline-warning"
                                                onClick={() => {
                                                    setEditId(m.id);
                                                    setEditData({
                                                        nombre_metodo_pago: m.nombre_metodo_pago,
                                                        incremento: m.incremento !== "0" ? m.incremento : "",
                                                        descuento: m.descuento !== "0" ? m.descuento : "",
                                                        estado: m.estado,
                                                        local_id: m.local_id
                                                    });
                                                }}
                                            >
                                                Editar
                                            </button>
                                        </div>
                                    </>
                                )}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};
