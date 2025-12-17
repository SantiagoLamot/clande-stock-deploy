import { useEffect, useState } from "react";
import PrimaryButtonSubmit from "../PrimaryButtonSubmit";
import {
  actualizarCategoria,
  getCategoriaPorLocal,
  guardarCategoria,
  darBajaCategoria,
  darAltaCategoria,
} from "../../api/categorias";

export default function AdminCategorias() {
  const [localId, setLocalId] = useState("1");
  const [categorias, setCategorias] = useState([]);
  const [formData, setFormData] = useState({ id: "", nombreCategoria: "" });

  const cargarCategorias = async () => {
    setCategorias([]);
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
    const categoriaDTO = {
      id: formData.id || null,
      nombreCategoria: formData.nombreCategoria,
      localId: localId,
    };

    try {
      if (formData.id) {
        await actualizarCategoria(categoriaDTO);
      } else {
        await guardarCategoria(categoriaDTO);
      }
      setFormData({ id: "", nombreCategoria: "" });
      cargarCategorias();
    } catch (err) {
      console.error("Error guardando categoría:", err);
    }
  };

  const handleEditar = (cat) => {
    setFormData({ id: cat.id, nombreCategoria: cat.nombre_categoria });
  };

  const handleToggleEstado = async (cat) => {
    try {
      if (cat.activo) {
        await darBajaCategoria(cat.id);
      } else {
        await darAltaCategoria(cat.id);
      }
      cargarCategorias();
    } catch (err) {
      console.error("Error cambiando estado de categoría:", err);
    }
  };

  return (
    <div className="card p-3">
      <h5>Categorías por local</h5>

      {/* Selector de local */}
      <select
        className="form-select mb-3"
        value={localId}
        onChange={(e) => setLocalId(e.target.value)}
        disabled={!!formData.id}
      >
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
        <PrimaryButtonSubmit
          label={formData.id ? "Guardar cambios" : "Agregar categoría"}
        />
      </form>

      {/* Listado */}
      <ul className="list-group">
        {categorias.map((cat) => (
          <li
            key={cat.id}
            className="list-group-item d-flex justify-content-between align-items-center"
          >
            <span>
              {cat.nombre_categoria}{" "}
              {!cat.activo && (
                <span className="badge bg-secondary">Inactiva</span>
              )}
            </span>
            <div className="d-flex gap-2">
              <button
                className="btn btn-sm btn-warning"
                onClick={() => handleEditar(cat)}
              >
                Editar
              </button>
              {cat.activo ? (
                <button
                  className="btn btn-sm btn-danger"
                  onClick={() => handleToggleEstado(cat)}
                >
                  Dar de baja
                </button>
              ) : (
                <button
                  className="btn btn-sm btn-success"
                  onClick={() => handleToggleEstado(cat)}
                >
                  Dar de alta
                </button>
              )}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
