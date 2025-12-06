import axios from "./axios";

export const getAlertasStockSecundario = async () => {
    const response = await axios.get("/productos/secundario/alertas");
    return response.data;
};


export const getAlertasStockPrimario = async () => {
    const response = await axios.get("/productos/principal/alertas");
    return response.data;
};

export const desactivarAlertaSecundario = async (id) => {
    const response = await axios.post(`/productos/secundario/desactivarAlerta/${id}`);
    return response.data;
};
export const desactivarAlertaPrimario = async (id) => {
    const response = await axios.post(`/producto/principal/desactivarAlerta/${id}`);
    return response.data;
};
export const actualizarStockPrimario = async (dto) => {
    const response = await axios.post("/producto/principal/stock", dto);
    return response.data;
};
export const actualizarStockSecundario = async (dto) => {
    const response = await axios.post("/productos/secundario/stock", dto);
    return response.data;
};

export const insertarAlertaPrimario = async (id, dto) => {
    const response = await axios.post(`/producto/principal/alertar/${id}`,dto);
    return response.data;
};
export const insertarAlertaSecundario = async (id, dto) => {
    const response = await axios.post(`/productos/secundario/alertar/${id}`,dto);
    return response.data;
};
