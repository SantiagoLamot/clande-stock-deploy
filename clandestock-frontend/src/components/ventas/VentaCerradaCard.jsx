import { PuntoLive } from "../PuntoLive";

const localesMap = {
    "1": "Tenedor Libre",
    "2": "Termas",
    "3": "Heladería",
};

const agruparProductos = (productos) => {
    const agrupados = {};
    productos.forEach((p) => {
        const key = `${p.idProducto}-${p.nombreProducto}-${p.precioProducto}`;
        if (!agrupados[key]) {
            agrupados[key] = {
                idProducto: p.idProducto,
                nombreProducto: p.nombreProducto,
                precioProducto: parseFloat(p.precioProducto),
                cantidad: 1,
            };
        } else {
            agrupados[key].cantidad += 1;
        }
    });
    return Object.values(agrupados);
};

export const VentaCerradaCard = ({ venta, metodo }) => {
    return (
        <div className="col-12 col-md-6 col-lg-4">
            <div className="card shadow-sm">
                <div className="card-body d-flex flex-column">
                    <h5 className="card-title">Venta #{venta.idVenta}</h5>
                    {/* Punto verde arriba a la derecha */}
                    {!venta.fechaCierre && (
                        <PuntoLive/>
                    )}
                    <p className="card-text mb-1">
                        <strong>Local:</strong> {localesMap[venta.localId]}
                    </p>
                    <p className="card-text mb-1">
                        <strong>Tipo:</strong> {venta.tipoVenta}
                    </p>
                    <p className="card-text mb-1">
                        <strong>Detalle entrega:</strong> {venta.detalleEntrega}
                    </p>
                    {venta.numMesa && (
                        <p className="card-text mb-1">
                            <strong>Mesa:</strong> {venta.numMesa}
                        </p>
                    )}
                    {venta.fechaCierre && (
                        <p className="card-text mb-1">
                            <strong>Fecha cierre:</strong>{" "}
                            {venta.fechaCierre.replace("T", " ").split(".")[0]}
                        </p>
                    )}
                    {metodo && (
                        <p className="card-text mb-1">
                            <strong>Método de pago:</strong> {metodo.nombre_metodo_pago}
                        </p>
                    )}

                    {/* Productos agrupados */}
                    <h6 className="mt-3">Productos:</h6>
                    <div
                        className="list-group flex-grow-1 overflow-auto"
                        style={{ maxHeight: "150px" }}
                    >
                        {agruparProductos(venta.productos).map((prod, index) => (
                            <div
                                key={index}
                                className="list-group-item d-flex justify-content-between"
                            >
                                <span>
                                    {prod.cantidad} × {prod.nombreProducto}
                                </span>
                                <span>
                                    ${(prod.precioProducto * prod.cantidad).toLocaleString()}
                                </span>
                            </div>
                        ))}
                    </div>

                    {/* Subtotal vs Total */}
                    {parseFloat(venta.precioTotal) !==
                        parseFloat(venta.precioTotalConMetodoDePago) ? (
                        <>
                            <p className="card-text mb-1">
                                <strong>Subtotal:</strong>{" "}
                                ${parseFloat(venta.precioTotal).toLocaleString()}
                            </p>
                            {metodo && (
                                <p className="card-text mb-1">
                                    {parseFloat(metodo.incremento) > 0 && (
                                        <span className="badge bg-danger ms-2">
                                            +{metodo.incremento}% incremento
                                        </span>
                                    )}
                                    {parseFloat(metodo.descuento) > 0 && (
                                        <span className="badge bg-success ms-2">
                                            -{metodo.descuento}% descuento
                                        </span>
                                    )}
                                </p>
                            )}
                            {venta.fechaCierre && (
                                <p className="card-text mb-1">
                                    <strong>Total:</strong>{" "}
                                    ${parseFloat(venta.precioTotalConMetodoDePago).toLocaleString()}
                                </p>
                            )
                            }
                        </>
                    ) : (
                        <p className="card-text mb-1">
                            <strong>Total:</strong>{" "}
                            ${parseFloat(venta.precioTotalConMetodoDePago).toLocaleString()}
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};
