import { useState, useEffect } from "react";
import PrimaryButtonSubmit from "../PrimaryButtonSubmit";
import { getCategoriaPorLocal } from "../../api/categorias";

export default function FormNuevoProductoPrincipal({ onSubmit, estado, setEstado }) {
    const initialState = {
        idLocal: "",
        nombre: "",
        precio: "",
        aletarStockBajo: "",
        estado: "",
        stock: "",
        idCategoria: "",
        comanda: "true"
    };

    const [formData, setFormData] = useState(initialState);
    const [categorias, setCategorias] = useState([]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        setEstado(null)
    }, [])

    useEffect(() => {
        const cargarCategorias = async () => {
            if (!formData.idLocal) return;
            try {
                const data = await getCategoriaPorLocal(formData.idLocal);
                setCategorias(data);
            } catch (err) {
                console.error("Error cargando categorías:", err);
            }
        };
        cargarCategorias();
    }, [formData.idLocal]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!formData.idLocal) {
            alert("Debe seleccionar un local");
            return;
        }
        if (!formData.idCategoria) {
            alert("Debe seleccionar una categoría");
            return;
        }
        onSubmit(formData);
        setFormData(initialState);
        setCategorias([]);
    };

    return (
        <form onSubmit={handleSubmit} className="p-3 text-dark">
            {estado && (
                <div
                    className={`alert mt-3 ${estado.tipo === "success" ? "alert-success" : "alert-danger"
                        }`}
                >
                    {estado.mensaje}
                </div>
            )}
            <div className="mb-3">
                <label className="form-label">Nombre producto</label>
                <input
                    type="text"
                    name="nombre"
                    className="form-control"
                    value={formData.nombre}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="mb-3">
                <label className="form-label">Precio</label>
                <input
                    type="number"
                    name="precio"
                    className="form-control"
                    value={formData.precio}
                    onChange={handleChange}
                    required
                />
            </div>

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

            <div className="mb-3">
                <label className="form-label">Local</label>
                <div>
                    <div className="form-check">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="idLocal"
                            value="1"
                            checked={formData.idLocal === "1"}
                            onChange={handleChange}
                            required
                        />
                        <label>Tenedor Libre</label>
                    </div>
                    <div className="form-check">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="idLocal"
                            value="2"
                            checked={formData.idLocal === "2"}
                            onChange={handleChange}
                            required
                        />
                        <label>Termas</label>
                    </div>
                    <div className="form-check">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="idLocal"
                            value="3"
                            checked={formData.idLocal === "3"}
                            onChange={handleChange}
                            required
                        />
                        <label>Heladería</label>
                    </div>
                </div>
            </div>
            <div className="mb-3">
                <label className="form-label">Comanda</label>
                <div>
                    <div className="form-check">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="comanda"
                            value="true"
                            checked={formData.comanda === "true"}
                            onChange={handleChange}
                            required
                        />
                        <label>Imprimir</label>
                    </div>
                    <div className="form-check">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="comanda"
                            value="false"
                            checked={formData.comanda === "false"}
                            onChange={handleChange}
                            required
                        />
                        <label>No imprimir</label>
                    </div>
                </div>
            </div>

            {/* 👇 categorías dinámicas */}
            {categorias.length > 0 && (
                <div className="mb-3">
                    <label className="form-label">Categoría</label>
                    <select
                        name="idCategoria"
                        className="form-select"
                        value={formData.idCategoria}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Seleccione una categoría</option>
                        {categorias.map((c) => (
                            <option key={c.id} value={c.id}>
                                {c.nombre_categoria}
                            </option>
                        ))}
                    </select>
                </div>
            )}

            <div className="mb-3">
                <label className="form-label">Alerta stock bajo</label>
                <input
                    type="text"
                    name="aletarStockBajo"
                    className="form-control"
                    value={formData.aletarStockBajo}
                    onChange={handleChange}
                />
            </div>
            <PrimaryButtonSubmit label={"Crear producto"} />
        </form>
    );
}
