import { Routes, Route, Navigate } from "react-router-dom";
import Login from "../pages/LoginPage";
import { ModeradorPage } from "../pages/moderador/ModeradorPage";
import { AdminPage } from "../pages/AdminPage";
import PrivateModeradorRoute from "./PrivateModeradorRoute";
import PrivateAdminRoute from "./PrivateAdminRoute";
import RegistrarModeradorPage from "../pages/admin/RegistrarModeradorPage";
import { AdminProductosPage } from "./../pages/admin/AdminProductosPage";
import AdminReportesPage from "../pages/admin/AdminReportesPage";
import { AdminVentasPage } from "../pages/admin/AdminVentasPage";
import { AdminUsuariosPage } from "./../pages/admin/AdminUsuariosPage";
import AdminEstadisticasPage from "../pages/admin/AdminEstadisticasPage";

export default function AppRouter({ setCantidadAlertas , cantidadAlertas, setCantidadReportesNoLeidos}) {
  return (
    <Routes>
      {/* ACCESO GENERAL */}
      <Route path="/login" element={<Login />} />

      {/* ACCESO ADMINISTRADOR */}
      <Route
        path="/admin"
        element={
          <PrivateAdminRoute>
            <AdminPage />
          </PrivateAdminRoute>
        }
      />
      <Route
        path="/admin/registrar-moderador"
        element={
          <PrivateAdminRoute>
            <RegistrarModeradorPage />
          </PrivateAdminRoute>
        }
      />
      <Route
        path="/admin/productos"
        element={
          <PrivateAdminRoute>
            <AdminProductosPage setCantidadAlertas={setCantidadAlertas} cantidadAlertas={cantidadAlertas}  />
          </PrivateAdminRoute>
        }
      />
      <Route
        path="/admin/usuarios"
        element={
          <PrivateAdminRoute>
            <AdminUsuariosPage />
          </PrivateAdminRoute>
        }
      />
      <Route
        path="/admin/ventas"
        element={
          <PrivateAdminRoute>
            <AdminVentasPage />
          </PrivateAdminRoute>
        }
      />
      <Route
        path="/admin/reportes"
        element={
          <PrivateAdminRoute>
            <AdminReportesPage setCantidadReportesNoLeidos={setCantidadReportesNoLeidos}/>
          </PrivateAdminRoute>
        }
      />
      <Route
        path="/admin/estadisticas"
        element={
          <PrivateAdminRoute>
            <AdminEstadisticasPage />
          </PrivateAdminRoute>
        }
      />

      {/* ACCESO MODERADOR */}
      <Route
        path="/moderador"
        element={
          <PrivateModeradorRoute>
            <ModeradorPage />
          </PrivateModeradorRoute>
        }
      />

      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
}
