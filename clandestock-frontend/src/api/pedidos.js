import axios from "./axios";

const API_URL = "/api";

export const getVentasActivas = async () => {
  const response = await axios.get(`${API_URL}/venta/abiertas`);
  return response.data;
};

export const getVentasCerradas = async () => {
  const token = localStorage.getItem("token");
  const response = await axios.get("/api/venta/cerradas", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};
