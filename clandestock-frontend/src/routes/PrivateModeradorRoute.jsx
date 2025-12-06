import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

export default function PrivateModeradorRoute({ children }) {
    const { user, loading, logout } = useContext(AuthContext);

    if (loading) {
        return <div>Cargando...</div>;
    }
    if (!user) {
        logout();
        return <Navigate to="/login" />
    };
    if (user.tipoUsuario === 'ADMIN_GENERAL'){
        logout();
        return <Navigate to="/login" />;
    }

    return children;
}