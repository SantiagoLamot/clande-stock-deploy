import { useContext } from "react";
import { AuthContext } from "./../context/AuthContext";
import "./../styles/adminPage.css";
import { motion } from "framer-motion";
import { Button } from "react-bootstrap";

export const AdminPage = () => {
  const { user } = useContext(AuthContext);

  return (
    <div className="admin-container d-flex flex-column align-items-center justify-content-center text-center">
      {/* Fondo animado */}
      <div className="background-glow"></div>

      {/* Animación de bienvenida */}
      <motion.h1
        className="admin-title text-warning"
        initial={{ opacity: 0, y: -40 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        Bienvenido al panel de <span className="brand">ClandeStock</span>,{" "}
        {user?.username}
      </motion.h1>

      <motion.p
        className="admin-subtitle text-light mt-3"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1, duration: 1 }}
      >
        Controlá el flujo. Dominá el inventario.
        <span className="text-danger"> ClandeStock</span> está en tus manos.
      </motion.p>
    </div>
  );
};
