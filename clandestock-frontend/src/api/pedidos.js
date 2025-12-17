import axios from "./axios";

const API_URL = "http://localhost:8080";

export const getVentasActivas = async () => {
  const response = await axios.get(`${API_URL}/venta/abiertas`);
  return response.data;
};

export const getVentasCerradas = async () => {
  const token = localStorage.getItem("token");
  const response = await axios.get("http://localhost:8080/venta/cerradas", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};
