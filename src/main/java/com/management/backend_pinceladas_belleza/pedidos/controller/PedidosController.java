package com.management.backend_pinceladas_belleza.pedidos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión de pedidos")
public class PedidosController {

    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        // Temporarily return empty list until proper implementation
        return ResponseEntity.ok(List.of(
            Map.of("id", 1, "cliente", "Cliente 1", "estado", "pendiente", "total", 100.0),
            Map.of("id", 2, "cliente", "Cliente 2", "estado", "completado", "total", 200.0)
        ));
    }

    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        // Temporarily return mock data
        return ResponseEntity.ok(Map.of(
            "id", id,
            "cliente", "Cliente " + id,
            "estado", "pendiente",
            "total", 100.0 * id,
            "fecha", "2024-01-01"
        ));
    }
}
