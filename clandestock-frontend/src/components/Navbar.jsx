import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import AdminNavbar from "./navbars/AdminNavbar";
import LoginNavbar from "./navbars/LoginNavbar";
import ModeradorNavbar from "./navbars/ModeradorNavbar";

export default function Navbar({ cantidadAlertas, cantidadReportesNoLeidos}) {
  const { user } = useContext(AuthContext);

  if (!user) return <LoginNavbar />;
  if (user.tipoUsuario === "ADMIN_GENERAL") return <AdminNavbar cantidadAlertas={cantidadAlertas} cantidadReportesNoLeidos={cantidadReportesNoLeidos}/>;
  if (user.tipoUsuario !== "ADMIN_GENERAL") return <ModeradorNavbar />;
}
