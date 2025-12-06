export default function InputField({ label, type, value, onChange }) {
  return (
    <div className="mb-3">
      <label className="form-label text-warning">{label}</label>
      <input
        type={type}
        className="form-control border-warning shadow-none"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required
      />
    </div>
  );
}
