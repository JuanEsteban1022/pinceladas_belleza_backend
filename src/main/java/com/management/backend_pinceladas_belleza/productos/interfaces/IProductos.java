package com.management.backend_pinceladas_belleza.productos.interfaces;

import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;

import java.util.List;

public interface IProductos {
    public List<Productos> getAll();

    Productos getById(Long id);

    Productos createProducto(ProductosDto producto);

    Productos updateProducto(ProductosDto producto);

    String deleteProducto(Long id);
}
