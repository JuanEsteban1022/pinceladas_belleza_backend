package com.management.backend_pinceladas_belleza.productos.services;

import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import com.management.backend_pinceladas_belleza.productos.interfaces.IProductos;
import com.management.backend_pinceladas_belleza.productos.repository.ProductosRepository;
import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.repository.CategoriaRepository;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import com.management.backend_pinceladas_belleza.proveedores.repository.ProveedoresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductos {
    private final ProductosRepository productosRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedoresRepository proveedoresRepository;

    @Override
    public List<Productos> getAll() {
        return productosRepository.findAll();
    }

    @Override
    public Productos getById(Long id) {
        return productosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    }

    @Override
    public Productos createProducto(ProductosDto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del producto es requerido");
        }
        
        if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
            throw new BadRequestException("El precio del producto debe ser mayor a 0");
        }

        Categoria categoria = categoriaRepository.findById(producto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", producto.getCategoriaId()));

        Proveedor proveedor = proveedoresRepository.findById(producto.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", producto.getProveedorId()));

        Productos productos = Productos.builder()
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .cantidadStock(producto.getCantidadStock())
                .categoria(categoria)
                .proveedor(proveedor)
                .fechaCreacion(LocalDate.now())
                .urlDrive(producto.getUrlDrive())
                .build();

        return productosRepository.save(productos);
    }

    @Override
    public Productos updateProducto(Productos producto) {
        if (producto.getId() == null) {
            throw new BadRequestException("El ID del producto es requerido para actualizar");
        }
        
        Productos productoEntity = getById(producto.getId());
        productoEntity.setNombre(producto.getNombre());
        productoEntity.setPrecio(producto.getPrecio());
        productoEntity.setDescripcion(producto.getDescripcion());
        
        // Buscar y asignar categoría y proveedor por ID
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", producto.getCategoria().getId()));
            productoEntity.setCategoria(categoria);
        }
        
        if (producto.getProveedor() != null && producto.getProveedor().getId() != null) {
            Proveedor proveedor = proveedoresRepository.findById(producto.getProveedor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", producto.getProveedor().getId()));
            productoEntity.setProveedor(proveedor);
        }
        
        productoEntity.setFechaCreacion(LocalDate.now());
        productoEntity.setUrlDrive(producto.getUrlDrive());
        return productosRepository.save(productoEntity);
    }

    @Override
    public String deleteProducto(Long id) {
        Productos productoEntity = getById(id);
        productosRepository.deleteById(id);
        return "Producto eliminado exitosamente";
    }
}
