package com.management.backend_pinceladas_belleza.ventas.controller;

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
@RequestMapping("/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de ventas y reportes")
public class VentasController {

    @Operation(summary = "Obtener ventas mensuales", description = "Retorna estadísticas de ventas mensuales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas mensuales obtenidas exitosamente",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/mensuales")
    public ResponseEntity<List<Map<String, Object>>> getVentasMensuales() {
        // Temporarily return mock data
        return ResponseEntity.ok(List.of(
            Map.of("mes", "Enero", "ventas", 15000.0, "cantidad", 150),
            Map.of("mes", "Febrero", "ventas", 18000.0, "cantidad", 180),
            Map.of("mes", "Marzo", "ventas", 22000.0, "cantidad", 220),
            Map.of("mes", "Abril", "ventas", 19000.0, "cantidad", 190),
            Map.of("mes", "Mayo", "ventas", 25000.0, "cantidad", 250),
            Map.of("mes", "Junio", "ventas", 28000.0, "cantidad", 280)
        ));
    }

    @Operation(summary = "Obtener todas las ventas", description = "Retorna una lista de todas las ventas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        // Temporarily return mock data
        return ResponseEntity.ok(List.of(
            Map.of("id", 1, "producto", "Producto 1", "cantidad", 2, "total", 200.0, "fecha", "2024-01-01"),
            Map.of("id", 2, "producto", "Producto 2", "cantidad", 1, "total", 150.0, "fecha", "2024-01-02")
        ));
    }
}
