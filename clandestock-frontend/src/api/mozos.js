import axios from './axios'; // usa el interceptor

export const getMozos = async () => {
    const response = await axios.get('/mozos');
    return response.data;
};

export const ActualizarMozo = async (id, dto) => {
    const response = await axios.put('/mozos/'+id, dto);
    return response.data;
};

export const insertarMozo = async (dto) => {
    const response = await axios.post('/mozos/nuevo', dto);
    return response.data;
};


