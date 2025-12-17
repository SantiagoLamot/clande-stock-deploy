export default function TicketCobro({ venta, metodoSeleccionado }) {

    const simulateLineBreaks = (text, width = 25) => {
        const lines = text.split("\n");
        return lines.map(line => line.padEnd(width) + "\r").join("");
    };

    const handlePrint = async () => {
        if (!venta || venta.productos.length === 0) return;

        const WIDTH = 25;
        const EMPTY = " ".repeat(WIDTH); // ← línea en blanco visual

        // corta el texto para que no se mezcle
        const wrap = (text = "") => {
            const out = [];
            for (let i = 0; i < text.length; i += WIDTH) {
                out.push(text.substring(i, i + WIDTH));
            }
            return out;
        };

        // agrega una línea + línea en blanco para simular salto
        const add = (text = "") => {
            wrap(text).forEach(l => {
                content += l;        // línea
                content += EMPTY;    // “salto de línea” sin \n
            });
        };

        const fecha = new Date();
        const fechaActual = fecha.toLocaleDateString();
        const horaActual = fecha.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        });

        const tipoVenta = (venta.tipoVenta || "").toUpperCase();

        // Agrupar productos
        const agrupados = venta.productos.reduce((acc, prod) => {
            const key = `${prod.nombreProducto}-${prod.precioProducto}`;
            if (!acc[key]) {
                acc[key] = { ...prod, cantidad: 0 };
            }
            acc[key].cantidad++;
            return acc;
        }, {});

        const productosAgrupados = Object.values(agrupados);
        let content = "";

        // Generás el ticket como siempre
        content += "  LA CLANDESTINA\n";
        content += "     TAPALQUÉ\n";
        content += "\n";
        content += `Pedido: ${venta.idVenta}\n`;
        content += `${fechaActual} - ${horaActual}\n`;
        content += `Tipo de venta: \n`;
        content += `${tipoVenta}\n`;
        
        if (venta.detalleEntrega) content += `Detalle:\n`;
        if (venta.detalleEntrega) content += `${venta.detalleEntrega}\n`;
        if (venta.numMesa) content += `Mesa: ${venta.numMesa}\n`;
        
        content += "-------------------------\n";
        
        productosAgrupados.forEach(p => {
            const precio = parseFloat(p.precioProducto).toFixed(2);
            content += `${p.cantidad} x ${p.nombreProducto}\n`;
            content += `    $${precio}\n`;
        });

        content += "-------------------------\n";
        
        const subtotal = parseFloat(venta.precioTotal).toFixed(2);
        const total = parseFloat(venta.precioTotalConMetodoDePago).toFixed(2);
        const diferencia = (total - subtotal).toFixed(2);
        
        if (diferencia != 0) content += `Subtotal: $${subtotal}\n`;
        if (diferencia > 0) content += `Recargo: +$${diferencia}\n`;
        else if (diferencia < 0) content += `Descuento: -$${Math.abs(diferencia)}\n`;
        content += `Total: $${total}\n`;
        content += "\n";
        content += "*** Este ticket no es    válido como factura ***\n";
        content += "¡Gracias por su compra!\n";

        // Y antes de enviar, lo convertís:
        const finalContent = simulateLineBreaks(content);

        await fetch("http://localhost:3001/print", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ content: finalContent })
        });
    };

    return metodoSeleccionado ? (
        <button className="btn btn-warning fw-bold" onClick={handlePrint}>
            Imprimir ticket
        </button>
    ) : null;
}
