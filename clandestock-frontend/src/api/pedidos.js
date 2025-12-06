import axios from "./axios";


export const getVentasActivas = async () => {
  const response = await axios.get(`/venta/abiertas`);
  return response.data;
};

export const getVentasCerradas = async () => {
  const token = localStorage.getItem("token");
  const response = await axios.get("/venta/cerradas", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};
