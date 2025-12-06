import axios from './axios'; // usa el interceptor

export const getRelacionesPorProductoPrincipal = async (id) => {
    const response = await axios.get('/productos/relacion/' + id);
    return response.data;
};
export const deleteRelacionSecundaria = async (id) => {
    const response = await axios.delete('/productos/relacion/' + id);
    return response.data;
};
export const guardarRelacionProducto = async (dto) => {
    const response = await axios.post("/productos/relacion", dto);
    return response.data;
};