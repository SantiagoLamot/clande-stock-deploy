import { useEffect, useState } from "react";
import { getDetalleCaja, getEstadoCaja, obtenerProductosVendidos } from "../../api/caja";

export default function TicketCierreCaja() {
    const [apertura, setApertura] = useState(null);
    const [detalle, setDetalle] = useState(null);
    const [productos, setProductos] = useState([]);


    const cargarDatos = async ()=>{
        const detalle = await getDetalleCaja();
        setDetalle(detalle[0]);

        const cajas = await getEstadoCaja();
        const abierta =
        Array.isArray(cajas) && cajas.length > 0 ? cajas[0] : null;
        setApertura(abierta);

        const prodxVenta = await obtenerProductosVendidos()
        setProductos(prodxVenta);
    }
    useEffect( () => {
        cargarDatos();
    }, [])

    // Simula salto de línea sin crear párrafos
    const simulateLineBreaks = (text, width = 25) => {
        const lines = text.split("\n");
        return lines.map(line => line.padEnd(width) + "\r").join("");
    };

    const handlePrint = async () => {
        if (!apertura || !detalle) return;

        const WIDTH = 25;

        // Local según ID
        const getLocalName = (id) => {
            switch (id) {
                case "1": return "TENEDOR LIBRE";
                case "2": return "TERMAS";
                case "3": return "HELADERIA";
                default: return "LOCAL";
            }
        };

        // Formateo de fechas
        const formatDate = (iso) => {
            if (!iso) return "-";
            const d = new Date(iso);
            const dia = String(d.getDate()).padStart(2, "0");
            const mes = String(d.getMonth() + 1).padStart(2, "0");
            const año = d.getFullYear();
            const hora = String(d.getHours()).padStart(2, "0");
            const min = String(d.getMinutes()).padStart(2, "0");
            return `${dia}/${mes}/${año} ${hora}:${min}`;
        };

        const fechaApertura = formatDate(apertura.fechaApertura);
        const fechaCierre = formatDate(new Date()); // cierre actual

        const nombreLocal = getLocalName(apertura.idLocal);
        const montoApertura = parseFloat(apertura.montoApertura).toFixed(2);
        const total = parseFloat(detalle.totalGeneral).toFixed(2);

        const totalGeneral = (parseFloat(montoApertura) + parseFloat(total)).toFixed(2);
        let content = "";

        // ENCABEZADO
        content += "CIERRE DE CAJA\n";
        content += `${nombreLocal}\n`;
        content += "-------------------------\n";

        // DATOS DE APERTURA Y CIERRE
        content += `Apertura:\n`;
        content += `${fechaApertura}\n`;

        content += `Cierre:\n`;
        content += `${fechaCierre}\n`;

        content += "-------------------------\n";

        // TOTAL GENERAL
        content += `TOTAL GENERAL:\n`;
        content += `$${totalGeneral}\n`;

        content += "-------------------------\n";

        // MONTOS POR MÉTODO
        content += "DETALLE POR METODO\n";
        content += `Monto apertura:\n`;
        content += `$${montoApertura}\n`;

        detalle.detallePorMetodo.forEach(m => {
            const total = parseFloat(m.totalCobrado).toFixed(2);
            content += `${m.metodoPago}:\n`;
            content += `$${total}\n`;
        });

        content += "-------------------------\n";
        content += "PRODUCTOS VENDIDOS\n";

        productos.forEach(p => {
            content += `${p.cantidad} x ${p.nombreProducto}\n`;
        });

        content += "-------------------------\n";
        content += "CIERRE COMPLETADO\n";

        // Convertir a formato térmico sin \n
        const finalContent = simulateLineBreaks(content);

        // Enviar al backend
        await fetch("http://localhost:3001/print", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ content: finalContent })
        });
    };

    return (
        <button className="btn btn-dark fw-bold" onClick={handlePrint}>
            Imprimir cierre de caja
        </button>
    );
}
