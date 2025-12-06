import { useEffect, useState } from "react";
import PanelPedidos from "./PanelPedidos";
import PanelDetallePedido from "./PanelDetallePedido";

export default function VistaPedidos({
  pedidos,
  cajaAbierta,
  refrescarPedidos,
}) {
  const [pedidoSeleccionado, setPedidoSeleccionado] = useState(null);

  useEffect(() => {
    if (!pedidoSeleccionado) {
      refrescarPedidos(); // 👈 refresca listado al volver
    }
  }, [pedidoSeleccionado]);

  return (
    <div className="h-100">
      {pedidoSeleccionado ? (
        <PanelDetallePedido
          pedido={pedidoSeleccionado}
          onBack={() => setPedidoSeleccionado(null)}
        />
      ) : (
        <PanelPedidos
          pedidos={pedidos}
          cajaAbierta={cajaAbierta}
          setPedidoSeleccionado={setPedidoSeleccionado}
        />
      )}
    </div>
  );
}
