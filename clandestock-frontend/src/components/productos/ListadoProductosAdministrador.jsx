import { useEffect, useState } from "react";
import ProductoCard from "./ProductoCard";
import { getAllProductosSecundarios, getProductosSecundarioPorId } from "../../api/productoSecundario";
import { getAllProductosPrimarios, getProductosPrimariosPorId } from "../../api/productoPrimario";
import FormEditarProductoSecundario from "./FormEditarProductoSecundario";
import FormEditarProductoPrincipal from "./FormEditarProductoPrincipal";


export default function ListadoProductosAdministrador() {
    const [productos, setProductos] = useState([]);
    const [filtroNombre, setFiltroNombre] = useState("");
    const [filtroLocal, setFiltroLocal] = useState("");
    const [orden, setOrden] = useState("");
    const [productoEditando, setProductoEditando] = useState(null);
    const [filtroTipo, setFiltroTipo] = useState("");
    const [filtroEstado, setFiltroEstado] = useState("");


    const localesMap = {
        "1": "Tenedor Libre",
        "2": "Termas",
        "3": "Heladería"
    };

    const cargar = async () => {
        try {
            const principales = await getAllProductosPrimarios();
            const secundarios = await getAllProductosSecundarios();

            const productosUnificados = [
                ...principales.map(p => ({
                    tipo: "principal",
                    id: p.id,
                    nombre: p.nombre_producto,
                    stock: parseInt(p.stock, 10),
                    estado: p.estado === "true" ? "activo" : "inactivo",
                    local: p.idLocal,
                    tieneSecundarios: p.tieneSecundarios,
                    comanda: p.comanda === "true" ? "true": "false",
                    aletarStockBajo: p.aletarStockBajo
                })),
                ...secundarios.map(p => ({
                    tipo: "secundario",
                    id: p.id,
                    nombre: p.nombre_producto,
                    stock: parseInt(p.stock, 10),
                    estado: p.estado === "true" ? "activo" : "inactivo",
                    local: p.local,
                    tieneSecundarios: p.tieneSecundarios,
                    comanda: p.comanda === "true" ? "true": "false",
                    aletarStockBajo: p.aletarStockBajo
                })),
            ];

            setProductos(productosUnificados);
        } catch (err) {
            console.error("Error cargando productos:", err);
        }
    };

    useEffect(() => {
        cargar();
    }, []);

    //  Filtros
    let productosFiltrados = productos.filter(p =>
        p.nombre.toLowerCase().includes(filtroNombre.toLowerCase()) &&
        (filtroLocal === "" || p.local === filtroLocal) &&
        (filtroTipo === "" || p.tipo === filtroTipo) &&
        (filtroEstado === "" || p.estado === filtroEstado || p.estado === (filtroEstado === "activo"))
    );


    // Ordenamiento
    if (orden === "stockDesc") {
        productosFiltrados.sort((a, b) => b.stock - a.stock);
    } else if (orden === "stockAsc") {
        productosFiltrados.sort((a, b) => a.stock - b.stock);
    } else if (orden === "localAsc") {
        productosFiltrados.sort((a, b) =>
            localesMap[a.local].localeCompare(localesMap[b.local])
        );
    } else if (orden === "localDesc") {
        productosFiltrados.sort((a, b) =>
            localesMap[b.local].localeCompare(localesMap[a.local])
        );
    } else if (orden === "nombreAsc") {
        productosFiltrados.sort((a, b) =>
            a.nombre.localeCompare(b.nombre)
        );
    } else if (orden === "nombreDesc") {
        productosFiltrados.sort((a, b) =>
            b.nombre.localeCompare(a.nombre)
        );
    }

    // función para editar
    const handleEditarClick = async (id, tipo) => {
        try {
            let datos;
            if (tipo === "secundario") {
                datos = await getProductosSecundarioPorId(id);
                console.log(datos)
            } else {
                datos = await getProductosPrimariosPorId(id);
            }
            setProductoEditando({ ...datos, tipo });
        } catch (err) {
            console.error("Error cargando producto para edición:", err);
        }
    };

    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <div className="card-header bg-warning text-dark">Productos existentes</div>
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
                        value={filtroLocal}
                        onChange={e => setFiltroLocal(e.target.value)}
                    >
                        <option value="">Todos los locales</option>
                        <option value="1">Tenedor Libre</option>
                        <option value="2">Termas</option>
                        <option value="3">Heladería</option>
                    </select>
                    <select
                        className="form-select"
                        value={filtroTipo}
                        onChange={e => setFiltroTipo(e.target.value)}
                    >
                        <option value="">Todos los tipos</option>
                        <option value="principal">Solo principales</option>
                        <option value="secundario">Solo secundarios</option>
                    </select>

                    <select
                        className="form-select"
                        value={filtroEstado}
                        onChange={e => setFiltroEstado(e.target.value)}
                    >
                        <option value="">Todos los estados</option>
                        <option value="activo">Activos</option>
                        <option value="inactivo">Inactivos</option>
                    </select>

                    <select
                        className="form-select"
                        value={orden}
                        onChange={e => setOrden(e.target.value)}
                    >
                        <option value="">Sin orden</option>
                        <option value="stockDesc">Stock: mayor a menor</option>
                        <option value="stockAsc">Stock: menor a mayor</option>
                        <option value="localAsc">Local: A → Z</option>
                        <option value="localDesc">Local: Z → A</option>
                        <option value="nombreAsc">Producto: A → Z</option>
                        <option value="nombreDesc">Producto: Z → A</option>
                    </select>

                </div>

                {/* Lista o edición */}
                {productoEditando ? (
                    productoEditando.tipo === "secundario" ? (
                        <FormEditarProductoSecundario
                            producto={productoEditando}
                            onClose={() => { setProductoEditando(null); cargar(); }}
                        />
                    ) : (
                        <FormEditarProductoPrincipal
                            producto={productoEditando}
                            onClose={() => { setProductoEditando(null); cargar(); }}
                        />
                    )
                ) : productosFiltrados.length === 0 ? (
                    <p className="text-muted">No hay productos cargados</p>
                ) : (
                    <div className="row">
                        {productosFiltrados.map((p) => (
                            <div key={`${p.tipo}-${p.id}`} className="col-12 col-md-6 col-lg-4">
                                <ProductoCard producto={p} onEditarClick={handleEditarClick} />
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
