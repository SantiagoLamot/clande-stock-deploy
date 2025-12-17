import axios from 'axios'

const API_URL = 'http://localhost:8080/auth';

export const login = async (username, contrasena) => {
    const response = await axios.post(`${API_URL}/login`, {username, contrasena});
    return response.data;
}
