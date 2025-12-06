import { useState } from "react";
import { abrirCaja } from "../../api/caja";
import Title from "../../components/Title"

export const AbrirCaja = ({ onSuccess }) => {
    const [montoApertura, setMontoApertura] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const dto = { montoApertura: montoApertura };
            const cajaResponse = await abrirCaja(dto);
            if (typeof onSuccess === "function") {
                onSuccess(cajaResponse);
            }
            alert("Caja abierta correctamente");
        } catch (err) {
            console.error("Error abriendo caja:", err);
            alert("Error al abrir la caja");
        }
    };

    return (
        <div className="card flex-grow-1 d-flex flex-column">
            <Title text={"Abrir Caja"} />
            <div className="card-body">
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Monto de apertura</label>
                        <input
                            type="number"
                            className="form-control"
                            value={montoApertura}
                            onChange={(e) => setMontoApertura(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-success">
                        Abrir caja
                    </button>
                </form>
            </div>
        </div>
    );
}