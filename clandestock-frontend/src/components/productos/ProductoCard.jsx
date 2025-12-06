import { useState } from "react";

export default function ProductoCard({ producto, onEditarClick }) {
    const [showActions, setShowActions] = useState(false);

    const handleCardClick = () => {
        setShowActions(prev => !prev);
    };

    const localesMap = {
        "1": "Tenedor Libre",
        "2": "Termas",
        "3": "Heladería"
    };

    const nombreLocal = localesMap[producto.local] || producto.local;

    return (
        <div
            className="card mb-3 shadow-sm cursor-pointer"
            onClick={handleCardClick}
            style={{ minHeight: "180px", display: "flex", flexDirection: "column", justifyContent: "space-between" }}
        >
            <div className="card-body">
                {/* Badge arriba */}
                <div className="d-flex justify-content-between align-items-center mb-2">
                    {/* Badge tipo (izquierda) */}
                    <span className={`badge px-2 ${producto.tipo === "principal" ? "bg-primary" : "bg-secondary"}`}>
                        {producto.tipo}
                    </span>

                    {/* Badge estado (derecha) */}
                    <span className={`badge px-2 ${producto.estado === true || producto.estado === "activo" ? "bg-success" : "bg-danger"}`}>
                        {producto.estado === true || producto.estado === "activo" ? "activo" : "inactivo"}
                    </span>
                </div>


                {/* Nombre sin truncar */}
                <h5 className="card-title mb-2" style={{ wordBreak: "break-word" }}>
                    {producto.nombre}
                </h5>

                {/* Datos */}
                <p className="card-text mb-0">
                    <strong>Local:</strong> {nombreLocal}
                </p>
                <p className="card-text mb-1">
                    {producto.tieneSecundarios === "true" ? (
                        <p className="text-muted">Stock controlado por secundarios</p>
                    ) : (
                        <>
                            <strong>Stock:</strong> {producto.stock}
                        </>
                    )}
                </p>
            </div>

            {/* Botón Editar */}
            <div
                className={`card-footer d-flex justify-content-end transition-all`}
                style={{ height: showActions ? "auto" : 0, overflow: "hidden", padding: showActions ? "0.5rem" : 0 }}
            >
                <button
                    className="btn btn-sm btn-warning"
                    onClick={(e) => {
                        e.stopPropagation();
                        onEditarClick(producto.id, producto.tipo);
                    }}
                >
                    Editar
                </button>
            </div>
        </div>
    );
}
