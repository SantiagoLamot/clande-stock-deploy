import axios from './axios'; // usa el interceptor

export const getMesas = async () => {
    const response = await axios.get('/mesas');
    return response.data;
};

export const ActualizarMesa = async (id, dto) => {
    const response = await axios.put('/mesas/'+id, dto);
    return response.data;
};

export const insertarMesa = async (dto) => {
    const response = await axios.post('/mesas', dto);
    return response.data;
};

