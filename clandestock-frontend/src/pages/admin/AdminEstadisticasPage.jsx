import { motion } from "framer-motion";
import "../../styles/AdminEstadisticasPage.css";

export default function AdminEstadisticasPage() {
  return (
    <div className="estadisticas-container text-center">
      <motion.h2
        className="estadisticas-title text-warning"
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        Panel de Estadísticas
      </motion.h2>

      <motion.p
        className="estadisticas-text mt-3"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.8, duration: 1 }}
      >
        Aquí podrás visualizar métricas clave de productos, usuarios y ventas.
      </motion.p>

      <motion.div
        className="proximamente mt-5"
        initial={{ opacity: 0 }}
        animate={{ opacity: [0.2, 1, 0.2] }}
        transition={{ duration: 2, repeat: Infinity }}
      >
        <h4 className="text-light gothic-font">
          ✨ Próximamente estadísticas detalladas...
        </h4>
      </motion.div>
    </div>
  );
}
