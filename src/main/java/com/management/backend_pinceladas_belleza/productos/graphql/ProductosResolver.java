package com.management.backend_pinceladas_belleza.productos.graphql;

import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import com.management.backend_pinceladas_belleza.productos.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductosResolver {

    private final ProductoService productoService;

    @QueryMapping
    public List<Productos> productos() {
        return productoService.getAllEntities();
    }

    @QueryMapping
    public Productos productoById(@Argument Long id) {
        return productoService.getById(id);
    }

    @MutationMapping
    public Productos createProducto(@Argument ProductoInput input) {
        ProductosDto dto = ProductosDto.builder()
                .nombre(input.getNombre())
                .descripcion(input.getDescripcion())
                .precio(input.getPrecio())
                .cantidadStock(input.getCantidadStock())
                .categoriaId(input.getCategoriaId())
                .proveedorId(input.getProveedorId())
                .urlDrive(input.getUrlDrive())
                .build();
        return productoService.createProducto(dto);
    }

    @MutationMapping
    public Productos updateProducto(@Argument ProductoUpdateInput input) {
        ProductosDto dto = ProductosDto.builder()
                .id(input.getId())
                .nombre(input.getNombre())
                .descripcion(input.getDescripcion())
                .precio(input.getPrecio())
                .cantidadStock(input.getCantidadStock())
                .categoriaId(input.getCategoriaId())
                .proveedorId(input.getProveedorId())
                .urlDrive(input.getUrlDrive())
                .build();
        return productoService.updateProducto(dto);
    }

    @MutationMapping
    public Boolean deleteProducto(@Argument Long id) {
        productoService.deleteProducto(id);
        return true;
    }

    // Input classes for GraphQL
    public static class ProductoInput {
        private String nombre;
        private String descripcion;
        private Double precio;
        private Integer cantidadStock;
        private Long categoriaId;
        private Long proveedorId;
        private String urlDrive;

        // Getters and setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        
        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }
        
        public Integer getCantidadStock() { return cantidadStock; }
        public void setCantidadStock(Integer cantidadStock) { this.cantidadStock = cantidadStock; }
        
        public Long getCategoriaId() { return categoriaId; }
        public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
        
        public Long getProveedorId() { return proveedorId; }
        public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
        
        public String getUrlDrive() { return urlDrive; }
        public void setUrlDrive(String urlDrive) { this.urlDrive = urlDrive; }
    }

    public static class ProductoUpdateInput {
        private Long id;
        private String nombre;
        private String descripcion;
        private Double precio;
        private Integer cantidadStock;
        private Long categoriaId;
        private Long proveedorId;
        private String urlDrive;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        
        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }
        
        public Integer getCantidadStock() { return cantidadStock; }
        public void setCantidadStock(Integer cantidadStock) { this.cantidadStock = cantidadStock; }
        
        public Long getCategoriaId() { return categoriaId; }
        public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
        
        public Long getProveedorId() { return proveedorId; }
        public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
        
        public String getUrlDrive() { return urlDrive; }
        public void setUrlDrive(String urlDrive) { this.urlDrive = urlDrive; }
    }
}
