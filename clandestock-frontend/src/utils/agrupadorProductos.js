/**
 * Agrupa productos repetidos por idProducto, calcula cantidad y total.
 * Conserva todos los idProductoPorVenta en un array y busca el productoPrincipalId en stock.
 */
export const agruparProductos = (productosVenta, productosStock) => {
  return Object.values(
    productosVenta.reduce((acc, prod) => {
      const key = prod.idProducto; // usar idProducto como clave

      // buscar el producto en stock para obtener productoPrincipalId
      const prodStock = productosStock.find(
        (p) => p.id === prod.idProducto
      );

      if (!acc[key]) {
        acc[key] = {
          nombreProducto: prod.nombreProducto,
          precioProducto: prod.precioProducto,
          idProducto: prod.idProducto,
          idProductosPorVenta: [prod.idProductoPorVenta], // array con todos los idProductoPorVenta
          cantidad: 1,
          total: parseFloat(prod.precioProducto),
          stockDisponible: prodStock ? prodStock.stockDisponible : 0,
        };
      } else {
        acc[key].cantidad += 1;
        acc[key].total += parseFloat(prod.precioProducto);
        acc[key].idProductosPorVenta.push(prod.idProductoPorVenta);
      }

      return acc;
    }, {})
  );
};
