import axios from './axios'; // usa el interceptor

export const getCategoriaPorLocal = async (local) => {
    const response = await axios.get('/categoria/local/'+local);
    return response.data;
};

export const guardarCategoria = async (categoria) => {
    const response = await axios.post('/categoria',categoria);
    return response.data;
};

export const actualizarCategoria = async (categoria) => {
    const response = await axios.put('/categoria',categoria);
    return response.data;
};
