import { useEffect, useState } from "react";
import axios from "../../api/axios";

export default function StockVentaPanel() {
    const [stockItems, setStockItems] = useState([]);
    const [filtroNombre, setFiltroNombre] = useState("");
    const [filtroEstado, setFiltroEstado] = useState("");
    const [filtroLocal, setFiltroLocal] = useState("");

    useEffect(() => {
        const cargarStock = async () => {
            try {
                const res = await axios.get("/productos/stock");
                setStockItems(res.data);
            } catch (err) {
                console.error("Error cargando stock a la venta:", err);
            }
        };
        cargarStock();
    }, []);

    const filtrados = stockItems.filter(item =>
        item.nombreProducto.toLowerCase().includes(filtroNombre.toLowerCase()) &&
        (filtroEstado === "" ||
            (filtroEstado === "sinStock" && item.sinStock === "true") ||
            (filtroEstado === "stockBajo" && item.stockBajo === "true")) &&
        (filtroLocal === "" || item.local === filtroLocal)
    );
    const localesMap = {
        "1": "Tenedor Libre",
        "2": "Termas",
        "3": "Heladería"
    };
    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <div className="card-header bg-warning text-dark">Stock a la venta</div>
            <div className="card-body overflow-auto">
                {/* Filtros */}
                <div className="mb-3 d-flex gap-3 flex-wrap">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Buscar por nombre..."
                        value={filtroNombre}
                        onChange={e => setFiltroNombre(e.target.value)}
                    />
                    <select
                        className="form-select"
                        value={filtroEstado}
                        onChange={e => setFiltroEstado(e.target.value)}
                    >
                        <option value="">Todos</option>
                        <option value="stockBajo">Stock bajo</option>
                        <option value="sinStock">Sin stock</option>
                    </select>
                    <select
                        className="form-select"
                        value={filtroLocal}
                        onChange={e => setFiltroLocal(e.target.value)}
                    >
                        <option value="">Todos los locales</option>
                        <option value="1">Tenedor Libre</option>
                        <option value="2">Termas</option>
                        <option value="3">Heladería</option>
                    </select>
                </div>

                {/* Lista */}
                {filtrados.length === 0 ? (
                    <p className="text-muted">No hay productos disponibles</p>
                ) : (
                    <div className="row">
                        {filtrados.map(item => (
                            <div key={item.productoPrincipalId} className="col-12 col-md-6 col-lg-4">
                                <div className="card mb-3 shadow-sm">
                                    <div className="card-body">
                                        <h5 className="card-title">{item.nombreProducto}</h5>
                                        <h6 className="card-text mb-1">{localesMap[item.local] || "Desconocido"}</h6>
                                        <p className="card-text mb-1">Stock disponible: {item.stockDisponible}</p>
                                        <p className="card-text mb-1">Precio: ${parseFloat(item.precio).toLocaleString()}</p>

                                        {item.sinStock === "true" && (
                                            <span className="badge bg-danger me-1">Sin stock</span>
                                        )}
                                        {item.stockBajo === "true" && (
                                            <span className="badge bg-warning text-dark">Stock bajo</span>
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
