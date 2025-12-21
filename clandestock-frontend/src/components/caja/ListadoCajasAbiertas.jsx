import { useEffect, useState } from "react";
import { getAllCajasAbiertas } from "../../api/caja";
import { VentasCerradasPorCaja } from "../../api/ventas";
import { VentaCerradaCard } from "../ventas/VentaCerradaCard";
import { getMetodos } from "../../api/metodoPago";
import { PuntoLive } from "../PuntoLive";
import Title from "../Title";

export const ListadoCajasAbiertas = () => {
    const [cajas, setCajas] = useState([]);
    const [paginaActual, setPaginaActual] = useState(1);
    const [cajaSeleccionada, setCajaSeleccionada] = useState(null);
    const [ventasCaja, setVentasCaja] = useState([]);
    const cajasPorPagina = 6;
    const [metodos, setMetodos] = useState([]);

    const localesMap = {
        "1": "Tenedor Libre",
        "2": "Termas",
        "3": "Heladería",
    };


    useEffect(() => {
        const cargarCajas = async () => {
            try {
                const response = await getAllCajasAbiertas();
                const m = await getMetodos();
                const data = Array.isArray(response) ? response : [response];
                const ordenadas = data.sort(
                    (a, b) => new Date(b.fechaCierre) - new Date(a.fechaCierre)
                );
                setCajas(ordenadas);
                setMetodos(m);
            } catch (err) {
                console.error("Error cargando cajas cerradas:", err);
                setCajas([]);
            }
        };
        cargarCajas();
    }, []);

    const handleClickCaja = async (cajaId) => {
        try {
            setCajaSeleccionada(cajaId);
            const ventas = await VentasCerradasPorCaja(cajaId);
            setVentasCaja(ventas);
        } catch (err) {
            console.error("Error cargando ventas de la caja:", err);
            setVentasCaja([]);
        }
    };

    // Paginación
    const totalPaginas = Math.ceil(cajas.length / cajasPorPagina);
    const inicio = (paginaActual - 1) * cajasPorPagina;
    const fin = inicio + cajasPorPagina;
    const cajasPagina = cajas.slice(inicio, fin);

    // Vista de ventas de una caja seleccionada
    if (cajaSeleccionada) {
        return (
            <div className="d-flex flex-column h-100">
                <button
                    className="btn btn-secondary mb-3 align-self-start"
                    onClick={() => {
                        setCajaSeleccionada(null);
                        setVentasCaja([]);
                    }}
                >
                    ← Volver a listado de cajas
                </button>

                <h4>Ventas de la Caja #{cajaSeleccionada}</h4>
                <div className="row flex-grow-1 overflow-auto gy-2">
                    {console.log(ventasCaja)}
                    {ventasCaja.map((venta) => {
                        const metodo = metodos.find((m) => m.id === venta.idMetodoPago);
                        return <VentaCerradaCard venta={venta} metodo={metodo} />
                    })
                    }
                </div>
            </div>
        );
    }

    // Vista de listado de cajas
    if (!Array.isArray(cajas) || cajas.length === 0) {
        return <p className="text-muted">No hay cajas abiertas</p>;
    }

    return (
        <div className="d-flex flex-column">
            <div className="row flex-grow-1 overflow-auto gy-2">
                <Title text={"Cajas abiertas"} />
                {cajasPagina.map((caja) => (
                    <div
                        key={caja.cajaId}
                        className="col-12 col-md-6 col-lg-4"
                        onClick={() => handleClickCaja(caja.cajaId)}
                        style={{ cursor: "pointer" }}
                    >
                        <div className="card shadow-sm h-100">
                            <div className="card-body d-flex flex-column">
                                {!caja.fechaCierre && (
                                    <PuntoLive />
                                )}
                                <h5 className="card-title">Caja #{caja.cajaId}</h5>
                                <p className="card-text mb-1">
                                    <strong>Local:</strong> {localesMap[caja.idLocal]}
                                </p>
                                <p className="card-text mb-1">
                                    <strong>Fecha apertura:</strong> {
                                        (() => {
                                            const fecha = new Date(caja.fechaApertura);

                                            // Ajusto manualmente restando 3 horas
                                            fecha.setHours(fecha.getHours() - 3);

                                            // Ahora uso los getters locales
                                            const dd = String(fecha.getDate()).padStart(2, "0");
                                            const mm = String(fecha.getMonth() + 1).padStart(2, "0");
                                            const yyyy = fecha.getFullYear();
                                            const hh = String(fecha.getHours()).padStart(2, "0");
                                            const min = String(fecha.getMinutes()).padStart(2, "0");

                                            return `${dd}/${mm}/${yyyy} ${hh}:${min}`;
                                        })()
                                    }

                                </p>

                                {caja.fechaCierre && (
                                    <p className="card-text mb-1">
                                        <strong>Fecha cierre:</strong>
                                        {
                                            (() => {
                                                const fecha = new Date(caja.fechaCierre);
                                                // Ajusto manualmente a hora local de la DB (UTC-3)
                                                fecha.setHours(fecha.getHours() - 3);
                                                const dd = String(fecha.getUTCDate()).padStart(2, "0");
                                                const mm = String(fecha.getUTCMonth() + 1).padStart(2, "0");
                                                const yyyy = fecha.getUTCFullYear();
                                                const hh = String(fecha.getUTCHours()).padStart(2, "0");
                                                const min = String(fecha.getUTCMinutes()).padStart(2, "0");
                                                return `${dd}/${mm}/${yyyy} ${hh}:${min}`;
                                            })()
                                        }
                                    </p>
                                )}

                                <p className="card-text mb-1">
                                    <strong>Total general:</strong>{" "}
                                    ${parseFloat(caja.totalGeneral).toLocaleString()}
                                </p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* Controles de paginación */}
            <div className="mt-3 d-flex justify-content-center gap-2">
                <button
                    className="btn btn-outline-primary btn-sm"
                    disabled={paginaActual === 1}
                    onClick={() => setPaginaActual(paginaActual - 1)}
                >
                    Anterior
                </button>
                <span>
                    Página {paginaActual} de {totalPaginas}
                </span>
                <button
                    className="btn btn-outline-primary btn-sm"
                    disabled={paginaActual === totalPaginas}
                    onClick={() => setPaginaActual(paginaActual + 1)}
                >
                    Siguiente
                </button>
            </div>
        </div>
    );
};
