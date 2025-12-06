const tiposModerador = [
  { label: "Tenedor Libre", value: "MODERADOR_TENEDOR_LIBRE" },
  { label: "Termas", value: "MODERADOR_TERMAS" },
  { label: "Heladería", value: "MODERADOR_HELADERIA" },
];

export default function ModeradorTipoSelector({ selected, onChange }) {
  return (
    <div className="mb-4">
      <label className="form-label text-warning">Tipo de Moderador</label>
      <div className="d-flex flex-column align-items-start">
        {tiposModerador.map((tipo) => (
          <div key={tipo.value} className="form-check mb-2">
            <input
              className="form-check-input"
              type="radio"
              name="tipoUsuario"
              value={tipo.value}
              checked={selected === tipo.value}
              onChange={(e) => onChange(e.target.value)}
            />
            <label className="form-check-label text-warning">
              Moderador de {tipo.label}
            </label>
          </div>
        ))}
      </div>
    </div>
  );
}
