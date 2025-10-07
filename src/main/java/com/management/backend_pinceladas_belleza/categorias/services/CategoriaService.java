package com.management.backend_pinceladas_belleza.categorias.services;

import com.management.backend_pinceladas_belleza.categorias.dto.CategoriaDto;
import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.interfaces.ICategoria;
import com.management.backend_pinceladas_belleza.categorias.repository.CategoriaRepository;
import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService implements ICategoria {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", categoriaId));
    }

    @Override
    public Categoria insertarCategoria(CategoriaDto categoriaDto) {
        if (categoriaDto.getNombreCategoria() == null || categoriaDto.getNombreCategoria().trim().isEmpty()) {
            throw new BadRequestException("El nombre de la categoría es requerido");
        }
        
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(categoriaDto.getNombreCategoria());
        categoria.setEstado(categoriaDto.getEstado());
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizarCategoria(Categoria categoria) {
        if (categoria.getId() == null) {
            throw new BadRequestException("El ID de la categoría es requerido para actualizar");
        }
        
        Categoria optionalCategoria = obtenerCategoriaId(categoria.getId());
        optionalCategoria.setNombreCategoria(categoria.getNombreCategoria());
        optionalCategoria.setEstado(categoria.getEstado());
        return categoriaRepository.save(optionalCategoria);

    }

    @Override
    public String eliminarCategoria(Long id) {
        Categoria categoriaDB = obtenerCategoriaId(id);
        categoriaRepository.deleteById(id);
        return "Categoría eliminada exitosamente";
    }
}
