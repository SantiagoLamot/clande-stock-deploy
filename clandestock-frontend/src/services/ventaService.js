const BASE_URL = "/api";

export const getVentaById = async (idVenta, token) => {
  const res = await fetch(`${BASE_URL}/venta/${idVenta}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Error al obtener venta");
  return await res.json();
};

export const agregarProductoAVenta = async (idVenta, idProducto, token) => {
  const body = { idVenta, idProducto };
  const res = await fetch(`${BASE_URL}/venta/agregar`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error("Error al agregar producto");
};

export const quitarProductoDeVenta = async (
  idVenta,
  idProductoPorVenta,
  token
) => {
  const body = { idVenta, idProducto: idProductoPorVenta };
  const res = await fetch(`${BASE_URL}/venta/quitar`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error("Error al quitar producto");
};

export const getCategorias = async (token) => {
  const res = await fetch(`${BASE_URL}/categoria/todas`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Error al obtener categorías");
  return await res.json();
};

export const getProductosPorCategoria = async (categoriaID, token) => {
  const url =
    categoriaID === "todos"
      ? `${BASE_URL}/productos/stock`
      : `${BASE_URL}/productos/stock?categoriaID=${categoriaID}`;

  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Error al obtener productos");
  return await res.json();
};

export const getMetodosPago = async (token) => {
  const res = await fetch(`${BASE_URL}/metodopago`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Error al obtener métodos de pago");
  return await res.json();
};

export const insertarMetodoPago = async (idVenta, idMetodoPago, token) => {
  const res = await fetch(
    `${BASE_URL}/venta/insertarMetodoPago/${idMetodoPago}/${idVenta}`,
    {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  if (!res.ok) throw new Error("Error al insertar método de pago");
  return await res.json(); // devuelve la venta actualizada con precioTotalConMetodoDePago
};

export const cerrarVenta = async (idVenta, token) => {
  const res = await fetch(`${BASE_URL}/venta/cerrar/${idVenta}`, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Error al cerrar venta");
  return await res.json();
};
