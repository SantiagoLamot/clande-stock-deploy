import axios from './axios'; // usa el interceptor

export const guardarProductoSecundario = async (productoDTO) => {
    const response = await axios.post('/productos/secundario', productoDTO);
    return response.data;
};

export const getAllProductosSecundarios = async () => {
    const response = await axios.get('/productos/secundario/todos');
    return response.data;
};
export const getProductosSecundarioPorId = async (id) => {
    const response = await axios.get('/productos/secundario/'+id);
    return response.data;
};

export const putProductosSecundario = async (producto) => {
    const response = await axios.put('/productos/secundario/actualizar', producto);
    return response.data;
};

export const incrementarStockSecundario = async (id) => {
    const response = await axios.post('/productos/secundario/incrementar/stock/'+id);
    return response.data;
};