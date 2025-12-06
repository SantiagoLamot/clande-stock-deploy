import { useEffect, useState } from "react";
import FormProductoSecundario from "../../components/productos/FormNuevoProductoSecundario";

import { guardarProductoSecundario } from "../../api/productoSecundario";
import ListadoProductosAdministrador from "../../components/productos/ListadoProductosAdministrador";
import FormNuevoProductoPrincipal from "../../components/productos/FormNuevoProductoPrincipal";
import { guardarProductoPrincipal } from "../../api/productoPrimario";
import AdminCategorias from "../../components/productos/AdminCategorias";
import StockVentaPanel from "../../components/productos/StockVentaPanel";
import AdminAlertasStock from "../../components/productos/AdminAlertasStock";
import { getAlertasStockPrimario, getAlertasStockSecundario } from "../../api/alertasStock";


export const AdminProductosPage = ({ setCantidadAlertas, cantidadAlertas}) => {
  const [productos, setProductos] = useState([]);
  const [vistaActiva, setVistaActiva] = useState("listado");
  const [estado, setEstado] = useState(null);
  useEffect(() => {
    cargarAlertas();
    setEstado(null)
  }, []);

  const cargarAlertas = async () => {
  try {
    const primarios = await getAlertasStockPrimario();
    const secundarios = await getAlertasStockSecundario();

    const primariosMapped = primarios.map(p => ({
      ...p,
      tipo: "principal",
      alerta: p.sinStock === "true" || p.stockDisponible === "0" ? "sin stock" : "poco stock"
    }));

    const secundariosMapped = secundarios.map(p => ({
      ...p,
      tipo: "secundario",
      alerta: p.sinStock === "true" || p.stockDisponible === "0" ? "sin stock" : "poco stock"
    }));

    setCantidadAlertas(primariosMapped.length + secundariosMapped.length);
  } catch (err) {
    console.error("Error cargando alertas:", err);
  }
};


  const handleNuevoProductoSecundario = async (producto) => {
    try {
      const saved = await guardarProductoSecundario(producto);
      setProductos([...productos, saved]);
      setEstado({ tipo: "success", mensaje: "Producto creado con exito ✅" });
    } catch (error) {
      console.error("Error guardando producto secundario:", error);
      setEstado({ tipo: "error", mensaje: "Error al crear producto ❌" });
    }
  };

  const handleNuevoProductoPrincipal = async (producto) => {
    try {
      const saved = await guardarProductoPrincipal(producto);
      setEstado({ tipo: "success", mensaje: "Producto creado con exito ✅" });
    } catch (error) {
      console.error("Error guardando producto primario:", error);
      setEstado({ tipo: "error", mensaje: "Error al crear producto ❌" });
    }
  };

  return (
    <div className="container-fluid" style={{ height: "calc(100vh - 67px)" }}>
      <div className="row h-100">
        {/* Sidebar */}
        <div className="col-md-3 bg-dark text-light p-3 d-flex flex-column overflow-auto">
          <h4 className="mb-4 text-center">Administrar productos</h4>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0 position-relative"
            onClick={() => setVistaActiva("alertas")}
          >
            Alertas de stock
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
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("stock")}
          >
            Stock a la venta
          </button>

          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("listado")}
          >
            Listado de productos
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("principal")}
          >
            Agregar producto principal
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("secundario")}
          >
            Agregar producto secundario
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("categorias")}
          >
            Categorias
          </button>

        </div>

        {/* Panel dinámico */}
        <div className="col-md-9 bg-light text-dark p-4 d-flex flex-column h-100 overflow-auto">
          {vistaActiva === "principal" && (
            <div className="card flex-grow-1 d-flex flex-column">
              <div className="card-header bg-warning text-dark">
                Agregar producto principal
              </div>
              <div className="card-body overflow-auto">
                <FormNuevoProductoPrincipal onSubmit={handleNuevoProductoPrincipal} estado={estado} setEstado={setEstado}/>
              </div>
            </div>
          )}

          {vistaActiva === "secundario" && (
            <div className="card flex-grow-1 d-flex flex-column">
              <div className="card-header bg-warning text-dark">
                Agregar producto secundario
              </div>
              <div className="card-body overflow-auto">
                <FormProductoSecundario onSubmit={handleNuevoProductoSecundario} estado={estado} setEstado={setEstado}/>
              </div>
            </div>
          )}

          {vistaActiva === "categorias" && <AdminCategorias />}

          {vistaActiva === "listado" && <ListadoProductosAdministrador />}

          {vistaActiva === "stock" && <StockVentaPanel />}

          {vistaActiva === "alertas" && (
            <AdminAlertasStock setCantidadAlertas={setCantidadAlertas} />
          )}
        </div>
      </div>
    </div>
  );
};
