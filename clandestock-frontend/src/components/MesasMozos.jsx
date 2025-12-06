import { useEffect, useState } from "react";
import { getMesas, ActualizarMesa, insertarMesa } from "../api/mesas";
import { getMozos, ActualizarMozo, insertarMozo } from "../api/mozos";

export const MesasMozos = () => {
    const [mozos, setMozos] = useState([]);
    const [mesas, setMesas] = useState([]);
    const [estado, setEstado] = useState(null);

    const [editMesaId, setEditMesaId] = useState(null);
    const [editMesaNumero, setEditMesaNumero] = useState("");

    const [editMozoId, setEditMozoId] = useState(null);
    const [editMozoNombre, setEditMozoNombre] = useState("");

    const [nuevaMesaNumero, setNuevaMesaNumero] = useState("");
    const [nuevoMozoNombre, setNuevoMozoNombre] = useState("");

    const cargarMesas = async () => {
        try {
            const data = await getMesas();
            setMesas(data);
        } catch (err) {
            console.error("Error cargando mesas:", err);
        }
    };

    const cargarMozos = async () => {
        try {
            const data = await getMozos();
            setMozos(data);
        } catch (err) {
            console.error("Error cargando mozos:", err);
        }
    };

    useEffect(() => {
        cargarMesas();
        cargarMozos();
        setEstado(null);
    }, []);

    const actualizarMesa = async (id) => {
        try {
            const dto = { numeroMesa: parseInt(editMesaNumero, 10), ocupada: false };
            const updated = await ActualizarMesa(id, dto);
            setMesas(mesas.map(m => m.id === id ? updated : m));
            setEditMesaId(null);
            setEditMesaNumero("");
            setEstado({ tipo: "success", mensaje: "Mesa actualizada correctamente" });
        } catch (err) {
            console.error("Error actualizando mesa:", err);
            setEstado({ tipo: "error", mensaje: "Error al actualizar la mesa" });
        }
    };

    const actualizarMozo = async (id) => {
        try {
            const dto = { nombre: editMozoNombre };
            const updated = await ActualizarMozo(id, dto);
            setMozos(mozos.map(m => m.id === id ? updated : m));
            setEditMozoId(null);
            setEditMozoNombre("");
            setEstado({ tipo: "success", mensaje: "Mozo actualizado correctamente" });
        } catch (err) {
            console.error("Error actualizando mozo:", err);
            setEstado({ tipo: "error", mensaje: "Error al actualizar el mozo" });
        }
    };

    const agregarMesa = async () => {
        try {
            const dto = { numeroMesa: parseInt(nuevaMesaNumero, 10), ocupada: false };
            const nueva = await insertarMesa(dto);
            setMesas([...mesas, nueva]);
            setNuevaMesaNumero("");
            setEstado({ tipo: "success", mensaje: "Mesa creada correctamente" });
        } catch (err) {
            console.error("Error creando mesa:", err);
            setEstado({ tipo: "error", mensaje: "Error al crear la mesa" });
        }
    };

    const agregarMozo = async () => {
        try {
            const dto = { nombre: nuevoMozoNombre };
            const nuevo = await insertarMozo(dto);
            setMozos([...mozos, nuevo]);
            setNuevoMozoNombre("");
            setEstado({ tipo: "success", mensaje: "Mozo creado correctamente" });
        } catch (err) {
            console.error("Error creando mozo:", err);
            setEstado({ tipo: "error", mensaje: "Error al crear el mozo" });
        }
    };

    return (
        <div className="container mt-3">
            {estado && (
                <div className={`alert mt-3 ${estado.tipo === "success" ? "alert-success" : "alert-danger"}`}>
                    {estado.mensaje}
                </div>
            )}

            <h4 className="d-flex justify-content-between align-items-center">
                Mesas
                <button className="btn btn-sm btn-success" onClick={agregarMesa} disabled={!nuevaMesaNumero}>
                    + Agregar
                </button>
            </h4>
            <div className="mb-3">
                <input
                    type="number"
                    value={nuevaMesaNumero}
                    onChange={(e) => setNuevaMesaNumero(e.target.value)}
                    className="form-control"
                    placeholder="Número de mesa"
                    style={{ maxWidth: "200px" }}
                />
            </div>
            <ul className="list-group mb-4">
                {mesas.map(mesa => (
                    <li key={mesa.id} className="list-group-item d-flex justify-content-between align-items-center">
                        {editMesaId === mesa.id ? (
                            <>
                                <input
                                    type="number"
                                    value={editMesaNumero}
                                    onChange={(e) => setEditMesaNumero(e.target.value)}
                                    className="form-control me-2"
                                    style={{ width: "100px" }}
                                />
                                <button className="btn btn-sm btn-success" onClick={() => actualizarMesa(mesa.id)}>Guardar</button>
                            </>
                        ) : (
                            <>
                                <span>Mesa {mesa.numeroMesa}</span>
                                <button
                                    className="btn btn-sm btn-outline-primary"
                                    onClick={() => {
                                        setEditMesaId(mesa.id);
                                        setEditMesaNumero(mesa.numeroMesa);
                                    }}
                                >
                                    Editar
                                </button>
                            </>
                        )}
                    </li>
                ))}
            </ul>

            <h4 className="d-flex justify-content-between align-items-center">
                Mozos
                <button className="btn btn-sm btn-success" onClick={agregarMozo} disabled={!nuevoMozoNombre}>
                    + Agregar
                </button>
            </h4>
            <div className="mb-3">
                <input
                    type="text"
                    value={nuevoMozoNombre}
                    onChange={(e) => setNuevoMozoNombre(e.target.value)}
                    className="form-control"
                    placeholder="Nombre del mozo"
                    style={{ maxWidth: "300px" }}
                />
            </div>
            <ul className="list-group">
                {mozos.map(mozo => (
                    <li key={mozo.id} className="list-group-item d-flex justify-content-between align-items-center">
                        {editMozoId === mozo.id ? (
                            <>
                                <input
                                    type="text"
                                    value={editMozoNombre}
                                    onChange={(e) => setEditMozoNombre(e.target.value)}
                                    className="form-control me-2"
                                    style={{ width: "200px" }}
                                />
                                <button className="btn btn-sm btn-success" onClick={() => actualizarMozo(mozo.id)}>Guardar</button>
                            </>
                        ) : (
                            <>
                                <span>{mozo.nombre}</span>
                                <button
                                    className="btn btn-sm btn-outline-primary"
                                    onClick={() => {
                                        setEditMozoId(mozo.id);
                                        setEditMozoNombre(mozo.nombre);
                                    }}
                                >
                                    Editar
                                </button>
                            </>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};


