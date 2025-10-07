package com.management.backend_pinceladas_belleza.categorias.interfaces;

import com.management.backend_pinceladas_belleza.categorias.dto.CategoriaDto;
import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;

import java.util.List;

public interface ICategoria {
    List<Categoria> obtenerCategorias();

    Categoria obtenerCategoriaId(Long categoriaId);

    Categoria insertarCategoria(CategoriaDto categoria);

    Categoria actualizarCategoria(Categoria categoria);

    String eliminarCategoria(Long id);
}
