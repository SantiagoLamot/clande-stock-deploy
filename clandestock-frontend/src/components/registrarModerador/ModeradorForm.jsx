import { useState } from "react";
import Title from "../Title";
import PrimaryButton from "../PrimaryButtonSubmit";
import InputField from "./InputField";
import ModeradorTipoSelector from "./ModeradorTipoSelector";

export default function ModeradorForm({ onSubmit }) {
  const [nombreUsuario, setNombreUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [confirmarContrasena, setConfirmarContrasena] = useState("");
  const [tipoSeleccionado, setTipoSeleccionado] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (contrasena !== confirmarContrasena) {
      alert("Las contraseñas no coinciden");
      return;
    }
    if (!tipoSeleccionado) {
      alert("Selecciona un tipo de moderador");
      return;
    }

    onSubmit({
      nombreUsuario,
      contrasena,
      tipoUsuario: tipoSeleccionado,
    });
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-light registro-box mx-5 p-4 rounded shadow"
    >
      <Title text="Registrar Moderador" />

      <InputField
        label="Nombre de Usuario"
        type="text"
        value={nombreUsuario}
        onChange={setNombreUsuario}
      />

      <InputField
        label="Contraseña"
        type="password"
        value={contrasena}
        onChange={setContrasena}
      />

      <InputField
        label="Confirmar Contraseña"
        type="password"
        value={confirmarContrasena}
        onChange={setConfirmarContrasena}
      />

      <ModeradorTipoSelector
        selected={tipoSeleccionado}
        onChange={setTipoSeleccionado}
      />

      <div className="text-center">
        <PrimaryButton label="Registrar" />
      </div>
    </form>
  );
}
