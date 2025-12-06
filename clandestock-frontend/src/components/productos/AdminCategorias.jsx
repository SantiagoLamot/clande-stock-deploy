import { useEffect, useState } from "react";
import PrimaryButtonSubmit from "../PrimaryButtonSubmit";
import { actualizarCategoria, getCategoriaPorLocal, guardarCategoria } from "../../api/categorias";

export default function AdminCategorias() {
    const [localId, setLocalId] = useState("1");
    const [categorias, setCategorias] = useState([]);
    const [formData, setFormData] = useState({ id: "", nombreCategoria: "" });

    const cargarCategorias = async () => {
        setCategorias([])
        const data = await getCategoriaPorLocal(localId);
        setCategorias(data);
    };

    useEffect(() => {
        cargarCategorias();
    }, [localId]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // armamos el DTO tal cual espera el backend
        const categoriaDTO = {
            id: formData.id || null,
            nombreCategoria: formData.nombreCategoria,
            localId: localId
        };

        try {
            if (formData.id) {
                // editar
                await actualizarCategoria(categoriaDTO);
            } else {
                // crear
                await guardarCategoria(categoriaDTO);
            }

            // limpiar form
            setFormData({ id: "", nombreCategoria: "" });
            // recargar listado
            cargarCategorias();
        } catch (err) {
            console.error("Error guardando categoría:", err);
        }
    };

    const handleEditar = (cat) => {
        setFormData({ id: cat.id, nombreCategoria: cat.nombre_categoria });
    };

    return (
        <div className="card p-3">
            <h5>Categorías por local</h5>

            {/* Selector de local */}
            <select className="form-select mb-3"
                value={localId}
                onChange={e => setLocalId(e.target.value)}
                disabled={!!formData.id}>
                <option value="1">Tenedor Libre</option>
                <option value="2">Termas</option>
                <option value="3">Heladería</option>
            </select>

            {/* Formulario */}
            <form onSubmit={handleSubmit} className="mb-3 d-flex gap-2 flex-wrap">
                <input
                    type="text"
                    name="nombreCategoria"
                    className="form-control"
                    placeholder="Nombre categoría"
                    value={formData.nombreCategoria}
                    onChange={handleChange}
                    required
                />
                <PrimaryButtonSubmit label={formData.id ? "Guardar cambios" : "Agregar categoría"} />
            </form>

            {/* Listado */}
            <ul className="list-group">
                {categorias.map(cat => (
                    <li key={cat.id} className="list-group-item d-flex justify-content-between align-items-center">
                        {cat.nombre_categoria}
                        <button className="btn btn-sm btn-warning" onClick={() => handleEditar(cat)}>Editar</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
