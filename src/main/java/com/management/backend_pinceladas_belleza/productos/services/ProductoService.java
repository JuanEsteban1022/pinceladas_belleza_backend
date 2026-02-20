package com.management.backend_pinceladas_belleza.productos.services;

import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.repository.CategoriaRepository;
import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import com.management.backend_pinceladas_belleza.productos.interfaces.IProductos;
import com.management.backend_pinceladas_belleza.productos.repository.ProductosRepository;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import com.management.backend_pinceladas_belleza.proveedores.repository.ProveedoresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductos {
    private final ProductosRepository productosRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedoresRepository proveedoresRepository;

    @Override
    public List<ProductosDto> getAll() {
        return productosRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Productos> getAllEntities() {
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
    public Productos updateProducto(ProductosDto producto) {
        if (producto.getId() == null) {
            throw new BadRequestException("El ID del producto es requerido para actualizar");
        }

        Productos productoEntity = getById(producto.getId());
        productoEntity.setNombre(producto.getNombre());
        productoEntity.setPrecio(producto.getPrecio());
        productoEntity.setDescripcion(producto.getDescripcion());
        productoEntity.setCantidadStock(producto.getCantidadStock());

        // Buscar y asignar categoría y proveedor por ID
        if (producto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", producto.getCategoriaId()));
            productoEntity.setCategoria(categoria);
        }

        if (producto.getProveedorId() != null) {
            Proveedor proveedor = proveedoresRepository.findById(producto.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", producto.getProveedorId()));
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

    private ProductosDto convertToDto(Productos entity) {
        if (entity == null) {
            return null;
        }

        return ProductosDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .precio(entity.getPrecio())
                .cantidadStock(entity.getCantidadStock())
                .categoriaId(entity.getCategoria() != null ? entity.getCategoria().getId() : null)
                .proveedorId(entity.getProveedor() != null ? entity.getProveedor().getId() : null)
                .urlDrive(entity.getUrlDrive())
                .build();
    }
}
