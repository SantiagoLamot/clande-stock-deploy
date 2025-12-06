import { useEffect, useState } from "react";
import { getAlertasStockSecundario, getAlertasStockPrimario, actualizarStockPrimario, actualizarStockSecundario, desactivarAlertaPrimario, desactivarAlertaSecundario } from "../../api/alertasStock";

const localesMap = {
    "1": "Tenedor Libre",
    "2": "Termas",
    "3": "Heladería"
};

export default function AdminAlertasStock({ setCantidadAlertas }) {
    const [alertas, setAlertas] = useState([]);

    useEffect(() => {
        cargarAlertas();
    }, []);

    const cargarAlertas = async () => {
        try {
            setAlertas([]);
            const primarios = await getAlertasStockPrimario();
            const secundarios = await getAlertasStockSecundario();

            const primariosMapped = primarios.map(p => ({
                ...p,
                tipo: "principal",
                alerta: p.alertaSinStock === "true" || p.stockDisponible === "0"
                    ? "sin stock"
                    : "poco stock"
            }));

            const secundariosMapped = secundarios.map(p => ({
                ...p,
                tipo: "secundario",
                alerta: p.alertaSinStock === "true" || p.stockDisponible === "0"
                    ? "sin stock"
                    : "poco stock"
            }));

            setAlertas([...primariosMapped, ...secundariosMapped]);
            setCantidadAlertas(primariosMapped.length + secundariosMapped.length);
        } catch (err) {
            console.error("Error cargando alertas:", err);
        }
    };

    const handleDesactivarAlerta = async (producto) => {
        try {
            if (producto.tipo === "principal") {
                await desactivarAlertaPrimario(producto.id);
            } else {
                await desactivarAlertaSecundario(producto.id);
            }
            cargarAlertas();
        } catch (err) {
            console.error("Error desactivando alerta:", err);
        }
    };

    const handleActualizarStock = async (producto, nuevoStock) => {
        try {
            const dto = {
                id_producto: producto.id,
                stock: nuevoStock.toString()
            };

            if (producto.tipo === "principal") {
                await actualizarStockPrimario(dto);
            } else {
                await actualizarStockSecundario(dto);
            }

            cargarAlertas(); // refresca la vista
        } catch (err) {
            console.error("Error actualizando stock:", err);
        }
    };

    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <div className="card-header bg-danger text-light">Alertas de stock</div>
            <div className="card-body overflow-auto">
                {alertas.length === 0 ? (
                    <p className="text-muted">No hay alertas activas</p>
                ) : (
                    <div className="row">
                        {alertas.map((p) => (
                            <div key={p.id} className="col-12 col-md-6 col-lg-4">
                                <div className="card mb-3 shadow-sm">
                                    <div className="card-body">
                                        <div className="d-flex justify-content-between mb-2">
                                            {/* Badge tipo */}
                                            <span
                                                className={`badge px-2 ${p.tipo === "principal" ? "bg-primary" : "bg-secondary"
                                                    }`}
                                            >
                                                {p.tipo}
                                            </span>

                                            {/* Badge alerta */}
                                            {p.alerta && (
                                                <span
                                                    className={`badge px-2 ${p.alerta === "sin stock" ? "bg-danger" : "bg-warning text-dark"}`}
                                                >
                                                    {p.alerta}
                                                </span>
                                            )}
                                        </div>
                                        <h5 className="card-title">{p.nombreProducto}</h5>
                                        <p className="card-text mb-1">Local: {localesMap[p.local]}</p>
                                        <p className="card-text mb-1">Stock disponible: {p.stockDisponible}</p>

                                        {(p.alertaStockBajo === "true" || p.alertaSinStock === "true") ? (
                                            <button
                                                className="btn btn-sm btn-warning"
                                                onClick={() => handleDesactivarAlerta(p)}
                                            >
                                                Desactivar alerta
                                            </button>
                                        ) : (
                                            p.tieneSecundarios === "true" ? (
                                                <p className="text-muted">Stock controlado por secundarios</p>
                                            ) : (
                                                <div className="d-flex gap-2">
                                                    <input
                                                        type="number"
                                                        className="form-control form-control-sm"
                                                        placeholder="Nuevo stock"
                                                        onKeyDown={(e) => {
                                                            if (e.key === "Enter") {
                                                                handleActualizarStock(p, e.target.value);
                                                            }
                                                        }}
                                                    />
                                                    <button
                                                        className="btn btn-sm btn-success"
                                                        onClick={(e) => {
                                                            const input = e.target.previousSibling;
                                                            handleActualizarStock(p, input.value);
                                                        }}
                                                    >
                                                        Actualizar stock
                                                    </button>
                                                </div>
                                            )
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}