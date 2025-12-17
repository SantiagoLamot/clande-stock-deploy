import { imprimioComanda } from "../../api/ventas";

export default function TicketComanda({ venta, onVentaActualizada }) {

    // Simula salto de línea sin crear párrafos
    const simulateLineBreaks = (text, width = 25) => {
        const lines = text.split("\n");
        return lines.map(line => line.padEnd(width) + "\r").join("");
    };

    const handlePrint = async () => {
        if (!venta || venta.productos.length === 0) return;

        const WIDTH = 25;

        const fecha = new Date();
        const fechaActual = fecha.toLocaleDateString();
        const horaActual = fecha.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        });

        const tipoVenta = (venta.tipoVenta || "").toUpperCase();

        // Filtrar productos que van a comanda
        const productosComanda = venta.productos.filter(
            p => p.comanda === true || p.comanda === "true"
        );

        if (productosComanda.length === 0) return;

        // Agrupar productos
        const agrupados = productosComanda.reduce((acc, prod) => {
            const key = prod.nombreProducto;
            if (!acc[key]) {
                acc[key] = { ...prod, cantidad: 0 };
            }
            acc[key].cantidad++;
            return acc;
        }, {});

        const productosAgrupados = Object.values(agrupados);

        let content = "";

        // ENCABEZADO
        content += "  LA CLANDESTINA\n";
        content += "  === COMANDA ===\n";
        content += "\n";
        content += `Pedido: ${venta.idVenta}\n`;
        content += `${fechaActual} - ${horaActual}\n`;
        content += "-------------------------\n";

        // TIPO DE VENTA
        content += `Tipo venta:\n`;
        content += `${tipoVenta}\n`;

        // DETALLE SEGÚN TIPO
        if (tipoVenta === "CONSUMO_LOCAL") {
            content += `${venta.detalleEntrega}\n`;
        } else if (tipoVenta === "ENVIO_DOMICILIO") {
            content += `Dirección:\n`;
            content += `${venta.detalleEntrega}\n`;
        } else {
            content += `Retira:\n`;
            content += `${venta.detalleEntrega}\n`;
        }

        if (venta.numMesa) {
            content += `Mesa: ${venta.numMesa}\n`;
        }

        content += "-------------------------\n";

        // PRODUCTOS
        productosAgrupados.forEach(p => {
            content += `${p.cantidad} x ${p.nombreProducto}\n`;
        });

        content += "-------------------------\n";
        // Convertir a formato térmico sin \n
        const finalContent = simulateLineBreaks(content);

        // Enviar al backend
        await fetch("http://localhost:3001/print", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ content: finalContent })
        });

        // Marcar comanda como impresa
        await imprimioComanda(venta.idVenta);
        onVentaActualizada(venta.idVenta);
    };

    return (
        venta && venta.productos.some(p => p.comanda === true || p.comanda === "true") ? (
            <button className="btn btn-success fw-bold" onClick={handlePrint}>
                Imprimir comanda
            </button>
        ) : null
    );
}
