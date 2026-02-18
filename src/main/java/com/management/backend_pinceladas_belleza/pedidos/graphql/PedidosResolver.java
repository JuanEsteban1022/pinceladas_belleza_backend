package com.management.backend_pinceladas_belleza.pedidos.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PedidosResolver {

    @QueryMapping
    public List<Map<String, Object>> pedidos() {
        // Return mock data similar to the controller
        return List.of(
            Map.of("id", 1, "cliente", "Cliente 1", "estado", "pendiente", "total", 100.0),
            Map.of("id", 2, "cliente", "Cliente 2", "estado", "completado", "total", 200.0)
        );
    }

    @QueryMapping
    public Map<String, Object> pedidoById(@Argument Long id) {
        // Return mock data for specific pedido
        return Map.of(
            "id", id,
            "cliente", "Cliente " + id,
            "estado", "pendiente",
            "total", 100.0 * id,
            "fecha", "2024-01-01"
        );
    }

    @MutationMapping
    public Map<String, Object> createPedido(@Argument PedidoInput input) {
        // Create a new pedido with mock data
        Map<String, Object> nuevoPedido = Map.of(
            "id", 3,
            "cliente", input.getCliente(),
            "estado", input.getEstado(),
            "total", input.getTotal(),
            "fecha", input.getFecha()
        );
        return nuevoPedido;
    }

    // Input class for GraphQL
    public static class PedidoInput {
        private String cliente;
        private String estado;
        private Double total;
        private String fecha;

        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        
        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }
        
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
    }
}
