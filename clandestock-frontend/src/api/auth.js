import axios from 'axios'

const API_URL = '/api/auth';

export const login = async (username, contrasena) => {
    const response = await axios.post(`${API_URL}/login`, {username, contrasena});
    return response.data;
}
