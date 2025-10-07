package com.management.backend_pinceladas_belleza.proveedores.controller;

import com.management.backend_pinceladas_belleza.proveedores.dto.ProveedorDto;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import com.management.backend_pinceladas_belleza.proveedores.interfaces.IProveedores;
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
@RequestMapping("proveedor")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "Gestión de proveedores")
@SecurityRequirement(name = "bearerAuth")
public class ProveedorController {
    private final IProveedores proveedoresImp;

    @Operation(summary = "Obtener todos los proveedores", description = "Retorna una lista de todos los proveedores disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proveedor.class)))
    })
    @GetMapping()
    public ResponseEntity<List<Proveedor>> findAll() {
        return ResponseEntity.ok(proveedoresImp.getProveedor());
    }

    @Operation(summary = "Obtener proveedor por ID", description = "Retorna un proveedor específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> findById(
            @Parameter(description = "ID del proveedor a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(proveedoresImp.getProveedorById(id));
    }

    @Operation(summary = "Crear nuevo proveedor", description = "Crea un nuevo proveedor en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Proveedor> create(
            @Parameter(description = "Datos del proveedor a crear", required = true) @RequestBody ProveedorDto proveedor) {
        return new ResponseEntity<>(proveedoresImp.createProveedor(proveedor), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar proveedor", description = "Actualiza la información de un proveedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PatchMapping("/update")
    public ResponseEntity<Proveedor> update(
            @Parameter(description = "Datos del proveedor a actualizar", required = true) @RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(proveedoresImp.updateProveedor(proveedor));
    }
}
