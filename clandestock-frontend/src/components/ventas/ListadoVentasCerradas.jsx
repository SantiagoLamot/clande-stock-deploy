import { useEffect, useState } from "react";
import { getAllVentasCerradas } from "../../api/ventas";
import { VentaCerradaCard } from "./VentaCerradaCard";
import { getMetodos } from "../../api/metodoPago";
import Title from "../Title";

const tiposVenta = ["TAKE_AWAY", "CONSUMO_LOCAL", "ENVIO_DOMICILIO"];

export const ListadoVentasCerradas = () => {
    const [ventas, setVentas] = useState([]);
    const [metodos, setMetodos] = useState([]);
    const [filtroLocal, setFiltroLocal] = useState("todos");
    const [filtroTipo, setFiltroTipo] = useState("todos");
    const [paginaActual, setPaginaActual] = useState(1);
    const ventasPorPagina = 6;

    useEffect(() => {
        const cargarVentas = async () => {
            try {
                const v = await getAllVentasCerradas();
                const m = await getMetodos();
                const data = Array.isArray(v) ? v : [];
                const ordenadas = data.sort(
                    (a, b) => new Date(b.fechaCierre) - new Date(a.fechaCierre)
                );
                setVentas(ordenadas);
                setMetodos(m);
            } catch (err) {
                console.error("Error cargando ventas cerradas:", err);
                setVentas([]);
            }
        };
        cargarVentas();
    }, []);

    const ventasFiltradas = ventas.filter((v) => {
        const coincideLocal =
            filtroLocal === "todos" || String(v.localId) === filtroLocal;
        const coincideTipo = filtroTipo === "todos" || v.tipoVenta === filtroTipo;
        return coincideLocal && coincideTipo;
    });

    // Calcular paginación
    const totalPaginas = Math.ceil(ventasFiltradas.length / ventasPorPagina);
    const inicio = (paginaActual - 1) * ventasPorPagina;
    const fin = inicio + ventasPorPagina;
    const ventasPagina = ventasFiltradas.slice(inicio, fin);

    if (!Array.isArray(ventas) || ventas.length === 0) {
        return <p className="text-muted">No hay ventas cerradas</p>;
    }

    return (
        <div className="d-flex flex-column h-100">
            <Title text={"Ventas cerradas"}/>
            {/* Filtros */}
            <div className="mb-3 d-flex flex-wrap gap-3 align-items-center">
                <div>
                    <label className="form-label me-2">Filtrar por local:</label>
                    <select
                        className="form-select w-auto d-inline-block"
                        value={filtroLocal}
                        onChange={(e) => {
                            setFiltroLocal(e.target.value);
                            setPaginaActual(1); // resetear página
                        }}
                    >
                        <option value="todos">Todos</option>
                        <option value="1">Tenedor Libre</option>
                        <option value="2">Termas</option>
                        <option value="3">Heladería</option>
                    </select>
                </div>

                <div>
                    <label className="form-label me-2">Filtrar por tipo:</label>
                    <select
                        className="form-select w-auto d-inline-block"
                        value={filtroTipo}
                        onChange={(e) => {
                            setFiltroTipo(e.target.value);
                            setPaginaActual(1); // resetear página
                        }}
                    >
                        <option value="todos">Todos</option>
                        {tiposVenta.map((tipo) => (
                            <option key={tipo} value={tipo}>
                                {tipo}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            {/* Listado de ventas */}
            <div className="row flex-grow-1 overflow-auto gy-2">
                {ventasPagina.map((venta) => {
                    const metodo = metodos.find((m) => m.id === venta.idMetodoPago);
                    return <VentaCerradaCard venta={venta} metodo={metodo} />;
                })}
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
