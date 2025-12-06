import { useNavigate } from "react-router-dom";
import ModeradorForm from "../../components/registrarModerador/ModeradorForm";
import {registrarModerador} from "../../services/moderadorService"
export default function RegistrarModeradorPage() {
  const navigate = useNavigate();

  const handleRegistro = async (data) => {
    try {
      await registrarModerador(data);
      alert("Moderador registrado con éxito");
      navigate("/admin");
    } catch (err) {
      console.error("Error al registrar moderador:", err);
      alert("Hubo un error al registrar el moderador");
    }
  };

  return (
    <div className="container mt-5">
      <ModeradorForm onSubmit={handleRegistro} />
    </div>
  );
}
