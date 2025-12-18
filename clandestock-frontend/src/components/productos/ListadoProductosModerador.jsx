import { useEffect, useState } from "react";
import { getProductosStok, incrementarStockPrimario } from "../../api/productoPrimario";
import { getAllProductosSecundarios, incrementarStockSecundario } from "../../api/productoSecundario";
import { getAlertasStockPrimario, getAlertasStockSecundario, insertarAlertaPrimario, insertarAlertaSecundario } from "../../api/alertasStock";

const localesMap = {
    "1": "Tenedor Libre",
    "2": "Termas",
    "3": "Heladería"
};

export default function ListadoProductosModerador({ cargarAlertas }) {
    const [productos, setProductos] = useState([]);
    const [selectedKey, setSelectedKey] = useState(null);

    const cargarProductos = async () => {
        try {
            const primarios = await getProductosStok();
            const secundarios = await getAllProductosSecundarios();
            const alertasPrimarios = await getAlertasStockPrimario();
            const alertasSecundarios = await getAlertasStockSecundario();

            const alertasPrimariosMap = Object.fromEntries(
                alertasPrimarios.map((a) => [String(a.id), a])
            );
            const alertasSecundariosMap = Object.fromEntries(
                alertasSecundarios.map((a) => [String(a.id), a])
            );

            const productosPrimarios = primarios.map((p) => {
                const alerta = alertasPrimariosMap[String(p.id)];
                return {
                    tipo: "principal",
                    id: p.id,
                    nombre: p.nombreProducto,
                    stock: parseInt(p.stockDisponible, 10),
                    precio: parseFloat(p.precio),
                    local: p.local,
                    tieneSecundarios: p.tieneSecundarios === "true",

                    stockBajo: alerta ? alerta.stockBajo === "true" : p.stockBajo === "true",
                    alertaStockBajo: alerta ? alerta.alertaStockBajo === "true" : p.alertaStockBajo === "true",
                    alertaSinStock: alerta ? alerta.alertaSinStock === "true" : p.alertaSinStock === "true",
                };
            });

            const productosSecundarios = secundarios.map((p) => {
                const alerta = alertasSecundariosMap[String(p.id)];
                return {
                    tipo: "secundario",
                    id: p.id,
                    nombre: p.nombre_producto,
                    stock: parseInt(p.stock, 10),
                    estado: p.estado === "true" ? "activo" : "inactivo",
                    local: p.local,
                    tieneSecundarios: p.tieneSecundarios === "true",

                    stockBajo: alerta ? alerta.stockBajo === "true" : p.stockBajo === "true",
                    alertaStockBajo: alerta ? alerta.alertaStockBajo === "true" : p.alertaStockBajo === "true",
                    alertaSinStock: alerta ? alerta.alertaSinStock === "true" : p.alertaSinStock === "true",
                };
            });


            setProductos([...productosPrimarios, ...productosSecundarios]);
        } catch (err) {
            console.error("Error cargando productos moderador:", err);
        }
    };

    useEffect(() => {
        cargarProductos();
    }, []);

    const handleAlerta = async (producto, tipoAlerta) => {
        try {
            const dto = { stockBajo: "false", sinStock: "false" };
            if (tipoAlerta === "bajo") dto.stockBajo = "true";
            else if (tipoAlerta === "sin") dto.sinStock = "true";

            if (producto.tipo === "principal") {
                await insertarAlertaPrimario(producto.id, dto);
            } else {
                await insertarAlertaSecundario(producto.id, dto);
            }

            await cargarProductos();
            await cargarAlertas();
        } catch (err) {
            console.error("Error actualizando alerta:", err);
        }
    };

    const handleIncrementarStock = async (producto) => {
        try {
            const id_producto = producto.id;
            if (producto.tipo === "principal") {
                await incrementarStockPrimario(id_producto);
            } else {
                await incrementarStockSecundario(id_producto);
            }
            await cargarProductos();
            await cargarAlertas();
        } catch (err) {
            console.error("Error incrementando stock:", err);
        }
    };

    const toggleSeleccion = (key) => {
        setSelectedKey(prev => prev === key ? null : key);
    };

    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <div className="card-header bg-info text-dark">Productos</div>
            <div className="card-body overflow-auto">
                {productos.length === 0 ? (
                    <p className="text-muted">No hay productos cargados</p>
                ) : (
                    <div className="row">
                        {productos.map((p) => {
                            const key = `${p.tipo}-${p.id}`;

                            return (
                                <div key={key} className="col-12 col-md-6 col-lg-4">
                                    <div className="card mb-3 shadow-sm">
                                        <div
                                            className="card-body"
                                            onClick={() => toggleSeleccion(key)}
                                            style={{ cursor: "pointer" }}
                                        >
                                            {/* Badges */}
                                            <div className="d-flex justify-content-between mb-2">
                                                <span
                                                    className={`badge ${p.tipo === "principal" ? "bg-primary" : "bg-secondary"
                                                        }`}
                                                >
                                                    {p.tipo}
                                                </span>

                                                {p.alertaSinStock || p.stock === 0 ? (
                                                    <span className="badge px-2 bg-danger">sin stock</span>
                                                ) : p.alertaStockBajo || p.stockBajo ? (
                                                    <span className="badge px-2 bg-warning text-dark">
                                                        poco stock
                                                    </span>
                                                ) : null}
                                            </div>

                                            {/* Datos */}
                                            <h5 className="card-title">{p.nombre}</h5>
                                            <p className="card-text mb-1">Local: {localesMap[p.local]}</p>
                                            <p className="card-text mb-1">Stock disponible: {p.stock}</p>
                                            {p.precio && (
                                                <p className="card-text mb-1">
                                                    Precio: ${p.precio.toLocaleString()}
                                                </p>
                                            )}

                                            {/* Acciones dinámicas */}
                                            {selectedKey === key && (
                                                <>
                                                    <div className="d-flex gap-2 mt-2 flex-wrap">
                                                        {p.tieneSecundarios ? (
                                                            <p className="text-muted">
                                                                Stock controlado por secundarios
                                                            </p>
                                                        ) : (
                                                            <button
                                                                className="btn btn-sm btn-success"
                                                                onClick={(e) => {
                                                                    e.stopPropagation();
                                                                    handleIncrementarStock(p);
                                                                }}
                                                            >
                                                                +1 STOCK
                                                            </button>
                                                        )}
                                                    </div>

                                                    <div className="d-flex gap-1 mt-2 flex-wrap">
                                                        {p.alertaSinStock || p.alertaStockBajo ? (
                                                            <button
                                                                className="btn btn-sm btn-primary"
                                                                onClick={(e) => {
                                                                    e.stopPropagation();
                                                                    handleAlerta(p, "desactivar");
                                                                }}
                                                            >
                                                                Desactivar alerta
                                                            </button>
                                                        ) : (
                                                            <>
                                                                <button
                                                                    className="btn btn-sm btn-warning"
                                                                    onClick={(e) => {
                                                                        e.stopPropagation();
                                                                        handleAlerta(p, "bajo");
                                                                    }}
                                                                >
                                                                    Alerta stock bajo
                                                                </button>
                                                                <button
                                                                    className="btn btn-sm btn-danger"
                                                                    onClick={(e) => {
                                                                        e.stopPropagation();
                                                                        handleAlerta(p, "sin");
                                                                    }}
                                                                >
                                                                    Alerta sin stock
                                                                </button>
                                                            </>
                                                        )}
                                                    </div>
                                                </>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        </div>
    );
}