import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const nombresLocales = {
    MODERADOR_TENEDOR_LIBRE: "Tenedor Libre",
    MODERADOR_TERMAS: " Termas",
    MODERADOR_HELADERIA: "Heladeria",
};

export default function FuncionesModerador({ cajaAbierta, caja, detalleCaja, setVistaActiva, cantidadAlertas }) {
    const { user } = useContext(AuthContext);
    const nombreLocal = user?.tipoUsuario ? nombresLocales[user.tipoUsuario] : "";

    return (
        <>
            <div className="mb-3 text-center">
                {nombreLocal && <h4 className="text-light">{nombreLocal}</h4>}
            </div>

            <button
                className="btn btn-outline-light mb-3"
                disabled={!cajaAbierta}
                onClick={() => setVistaActiva("pedidos")}
            >
                Pedidos abiertos
            </button>
            <button
                className="btn btn-outline-light mb-3"
                disabled={!cajaAbierta}
                onClick={() => setVistaActiva("nuevaVenta")}
            >
                Nueva venta
            </button>

            <button
                className="btn btn-outline-light mb-3 position-relative"
                onClick={() => setVistaActiva("productos")}
            >
                Productos
                {cantidadAlertas > 0 && (
                    <span
                        className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                        style={{ fontSize: "0.75rem" }}
                    >
                        {cantidadAlertas}
                    </span>
                )}
            </button>

            <button
                className="btn btn-outline-light mb-3"
                disabled={!cajaAbierta}
                onClick={() => setVistaActiva("ventasCerradas")}
            >
                Ventas cerradas
            </button>

            <button
                className="btn btn-outline-light mb-3"
                onClick={() => setVistaActiva("reportes")}
            >
                Enviar reportes
            </button>


            <button
                className="btn btn-outline-light mb-3"
                onClick={() => setVistaActiva("mesasMozos")}
            >
                Mesas / Mozos
            </button>

            <button
                className={`btn mb-3 ${cajaAbierta ? 'btn-danger' : 'btn-success'}`}
                onClick={() => cajaAbierta ? setVistaActiva("cerrarCaja") : setVistaActiva("abrirCaja")}
            >
                {cajaAbierta ? 'Cerrar caja' : 'Abrir caja'}
            </button>

            {cajaAbierta && caja && (
                <div className="text-light mt-3">
                    <small>
                        Apertura de caja: {new Date(caja.fechaApertura).toLocaleString()}
                    </small>
                    <br />
                    {detalleCaja && (
                        <div className="mt-2">
                            <div className="mt-2">
                                <strong><p>Total general: ${(detalleCaja.totalGeneral + Number(caja.montoApertura)).toFixed(2)}</p></strong>
                            </div>
                            <h6>Detalle por método de pago:</h6>
                            <ul className="list-unstyled">
                                {/* monto de apertura como primer método */}
                                <li>
                                    • Monto de apertura: ${caja.montoApertura}
                                </li>
                                {detalleCaja.detallePorMetodo.map((d, idx) => (
                                    <li key={idx}>
                                        {"• " + d.metodoPago}: ${d.totalCobrado}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            )}
        </>
    );
}
