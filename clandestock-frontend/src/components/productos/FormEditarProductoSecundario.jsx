import { useState, useEffect } from "react";
import PrimaryButtonSubmit from "../PrimaryButtonSubmit";
import { getCategoriaPorLocal } from "../../api/categorias";
import { putProductosSecundario } from "../../api/productoSecundario";

export default function FormEditarProductoSecundario({ producto, onClose }) {
    const [formData, setFormData] = useState({
        id: "",
        nombre_producto: "",
        stock: "",
        estado: false,
        local: "",
        alertaStockBajo: "",
        precio: "",
        idCategoria: ""
    });

    
    useEffect(() => {
        const cargarDatos = async () => {
            if (!producto) return;

            try {
                setFormData({
                    id: producto.id,
                    nombre_producto: producto.nombre_producto || "",
                    stock: producto.stock || "",
                    estado: producto.estado === "true",
                    local: producto.local || "",
                    alertaStockBajo: producto.alertaStockBajo || "",
                    precio: producto.precio || ""
                });
            } catch (err) {
                console.error("Error cargando datos del producto:", err);
            }
        };

        cargarDatos();
    }, [producto]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await putProductosSecundario({
                ...formData,
                estado: formData.estado ? "true" : "false"
            });
            onClose();
        } catch (err) {
            console.error("Error actualizando producto secundario:", err);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-3 text-dark">
            <h5>Editar producto secundario</h5>

            {/* Nombre */}
            <div className="mb-3">
                <label className="form-label">Nombre producto</label>
                <input
                    type="text"
                    name="nombre_producto"
                    className="form-control"
                    value={formData.nombre_producto}
                    onChange={handleChange}
                    required
                />
            </div>

            {/* Stock */}
            <div className="mb-3">
                <label className="form-label">Stock</label>
                <input
                    type="number"
                    name="stock"
                    className="form-control"
                    value={formData.stock}
                    onChange={handleChange}
                />
            </div>

            {/* Alerta stock bajo */}
            <div className="mb-3">
                <label className="form-label">Alerta stock bajo</label>
                <input
                    type="text"
                    name="alertaStockBajo"
                    className="form-control"
                    value={formData.alertaStockBajo}
                    onChange={handleChange}
                />
            </div>

            {/* Estado toggle */}
            <div className="mb-3">
                <label className="form-label">Estado</label>
                <div>
                    <button
                        type="button"
                        className={`btn btn-sm ${formData.estado ? "btn-success" : "btn-outline-secondary"}`}
                        onClick={() => setFormData({ ...formData, estado: !formData.estado })}
                    >
                        {formData.estado ? "Activo" : "Inactivo"}
                    </button>
                </div>
            </div>

            {/* Botones */}
            <div className="d-flex justify-content-between">
                <PrimaryButtonSubmit label="Guardar cambios" />
                <button type="button" className="btn btn-secondary" onClick={onClose}>
                    Cancelar
                </button>
            </div>
        </form>
    );
}
