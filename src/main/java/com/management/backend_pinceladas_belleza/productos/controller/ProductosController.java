package com.management.backend_pinceladas_belleza.productos.controller;

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

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos de belleza")
@SecurityRequirement(name = "bearerAuth")
public class ProductosController {
    private final IProductos productos;

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Productos.class)))
    })
    @GetMapping()
    public ResponseEntity<List<Productos>> getAll() {
        return ResponseEntity.ok(productos.getAll());
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Productos.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Productos> getById(
            @Parameter(description = "ID del producto a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(productos.getById(id));
    }

    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Productos.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Productos> create(
            @Parameter(description = "Datos del producto a crear", required = true) @RequestBody ProductosDto productosDto) {
        return new ResponseEntity<>(productos.createProducto(productosDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza la información de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Productos.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @PatchMapping("/update")
    public ResponseEntity<Productos> update(
            @Parameter(description = "Datos del producto a actualizar", required = true) @RequestBody Productos producto) {
        return ResponseEntity.ok(productos.updateProducto(producto));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID del producto a eliminar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(productos.deleteProducto(id));
    }
}
