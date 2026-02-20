package com.management.backend_pinceladas_belleza.categorias.dto;

import lombok.Data;

@Data
public class CategoriaDto {
    private Long id;
    private String nombreCategoria;
    private Short estado;
}
