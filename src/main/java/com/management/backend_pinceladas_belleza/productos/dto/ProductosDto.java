package com.management.backend_pinceladas_belleza.productos.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductosDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private String beneficios;
    private BigDecimal precio;
    private Integer cantidadStock;
    private Long categoriaId;
    private Long proveedorId;
    private String urlDrive;
}
