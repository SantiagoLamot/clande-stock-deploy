import { useEffect, useState } from "react";
import FuncionesModerador from "../../components/FuncionesModerador";
import VistaPedidos from "../../components/VistaPedido";
import VistaVentasCerradas from "../../components/VistaVentasCerradas";
import PanelNuevaVenta from "../../components/PanelNuevaVenta";
import { getEstadoCaja, getDetalleCaja } from "../../api/caja";
import { getVentasActivas, getVentasCerradas } from "../../api/pedidos";
import PanelReporte from "../../components/PanelReporte";
import ListadoProductosModerador from "../../components/productos/ListadoProductosModerador";
import { getAlertasStockPrimario, getAlertasStockSecundario } from "../../api/alertasStock";
import { AbrirCaja } from "../../components/caja/AbrirCaja";
import { CerrarCaja } from "../../components/caja/CerrarCaja";
import { MesasMozos } from "../../components/MesasMozos";

export const ModeradorPage = () => {
  const [cajaAbierta, setCajaAbierta] = useState(false);
  const [caja, setCaja] = useState(null);
  const [detalleCaja, setDetalleCaja] = useState(null);
  const [vistaActiva, setVistaActiva] = useState("pedidos");
  const [cantidadAlertas, setCantidadAlertas] = useState(0);
  const [estado, setEstado] = useState(null);

  const [pedidos, setPedidos] = useState({
    local: [],
    takeaway: [],
    delivery: [],
  });

  const handleCajaAbierta = async (nuevaCaja) => {
    setCaja(nuevaCaja);
    setCajaAbierta(true);
    const detalle = await getDetalleCaja();
    setDetalleCaja(detalle[0]);
    setVistaActiva("pedidos"); // o la vista que quieras mostrar automáticamente
  };

  const [pedidosCerrados, setPedidosCerrados] = useState({
    local: [],
    takeaway: [],
    delivery: [],
  });

  const refrescarPedidos = async () => {
    try {
      const pedidosData = await getVentasActivas();
      setPedidos({
        local: pedidosData.filter((p) => p.tipoVenta === "CONSUMO_LOCAL"),
        takeaway: pedidosData.filter((p) => p.tipoVenta === "TAKE_AWAY"),
        delivery: pedidosData.filter((p) => p.tipoVenta === "ENVIO_DOMICILIO"),
      });
    } catch (error) {
      console.error("Error refrescando pedidos:", error);
    }
  };

  const refrescarPedidosCerrados = async () => {
    try {
      const cerrados = await getVentasCerradas();
      setPedidosCerrados({
        local: cerrados.filter((p) => p.tipoVenta === "CONSUMO_LOCAL"),
        takeaway: cerrados.filter((p) => p.tipoVenta === "TAKE_AWAY"),
        delivery: cerrados.filter((p) => p.tipoVenta === "ENVIO_DOMICILIO"),
      });
    } catch (error) {
      console.error("Error cargando ventas cerradas:", error);
    }
  };

  useEffect(() => {
    if (vistaActiva === "ventasCerradas") {
      refrescarPedidosCerrados();
    }
    cargarDatos();
  }, [vistaActiva]);


  const cargarDatos = async () => {
    try {
      const cajas = await getEstadoCaja();
      const abierta =
        Array.isArray(cajas) && cajas.length > 0 ? cajas[0] : null;
      setCaja(abierta);
      setCajaAbierta(String(abierta?.estado).toLowerCase() === "true");

      const pedidosData = await getVentasActivas();
      setPedidos({
        local: pedidosData.filter((p) => p.tipoVenta === "CONSUMO_LOCAL"),
        takeaway: pedidosData.filter((p) => p.tipoVenta === "TAKE_AWAY"),
        delivery: pedidosData.filter(
          (p) => p.tipoVenta === "ENVIO_DOMICILIO"
        ),
      });

      const cerradosData = await getVentasCerradas();
      setPedidosCerrados({
        local: cerradosData.filter((p) => p.tipoVenta === "CONSUMO_LOCAL"),
        takeaway: cerradosData.filter((p) => p.tipoVenta === "TAKE_AWAY"),
        delivery: cerradosData.filter(
          (p) => p.tipoVenta === "ENVIO_DOMICILIO"
        ),
      });

      const detalle = await getDetalleCaja();
      setDetalleCaja(detalle[0]);
    } catch (error) {
      console.error("Error cargando datos del moderador:", error);
    }
  };
  useEffect(() => {

    cargarDatos();
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
  useEffect(() => {
    cargarDatos();
    cargarAlertas();
  }, []);
  return (
    <div className="container-fluid" style={{ height: "calc(100vh - 67px)" }}>
      <div className="row h-100">
        <div className="col-4 bg-dark text-light p-3 d-flex flex-column">
          <FuncionesModerador
            cajaAbierta={cajaAbierta}
            caja={caja}
            detalleCaja={detalleCaja}
            setVistaActiva={setVistaActiva}
            cantidadAlertas={cantidadAlertas}
          />
        </div>
        <div className="col-8 bg-light text-muted">
          {vistaActiva === "pedidos" && (
            <VistaPedidos
              pedidos={pedidos}
              cajaAbierta={cajaAbierta}
              refrescarPedidos={refrescarPedidos}
            />
          )}

          {vistaActiva === "ventasCerradas" && (
            <VistaVentasCerradas
              pedidos={pedidosCerrados}
              cajaAbierta={cajaAbierta}
              setPedidoSeleccionado={() => { }}
            />
          )}

          {vistaActiva === "nuevaVenta" && <PanelNuevaVenta />}

          {vistaActiva === "productos" && <ListadoProductosModerador cargarAlertas={cargarAlertas} />}

          {vistaActiva === "reportes" && <PanelReporte />}

          {vistaActiva === "mesasMozos" && <MesasMozos />}

          {vistaActiva === "abrirCaja" && <AbrirCaja onSuccess={handleCajaAbierta} />}

          {vistaActiva === "cerrarCaja" && (
            <CerrarCaja
              onSuccess={(cajaResponse) => {
                setCaja(cajaResponse);
                setCajaAbierta(false);
                setDetalleCaja(null);
                setVistaActiva("pedidos"); // volvés a pedidos o la vista que quieras
              }}
            />
          )}
        </div>
      </div>
    </div>
  );
};
