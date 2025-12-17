import axios from "./axios"; // usa el interceptor

export const getMetodos = async () => {
  const response = await axios.get("/metodopago");
  return response.data;
};

export const ActualizarMetodoPago = async (id, dto) => {
  const response = await axios.put("/metodopago", dto);
  return response.data;
};

export const insertarMetodoPago = async (dto) => {
  const response = await axios.post("/metodopago", dto);
  return response.data;
};

export const darAltaMetodoPago = async (id) => {
  const response = await axios.put(`/metodopago/alta/${id}`);
  return response.data;
};

export const darBajaMetodoPago = async (id) => {
  const response = await axios.put(`/metodopago/baja/${id}`);
  return response.data;
};
