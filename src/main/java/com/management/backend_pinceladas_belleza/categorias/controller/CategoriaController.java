package com.management.backend_pinceladas_belleza.categorias.controller;

import com.management.backend_pinceladas_belleza.categorias.dto.CategoriaDto;
import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.interfaces.ICategoria;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final ICategoria categoriaImp;

    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista de todas las categorías disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class)))
    })
    @GetMapping()
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok(categoriaImp.obtenerCategorias());
    }

    @Operation(summary = "Obtener categoría por ID", description = "Retorna una categoría específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(
            @Parameter(description = "ID de la categoría a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(categoriaImp.obtenerCategoriaId(id));
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Categoria> createCategoria(
            @Parameter(description = "Datos de la categoría a crear", required = true) @RequestBody CategoriaDto categoria) {
        return new ResponseEntity<>(categoriaImp.insertarCategoria(categoria), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(categoriaImp.eliminarCategoria(id));
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza la información de una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PatchMapping("update")
    public ResponseEntity<Categoria> updateCategoria(
            @Parameter(description = "Datos de la categoría a actualizar", required = true) @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaImp.actualizarCategoria(categoria));
    }
}
