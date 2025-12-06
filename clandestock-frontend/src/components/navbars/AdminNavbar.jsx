import { useNavigate, Link } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import logo from "../../assets/lc-logo2.png";

export default function AdminNavbar({ cantidadAlertas = 0, cantidadReportesNoLeidos = 0 }) {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };
  const cerrarMenu = () => {
    const navbar = document.getElementById("adminNavbar");
    if (navbar && navbar.classList.contains("show")) {
      const collapse = new window.bootstrap.Collapse(navbar, { toggle: false });
      collapse.hide();
    }
  };
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light px-3 shadow-sm">
      <div className="container-fluid">
        <Link to="/admin" className="navbar-brand" onClick={cerrarMenu}>
          <img src={logo} alt="Logo" height="40" />
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#adminNavbar"
          aria-controls="adminNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="adminNavbar">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link className="nav-link" to="/admin/productos" onClick={cerrarMenu}>
                <span className="d-inline-block position-relative">
                  Administrar productos
                  {cantidadAlertas > 0 && (
                    <span
                      className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                      style={{ fontSize: "0.75rem", minWidth: "1.2rem" }}
                    >
                      {cantidadAlertas}
                    </span>
                  )}
                </span>
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/admin/usuarios" onClick={cerrarMenu}>
                Administrar usuarios
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/admin/ventas" onClick={cerrarMenu}>
                Administrar ventas
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/admin/reportes" onClick={cerrarMenu}>
                <span className="d-inline-block position-relative">
                  Reportes
                  {cantidadReportesNoLeidos > 0 && (
                    <span
                      className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                      style={{ fontSize: "0.75rem", minWidth: "1.2rem" }}
                    >
                      {cantidadReportesNoLeidos}
                    </span>
                  )}
                </span>
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/admin/estadisticas" onClick={cerrarMenu}>
                Estadísticas
              </Link>
            </li>
          </ul>
          <button className="btn btn-outline-danger" onClick={handleLogout}>
            Cerrar sesión
          </button>
        </div>
      </div>
    </nav >
  );
}
