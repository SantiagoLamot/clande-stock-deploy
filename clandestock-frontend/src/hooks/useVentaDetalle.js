import { useEffect, useState } from "react";
import {
  getVentaById,
  agregarProductoAVenta,
  quitarProductoDeVenta,
  getCategoriasActivas,
  getProductosPorCategoria,
  getMetodosPagoActivos,
  insertarMetodoPago,
  cerrarVenta,
} from "../services/ventaService";
import { agruparProductos } from "../utils/agrupadorProductos";

export const useVentaDetalle = (idVenta) => {
  const [venta, setVenta] = useState(null);
  const [categorias, setCategorias] = useState([]);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState("todos");
  const [productos, setProductos] = useState([]);
  const [metodosPago, setMetodosPago] = useState([]);
  const [metodoSeleccionado, setMetodoSeleccionado] = useState("");
  const [mensaje, setMensaje] = useState("");

  const token = localStorage.getItem("access_token");

  // 🔄 Cargar venta
  const cargarVenta = async () => {
    try {
      const data = await getVentaById(idVenta, token);
      setVenta(data);

      // 👇 si la venta ya tiene método de pago asignado, lo guardamos en el estado
      if (data.idMetodoPago) {
        setMetodoSeleccionado(data.idMetodoPago.toString());
      }
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al cargar venta");
    }
  };
  const refreshVenta = async (idVenta) => {
    const data = await getVentaById(idVenta, token);
    setVenta(data);
  };

  // 🔄 Cargar categorías activas
  const cargarCategorias = async () => {
    try {
      const data = await getCategoriasActivas(token);
      const normalizadas = data
        .filter((c) => c.activo)
        .map((c) => ({
          id: c.id,
          nombreCategoria: c.nombre_categoria,
          localID: c.local_id,
        }));
      setCategorias(normalizadas);
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al cargar categorías");
    }
  };

  // 🔄 Cargar productos
  const cargarProductos = async () => {
    try {
      const data = await getProductosPorCategoria(categoriaSeleccionada, token);
      setProductos(data);
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al cargar productos");
    }
  };

  // 🔄 Cargar métodos de pago activos
  const cargarMetodosPago = async () => {
    try {
      const data = await getMetodosPagoActivos(token);
      // normalizamos para que siempre tengas camelCase y boolean
      const normalizados = data.map((m) => ({
        ...m,
        estadoBool:
          String(m.estado).trim().toLowerCase() === "true" ||
          String(m.estado).trim().toLowerCase() === "activo",
      }));
      setMetodosPago(normalizados);
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al cargar métodos de pago");
    }
  };

  // ➕ Agregar producto
  const handleAgregarProducto = async (prod) => {
    try {
      const idProd = prod.idProducto ?? prod.id;
      await agregarProductoAVenta(idVenta, idProd, token);
      await cargarVenta();
      await cargarProductos();
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al agregar producto");
    }
  };

  // ➖ Quitar producto
  const handleQuitarProducto = async (prod) => {
    try {
      await quitarProductoDeVenta(
        idVenta,
        prod.idProductosPorVenta[prod.idProductosPorVenta.length - 1],
        token
      );
      await cargarVenta();
      await cargarProductos();
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al quitar producto");
    }
  };

  // 💳 Seleccionar método de pago
  const handleSeleccionarMetodoPago = async (idMetodoPago) => {
    try {
      setMetodoSeleccionado(idMetodoPago);
      const ventaActualizada = await insertarMetodoPago(
        idVenta,
        idMetodoPago,
        token
      );
      setVenta(ventaActualizada);
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al insertar método de pago");
    }
  };

  // 🔴 Cerrar venta
  const handleCerrarVenta = async (onBack) => {
    try {
      const confirmar = window.confirm("¿Seguro que quiere cerrar esta venta?");
      if (!confirmar) return;
      await cerrarVenta(idVenta, token);
      onBack(); // volver al listado de pedidos
    } catch (err) {
      console.error(err);
      setMensaje("❌ Error al cerrar venta");
    }
  };

  useEffect(() => {
    cargarVenta();
    cargarCategorias();
    cargarMetodosPago();
  }, [idVenta]);

  useEffect(() => {
    cargarProductos();
  }, [categoriaSeleccionada]);

  const productosAgrupados = venta
    ? agruparProductos(venta.productos, productos)
    : [];

  return {
    venta,
    refreshVenta,
    categorias,
    categoriaSeleccionada,
    setCategoriaSeleccionada,
    productos,
    productosAgrupados,
    handleAgregarProducto,
    handleQuitarProducto,
    metodosPago,
    metodoSeleccionado,
    handleSeleccionarMetodoPago,
    handleCerrarVenta,
    mensaje,
  };
};
