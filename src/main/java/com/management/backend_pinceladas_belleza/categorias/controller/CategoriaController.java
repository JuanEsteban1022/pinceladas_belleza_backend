package com.management.backend_pinceladas_belleza.categorias.controller;

import com.management.backend_pinceladas_belleza.common.dto.ErrorResponse;
import com.management.backend_pinceladas_belleza.common.dto.PaginatedResponse;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final ICategoria categoriaImp;

    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista paginada de categorías")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponse.class)))
    })
    @GetMapping()
    public ResponseEntity<PaginatedResponse<CategoriaDto>> obtenerCategorias(
            @Parameter(description = "Número de página (0-based)", example = "0") 
            @RequestParam(defaultValue = "0") @Min(0) int page,
            
            @Parameter(description = "Tamaño de página", example = "20") 
            @RequestParam(defaultValue = "20") @Min(1) int size,
            
            @Parameter(description = "Término de búsqueda") 
            @RequestParam(required = false) String search) {
        
        // Por ahora, implementar paginación básica con lista existente
        List<Categoria> allCategorias = categoriaImp.obtenerCategorias();
        
        // Filtrar por búsqueda si se proporciona
        if (search != null && !search.trim().isEmpty()) {
            allCategorias = allCategorias.stream()
                .filter(cat -> cat.getNombreCategoria() != null &&
                        cat.getNombreCategoria().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        // Calcular paginación manualmente
        int start = page * size;
        int end = Math.min(start + size, allCategorias.size());
        
        if (start >= allCategorias.size()) {
            // Si la página está fuera de rango, retornar vacío
            PaginatedResponse<CategoriaDto> response = PaginatedResponse.of(
                List.of(), allCategorias.size(), page + 1, size
            );
            return ResponseEntity.ok(response);
        }
        
        List<Categoria> pageCategorias = allCategorias.subList(start, end);
        List<CategoriaDto> pageDtos = pageCategorias.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        PaginatedResponse<CategoriaDto> response = PaginatedResponse.of(
            pageDtos, allCategorias.size(), page + 1, size
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener categoría por ID", description = "Retorna una categoría específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaDto.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtenerCategoria(
            @Parameter(description = "ID de la categoría a buscar", required = true) @PathVariable @NotNull Long id) {
        
        Categoria entity = Optional.ofNullable(categoriaImp.obtenerCategoriaId(id))
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        CategoriaDto dto = convertToDto(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<CategoriaDto> createCategoria(
            @Parameter(description = "Datos de la categoría a crear", required = true) @RequestBody CategoriaDto categoria) {
        // Convertir entidad resultante a DTO para respuesta consistente
        Categoria entity = categoriaImp.insertarCategoria(categoria);
        CategoriaDto dto = convertToDto(entity);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar", required = true) @PathVariable @NotNull Long id) {
        categoriaImp.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza la información de una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaDto.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/update")
    public ResponseEntity<CategoriaDto> updateCategoria(
            @Parameter(description = "Datos de la categoría a actualizar", required = true) @RequestBody CategoriaDto categoria) {
        // Convertir entidad resultante a DTO para respuesta consistente
        Categoria entity = categoriaImp.actualizarCategoria(convertToEntity(categoria));
        CategoriaDto dto = convertToDto(entity);
        return ResponseEntity.ok(dto);
    }

    /**
     * Convierte una entidad Categoria a CategoriaDto para respuestas consistentes
     */
    private CategoriaDto convertToDto(Categoria entity) {
        if (entity == null) {
            return null;
        }
        
        CategoriaDto dto = new CategoriaDto();
        dto.setId(entity.getId());
        dto.setNombreCategoria(entity.getNombreCategoria());
        dto.setEstado(entity.getEstado());
        return dto;
    }

    /**
     * Convierte un CategoriaDto a entidad Categoria para operaciones de servicio
     */
    private Categoria convertToEntity(CategoriaDto dto) {
        if (dto == null) {
            return null;
        }
        
        Categoria entity = new Categoria();
        entity.setId(dto.getId());
        entity.setNombreCategoria(dto.getNombreCategoria());
        entity.setEstado(dto.getEstado());
        return entity;
    }
}
