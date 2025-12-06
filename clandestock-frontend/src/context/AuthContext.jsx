import { createContext, useEffect, useState } from 'react';
import { login as loginService } from '../api/auth';
import { jwtDecode } from 'jwt-decode';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('access_token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setUser({ username: decoded.sub, tipoUsuario: decoded.tipoUsuario });
            } catch (err) {
                console.error("Error decodificando token:", err);
                setUser(null);
            }
        }
        setLoading(false);
    }, []);

    const login = async (username, contrasena) => {
        const data = await loginService(username, contrasena);
        localStorage.setItem('access_token', data.access_token);
        localStorage.setItem('refresh_token', data.refresh_token);

        const decoded = jwtDecode(data.access_token);
        const tipoUsuario = decoded.tipoUsuario;

        setUser({ username, tipoUsuario });
        return { tipoUsuario };
    };

    const logout = () => {
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        setUser(null);
    };

    const isAuthenticated = !!user;

    return (
        <AuthContext.Provider value={{ user, login, logout, isAuthenticated, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
