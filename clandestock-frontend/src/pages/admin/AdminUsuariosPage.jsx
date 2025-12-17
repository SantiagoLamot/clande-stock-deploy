import { useEffect, useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";

export const AdminUsuariosPage = () => {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const [usuarios, setUsuarios] = useState([]);
  const [editingUser, setEditingUser] = useState(null);
  const [nombreUsuarioEdit, setNombreUsuarioEdit] = useState("");
  const [tipoUsuarioEdit, setTipoUsuarioEdit] = useState("");

  const token = localStorage.getItem("access_token");

  useEffect(() => {
    const fetchUsuarios = async () => {
      try {
        const response = await fetch("http://localhost:8080/usuario", {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (!response.ok) throw new Error("Error al obtener usuarios");
        const data = await response.json();
        setUsuarios(data);
      } catch (err) {
        console.error("Error cargando usuarios:", err);
      }
    };
    fetchUsuarios();
  }, [token]);

  const irARegistro = () => {
    navigate("/admin/registrar-moderador");
  };

  const handleEditar = (usuario) => {
    setEditingUser(usuario);
    setNombreUsuarioEdit(usuario.nombreUsuario);
    setTipoUsuarioEdit(usuario.tipoUsuario);
  };

  const handleGuardarCambios = async () => {
    if (!editingUser) return;
    try {
      const body = {
        nombreUsuario: nombreUsuarioEdit || null,
        tipoUsuario: tipoUsuarioEdit || null,
      };

      const response = await fetch(
        `http://localhost:8080/usuario/${editingUser.id}/editar`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(body),
        }
      );

      if (!response.ok) throw new Error("Error al editar usuario");

      setUsuarios((prev) =>
        prev.map((u) =>
          u.id === editingUser.id
            ? {
                ...u,
                nombreUsuario: nombreUsuarioEdit,
                tipoUsuario: tipoUsuarioEdit,
              }
            : u
        )
      );

      alert("Cambios guardados correctamente");
      setEditingUser(null);
    } catch (err) {
      console.error("Error guardando cambios:", err);
      alert("Error al guardar cambios");
    }
  };

  const handleToggleEstado = async (id, isActivo) => {
    try {
      const endpoint = isActivo
        ? `http://localhost:8080/usuario/${id}/baja`
        : `http://localhost:8080/usuario/${id}/alta`;

      const response = await fetch(endpoint, {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Error cambiando estado");

      setUsuarios((prev) =>
        prev.map((u) =>
          u.id === id ? { ...u, estado: isActivo ? false : true } : u
        )
      );
    } catch (err) {
      console.error("Error cambiando estado:", err);
    }
  };

  return (
    <div className="container-fluid mt-4">
      <div className="row align-items-start">
        {/* 📊 Columna izquierda: listado */}
        <div className="col-lg-8 col-md-7 ps-4">
          {" "}
          {/* margen/padding más amplio */}
          <h2 className="text-warning mb-4 gothic-font">Lista de usuarios</h2>
          <table className="table table-hover table-bordered shadow w-100">
            <thead className="table-warning">
              <tr>
                <th>Nombre Usuario</th>
                <th>Tipo Usuario</th>
                <th>Fecha Creación</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((usuario) => {
                const isActivo =
                  usuario.estado === "true" || usuario.estado === true;
                return (
                  <tr key={usuario.id}>
                    <td>{usuario.nombreUsuario}</td>
                    <td>{usuario.tipoUsuario}</td>
                    <td>{usuario.fechaCreacion}</td>
                    <td>
                      <span
                        className={`fw-bold ${
                          isActivo ? "text-success" : "text-danger"
                        }`}
                      >
                        {isActivo ? "Activo" : "Desactivo"}
                      </span>
                    </td>
                    <td>
                      <button
                        className="btn btn-primary btn-sm me-2"
                        onClick={() => handleEditar(usuario)}
                      >
                        Editar
                      </button>
                      {isActivo ? (
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleToggleEstado(usuario.id, true)}
                        >
                          Dar baja
                        </button>
                      ) : (
                        <button
                          className="btn btn-success btn-sm"
                          onClick={() => handleToggleEstado(usuario.id, false)}
                        >
                          Activar
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>

        {/* 📌 Columna derecha: acciones + panel de edición */}
        <div className="col-lg-4 col-md-5 mt-5">
          {" "}
          {/* bajamos más los paneles */}
          <div className="card shadow p-3 mb-4">
            <h5 className="text-warning gothic-font mb-3">Acciones rápidas</h5>
            <button className="btn btn-warning w-100" onClick={irARegistro}>
              Registrar Moderador
            </button>
          </div>
          {editingUser && (
            <div className="card shadow p-4 mt-5">
              {" "}
              {/* más espacio arriba */}
              <h5 className="text-warning gothic-font mb-3">
                Editar Usuario: {editingUser.nombreUsuario}
              </h5>
              <div className="mb-3">
                <label className="form-label text-warning">
                  Nombre de Usuario
                </label>
                <input
                  type="text"
                  className="form-control border-warning shadow-none"
                  value={nombreUsuarioEdit}
                  onChange={(e) => setNombreUsuarioEdit(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label className="form-label text-warning">
                  Tipo de Usuario
                </label>
                <select
                  className="form-select border-warning shadow-none"
                  value={tipoUsuarioEdit}
                  onChange={(e) => setTipoUsuarioEdit(e.target.value)}
                >
                  <option value="">-- Seleccionar --</option>
                  <option value="MODERADOR_TENEDOR_LIBRE">
                    Moderador Tenedor Libre
                  </option>
                  <option value="MODERADOR_TERMAS">Moderador Termas</option>
                  <option value="MODERADOR_HELADERIA">
                    Moderador Heladería
                  </option>
                </select>
              </div>
              <div className="text-center">
                <button
                  className="btn btn-success me-2"
                  onClick={handleGuardarCambios}
                >
                  Guardar cambios
                </button>
                <button
                  className="btn btn-secondary"
                  onClick={() => setEditingUser(null)}
                >
                  Cancelar
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
