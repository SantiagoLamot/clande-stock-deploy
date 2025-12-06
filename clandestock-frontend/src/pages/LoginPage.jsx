import { useState, useContext, useEffect } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Title from '../components/Title';
import PrimaryButton from '../components/PrimaryButtonSubmit';

export default function Login() {
    const [username, setUsername] = useState('');
    const [contrasena, setContrasena] = useState('');
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const { tipoUsuario } = await login(username, contrasena);
            if (tipoUsuario === 'ADMIN_GENERAL') {
                navigate('/admin');
            } else {
                navigate('/moderador');
            }
        } catch (err) {
            console.error("Error en Login.jsx:", err);
            alert('Credenciales inválidas');
        }
    };

    return (
        <div className="container mt-5">
            <form onSubmit={handleSubmit} className="bg-light login-box mx-5 p-4 rounded shadow">
                <Title text="Iniciar sesión" />

                <div className="mb-3">
                    <label className="form-label text-warning">Usuario</label>
                    <input
                        type="text"
                        className="form-control border-warning shadow-none"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="form-label text-warning">Contraseña</label>
                    <input
                        type="password"
                        className="form-control border-warning shadow-none"
                        value={contrasena}
                        onChange={(e) => setContrasena(e.target.value)}
                        required
                    />
                </div>

                <div className="text-center">
                    <PrimaryButton label="Ingresar" />
                </div>
            </form>
        </div>
    );
}