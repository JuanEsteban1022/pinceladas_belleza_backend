package com.management.backend_pinceladas_belleza.categorias.graphql;

import com.management.backend_pinceladas_belleza.categorias.dto.CategoriaDto;
import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoriasResolver {

    private final CategoriaService categoriaService;

    @QueryMapping
    public List<Categoria> categorias() {
        return categoriaService.obtenerCategorias();
    }

    @QueryMapping
    public Categoria categoriaById(@Argument Long id) {
        return categoriaService.obtenerCategoriaId(id);
    }

    @MutationMapping
    public Categoria createCategoria(@Argument CategoriaInput input) {
        CategoriaDto dto = new CategoriaDto();
        dto.setNombreCategoria(input.getNombreCategoria());
        dto.setEstado(input.getEstado());
        return categoriaService.insertarCategoria(dto);
    }

    @MutationMapping
    public Categoria updateCategoria(@Argument CategoriaUpdateInput input) {
        Categoria categoria = new Categoria();
        categoria.setId(input.getId());
        categoria.setNombreCategoria(input.getNombreCategoria());
        categoria.setEstado(input.getEstado());
        return categoriaService.actualizarCategoria(categoria);
    }

    @MutationMapping
    public Boolean deleteCategoria(@Argument Long id) {
        categoriaService.eliminarCategoria(id);
        return true;
    }

    // Input classes for GraphQL
    public static class CategoriaInput {
        private String nombreCategoria;
        private Short estado;

        public String getNombreCategoria() { return nombreCategoria; }
        public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
        
        public Short getEstado() { return estado; }
        public void setEstado(Short estado) { this.estado = estado; }
    }

    public static class CategoriaUpdateInput {
        private Long id;
        private String nombreCategoria;
        private Short estado;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNombreCategoria() { return nombreCategoria; }
        public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
        
        public Short getEstado() { return estado; }
        public void setEstado(Short estado) { this.estado = estado; }
    }
}
