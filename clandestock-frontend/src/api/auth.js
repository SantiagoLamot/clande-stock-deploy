import axios from 'axios'

const API_URL = '/api';

export const login = async (username, contrasena) => {
    const response = await axios.post(`${API_URL}/auth/login`, {username, contrasena});
    return response.data;
}
