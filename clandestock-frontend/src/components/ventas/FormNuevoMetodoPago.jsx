import { useState } from "react";
import { insertarMetodoPago } from "../../api/metodoPago";

const locales = [
    { id: "1", nombre: "Tenedor Libre" },
    { id: "2", nombre: "Termas" },
    { id: "3", nombre: "Heladería" }
];

export const FormNuevoMetodoPago = ({ estado, setEstado, onCreated }) => {
    const [localSeleccionado, setLocalSeleccionado] = useState("");
    const [nuevoMetodo, setNuevoMetodo] = useState({
        nombre_metodo_pago: "",
        incremento: "",
        descuento: "",
        estado: "activo"
    });

    const agregarMetodo = async () => {
        try {
            if (nuevoMetodo.incremento && nuevoMetodo.descuento) {
                setEstado({ tipo: "error", mensaje: "No se puede cargar incremento y descuento juntos ❌" });
                return;
            }

            const dto = {
                nombre_metodo_pago: nuevoMetodo.nombre_metodo_pago,
                incremento: nuevoMetodo.incremento || "0",
                descuento: nuevoMetodo.descuento || "0",
                estado: nuevoMetodo.estado === "activo" ? "true" : "false",
                local_id: localSeleccionado
            };

            const creado = await insertarMetodoPago(dto);
            onCreated(creado);
            setNuevoMetodo({ nombre_metodo_pago: "", incremento: "", descuento: "", estado: "activo" });
            setEstado({ tipo: "success", mensaje: "Método creado ✅" });
        } catch (err) {
            console.error("Error creando método:", err);
            setEstado({ tipo: "error", mensaje: "Error al crear ❌" });
        }
    };

    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <div className="card-header bg-warning text-dark">Agregar método de pago</div>
            <div className="card-body overflow-auto">
                <div className="mb-3">
                    <label className="form-label">Local</label>
                    <select
                        className="form-select"
                        value={localSeleccionado}
                        onChange={(e) => {setLocalSeleccionado(e.target.value); setEstado(null);}}
                    >
                        <option value="">-- Seleccione un local --</option>
                        {locales.map(l => (
                            <option key={l.id} value={l.id}>{l.nombre}</option>
                        ))}
                    </select>
                </div>
                <div className="mb-3">
                    <label className="form-label">Nombre del método</label>
                    <input
                        type="text"
                        className="form-control"
                        value={nuevoMetodo.nombre_metodo_pago}
                        onChange={(e) => setNuevoMetodo({ ...nuevoMetodo, nombre_metodo_pago: e.target.value })}
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Incremento (%)</label>
                    <input
                        type="number"
                        className="form-control"
                        value={nuevoMetodo.incremento}
                        onChange={(e) => setNuevoMetodo({ ...nuevoMetodo, incremento: e.target.value })}
                        disabled={!!nuevoMetodo.descuento}
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Descuento (%)</label>
                    <input
                        type="number"
                        className="form-control"
                        value={nuevoMetodo.descuento}
                        onChange={(e) => setNuevoMetodo({ ...nuevoMetodo, descuento: e.target.value })}
                        disabled={!!nuevoMetodo.incremento}
                    />
                </div>
                <button
                    className="btn btn-success"
                    onClick={agregarMetodo}
                    disabled={!localSeleccionado || !nuevoMetodo.nombre_metodo_pago}
                >
                    Guardar
                </button>
            </div>
        </div>
    );
};
