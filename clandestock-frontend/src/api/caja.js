import axios from './axios'; // usa el interceptor

export const getEstadoCaja = async () => {
    const response = await axios.get('/caja/abierta');
    return response.data;
};

export const getDetalleCaja = async () => {
    const response = await axios.get('/caja/detalle');
    return response.data;
};

export const abrirCaja = async (dto) => {
    const response = await axios.post('/caja/abrir',dto);
    return response.data;
};
export const cerrarCaja = async () => {
    const response = await axios.post('/caja/cerrar');
    return response.data;
};
export const getAllCajasCerradas = async () => {
    const response = await axios.get('/caja/cerradas');
    return response.data;
};
export const getAllCajasAbiertas = async () => {
    const response = await axios.get('/caja/detalle');
    return response.data;
};
export const obtenerProductosVendidos = async () => {
    const response = await axios.get('/productoxventa/caja');
    return response.data;
};
