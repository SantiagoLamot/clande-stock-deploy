import { useState } from "react";
import { ListadoMetodosPago } from "../../components/ventas/ListadoMetodosPago";
import { FormNuevoMetodoPago } from "../../components/ventas/FormNuevoMetodoPago";
import { ListadoVentasCerradas } from "../../components/ventas/ListadoVentasCerradas";
import { ListadoCajasCerradas } from "../../components/caja/ListadoCajasCerradas";
import { ListadoCajasAbiertas } from "../../components/caja/ListadoCajasAbiertas";

export const AdminVentasPage = () => {
  const [vistaActiva, setVistaActiva] = useState("listado");
  const [estado, setEstado] = useState(null);
  const [metodos, setMetodos] = useState([]);

  return (
    <div className="container-fluid" style={{ height: "calc(100vh - 67px)" }}>
      <div className="row h-100">
        {/* Sidebar */}
        <div className="col-md-3 bg-dark text-light p-3 d-flex flex-column overflow-auto">
          <h4 className="mb-4 text-center">Administrar ventas</h4>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("cajasAbiertas")}
          >
            Cajas abiertas
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("cajasCerradas")}
          >
            Cajas cerradas
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("ventasCerradas")}
          >
            Ventas cerradas
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("listado")}
          >
            Métodos de pago
          </button>
          <button
            className="btn btn-outline-light mb-2 flex-shrink-0"
            onClick={() => setVistaActiva("nuevo")}
          >
            Agregar método de pago
          </button>
        </div>

        {/* Panel dinámico */}
        <div className="col-md-9 bg-light text-dark p-4 d-flex flex-column h-100 overflow-auto">
          {estado && (
            <div
              className={`alert ${estado.tipo === "success" ? "alert-success" : "alert-danger"}`}
            >
              {estado.mensaje}
            </div>
          )}

          {vistaActiva === "listado" && (
            <ListadoMetodosPago estado={estado} setEstado={setEstado} />
          )}

          {vistaActiva === "nuevo" && (
            <FormNuevoMetodoPago
              estado={estado}
              setEstado={setEstado}
              onCreated={(nuevo) => setMetodos([...metodos, nuevo])}
            />
          )}

          {vistaActiva === "ventasCerradas" && <ListadoVentasCerradas />}

          {vistaActiva === "cajasCerradas" && <ListadoCajasCerradas />}

          {vistaActiva === "cajasAbiertas" && <ListadoCajasAbiertas />}
        </div>
      </div>
    </div>
  );
};
