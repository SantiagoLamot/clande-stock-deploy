import axios from './axios'; // usa el interceptor

export const getAllVentasCerradas = async () => {
    const response = await axios.get('/venta/cerradas');
    return response.data;
};

export const VentasCerradasPorCaja = async (caja) => {
    const response = await axios.get('/venta/caja/'+caja);
    return response.data;
};
