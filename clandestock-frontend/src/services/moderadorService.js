import axios from '../api/axios'; // usa el interceptor

export const registrarModerador = async (data) => {
    const response = await axios.post('/api/moderador/register', data);
    return response.data;
};