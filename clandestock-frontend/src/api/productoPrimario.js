import axios from './axios'; // usa el interceptor

export const getAllProductosPrimarios = async () => {
    const response = await axios.get('/producto/principal/todos');
    return response.data;
};
export const getProductosPrimariosPorId = async (id) => {
    const response = await axios.get('/producto/principal/'+id);
    return response.data;
};

export const guardarProductoPrincipal = async (producto) => {
    const response = await axios.post('/producto/principal', producto);
    return response.data;
};

export const putProductosPrimario = async (producto) => {
    const response = await axios.put('/producto/principal', producto);
    return response.data;
};
export const getProductosStok = async () => {
    const response = await axios.get('/productos/stock');
    return response.data;
};

export const incrementarStockPrimario = async (id) => {
    const response = await axios.post('/producto/principal/incrementar/stock/'+id);
    return response.data;
};
