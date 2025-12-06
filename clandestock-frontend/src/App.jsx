import { useEffect, useState } from 'react';
import Navbar from './components/Navbar';
import AppRouter from './routes/AppRouter';
import { getAlertasStockPrimario, getAlertasStockSecundario } from './api/alertasStock';
import { obtenerTodosReportes } from './api/reporte';

function App() {
  const [cantidadAlertas, setCantidadAlertas] = useState(0);
  const [cantidadReportesNoLeidos, setCantidadReportesNoLeidos] = useState(0);

  const cargarReportes = async () => {
    try {
      const data = await obtenerTodosReportes();
      // contar solo los "No leído"
      const noLeidos = data.filter(r => r.estado === "No leído").length;
      setCantidadReportesNoLeidos(noLeidos);
    } catch (error) {
      console.error("Error cargando reportes:", error.response?.data || error.message);
    }
  };
  const cargarAlertas = async () => {
    try {
      const primarios = await getAlertasStockPrimario();
      const secundarios = await getAlertasStockSecundario();

      const primariosMapped = primarios.map(p => ({
        ...p,
        tipo: "principal",
        alerta: p.sinStock === "true" || p.stockDisponible === "0" ? "sin stock" : "poco stock"
      }));

      const secundariosMapped = secundarios.map(p => ({
        ...p,
        tipo: "secundario",
        alerta: p.sinStock === "true" || p.stockDisponible === "0" ? "sin stock" : "poco stock"
      }));

      setCantidadAlertas(primariosMapped.length + secundariosMapped.length);
    } catch (err) {
      console.error("Error cargando alertas:", err);
    }
  };

  useEffect(() => {
    cargarAlertas();
    cargarReportes();
  }, []);

  return (
    <>
      <Navbar
        cantidadAlertas={cantidadAlertas}
        cantidadReportesNoLeidos={cantidadReportesNoLeidos} />
      <AppRouter setCantidadAlertas={setCantidadAlertas}
        cantidadAlertas={cantidadAlertas}
        setCantidadReportesNoLeidos={setCantidadReportesNoLeidos} />
    </>
  );
}

export default App;