package com.management.backend_pinceladas_belleza.ventas.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VentasResolver {

    @QueryMapping
    public List<Map<String, Object>> ventas() {
        // Return mock data similar to the controller
        return List.of(
            Map.of("id", 1, "producto", "Producto 1", "cantidad", 2, "total", 200.0, "fecha", "2024-01-01"),
            Map.of("id", 2, "producto", "Producto 2", "cantidad", 1, "total", 150.0, "fecha", "2024-01-02")
        );
    }

    @QueryMapping
    public Map<String, Object> ventaById(@Argument Long id) {
        // Return mock data for specific venta
        if (id.equals(1L)) {
            return Map.of("id", 1, "producto", "Producto 1", "cantidad", 2, "total", 200.0, "fecha", "2024-01-01");
        } else if (id.equals(2L)) {
            return Map.of("id", 2, "producto", "Producto 2", "cantidad", 1, "total", 150.0, "fecha", "2024-01-02");
        }
        return null;
    }

    @QueryMapping
    public List<Map<String, Object>> ventasMensuales() {
        // Return mock data similar to the controller
        return List.of(
            Map.of("mes", "Enero", "ventas", 15000.0, "cantidad", 150),
            Map.of("mes", "Febrero", "ventas", 18000.0, "cantidad", 180),
            Map.of("mes", "Marzo", "ventas", 22000.0, "cantidad", 220),
            Map.of("mes", "Abril", "ventas", 19000.0, "cantidad", 190),
            Map.of("mes", "Mayo", "ventas", 25000.0, "cantidad", 250),
            Map.of("mes", "Junio", "ventas", 28000.0, "cantidad", 280)
        );
    }

    @MutationMapping
    public Map<String, Object> createVenta(@Argument VentaInput input) {
        // Create a new venta with mock data
        Map<String, Object> nuevaVenta = Map.of(
            "id", 3, 
            "producto", input.getProducto(), 
            "cantidad", input.getCantidad(), 
            "total", input.getTotal(), 
            "fecha", input.getFecha()
        );
        return nuevaVenta;
    }

    // Input class for GraphQL
    public static class VentaInput {
        private String producto;
        private Integer cantidad;
        private Double total;
        private String fecha;

        public String getProducto() { return producto; }
        public void setProducto(String producto) { this.producto = producto; }
        
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        
        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }
        
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
    }
}
