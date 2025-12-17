import axios from "axios";

export const enviarReporte = async (descripcion, usuarioEmisor) => {
  const token = localStorage.getItem("access_token");
  if (!token) throw new Error("Token no encontrado");

  const response = await axios.post(
    "http://localhost:8080/reporte",
    {
      descripcion,
      usuarioEmisor, // nombre de usuario, no id
    },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const obtenerHistorialReportes = async () => {
  const token = localStorage.getItem("access_token");
  if (!token) throw new Error("Token no encontrado");

  const response = await axios.get("http://localhost:8080/reporte/historial", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};

export const obtenerTodosReportes = async () => {
  const token = localStorage.getItem("access_token");
  if (!token) throw new Error("Token no encontrado");

  const response = await axios.get("http://localhost:8080/reporte/historial", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};

export const checkReporte = async (id) => {
  const token = localStorage.getItem("access_token");
  if (!token) throw new Error("Token no encontrado");

  const response = await axios.put(
    `http://localhost:8080/reporte/${id}`,
    {},
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};
