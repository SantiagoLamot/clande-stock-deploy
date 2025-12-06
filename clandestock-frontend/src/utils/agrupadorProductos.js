/**
 * Agrupa productos repetidos por nombre, calcula cantidad y total.
 * Además, conserva el idProductoPorVenta (para quitar) y busca el productoPrincipalId (para agregar).
 *
 * @param {Array} productosVenta - Array de productos desde la venta (cada uno con idProductoPorVenta).
 * @param {Array} productosStock - Array de productos disponibles con productoPrincipalId.
 * @returns {Array} - Array de productos agrupados con cantidad, total, y ambos IDs.
 */
export const agruparProductos = (productosVenta, productosStock) => {
  return Object.values(
    productosVenta.reduce((acc, prod) => {
      const key = prod.nombreProducto;

      // buscar el producto en stock para obtener productoPrincipalId
      const prodStock = productosStock.find(
        (p) => p.nombreProducto === prod.nombreProducto
      );

      if (!acc[key]) {
        acc[key] = {
          nombreProducto: prod.nombreProducto,
          precioProducto: prod.precioProducto,
          productoPrincipalId: prodStock ? prodStock.productoPrincipalId : null,
          idProductoPorVenta: prod.idProductoPorVenta,
          cantidad: 1,
          total: parseFloat(prod.precioProducto),
        };
      } else {
        acc[key].cantidad += 1;
        acc[key].total += parseFloat(prod.precioProducto);
        acc[key].idProductoPorVenta = prod.idProductoPorVenta; // mantener último para quitar
      }

      return acc;
    }, {})
  );
};
