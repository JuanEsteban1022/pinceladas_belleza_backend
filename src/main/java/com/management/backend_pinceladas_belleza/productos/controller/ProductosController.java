package com.management.backend_pinceladas_belleza.productos.controller;

import com.management.backend_pinceladas_belleza.common.dto.PaginatedResponse;
import com.management.backend_pinceladas_belleza.common.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import com.management.backend_pinceladas_belleza.productos.interfaces.IProductos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos de belleza")
public class ProductosController {
    private final IProductos productos;

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista paginada de productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponse.class)))
    })
    @GetMapping()
    public ResponseEntity<PaginatedResponse<ProductosDto>> getAll(
            @Parameter(description = "Número de página (0-based)", example = "0") 
            @RequestParam(defaultValue = "0") @Min(0) int page,
            
            @Parameter(description = "Tamaño de página", example = "20") 
            @RequestParam(defaultValue = "20") @Min(1) int size,
            
            @Parameter(description = "Término de búsqueda") 
            @RequestParam(required = false) String search) {
        
        // Por ahora, implementar paginación básica con lista existente
        List<ProductosDto> allProductos = productos.getAll();
        
        // Calcular paginación manualmente (temporal hasta que el servicio soporte paginación)
        int start = page * size;
        int end = Math.min(start + size, allProductos.size());
        
        if (start >= allProductos.size()) {
            // Si la página está fuera de rango, retornar vacío
            PaginatedResponse<ProductosDto> response = PaginatedResponse.of(
                List.of(), allProductos.size(), page + 1, size
            );
            return ResponseEntity.ok(response);
        }
        
        List<ProductosDto> pageProductos = allProductos.subList(start, end);
        
        PaginatedResponse<ProductosDto> response = PaginatedResponse.of(
            pageProductos, allProductos.size(), page + 1, size
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductosDto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductosDto> getById(
            @Parameter(description = "ID del producto a buscar", required = true) @PathVariable @NotNull Long id) {
        
        Productos entity = Optional.ofNullable(productos.getById(id))
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        
        ProductosDto dto = convertToDto(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductosDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create")
    public ResponseEntity<ProductosDto> create(
            @Parameter(description = "Datos del producto a crear", required = true) @RequestBody ProductosDto productosDto) {
        // Convertir entidad resultante a DTO para respuesta consistente
        Productos entity = productos.createProducto(productosDto);
        ProductosDto dto = convertToDto(entity);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza la información de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductosDto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update")
    public ResponseEntity<ProductosDto> update(
            @Parameter(description = "Datos del producto a actualizar", required = true) @RequestBody ProductosDto producto) {
        // Convertir entidad resultante a DTO para respuesta consistente
        Productos entity = productos.updateProducto(producto);
        ProductosDto dto = convertToDto(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto a eliminar", required = true) @PathVariable @NotNull Long id) {
        productos.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte una entidad Productos a ProductosDto para respuestas consistentes
     */
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
