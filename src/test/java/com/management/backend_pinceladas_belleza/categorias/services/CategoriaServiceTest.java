package com.management.backend_pinceladas_belleza.categorias.services;

import com.management.backend_pinceladas_belleza.categorias.dto.CategoriaDto;
import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.repository.CategoriaRepository;
import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - CategoriaService")
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDto categoriaDto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombreCategoria("Maquillaje");
        categoria.setEstado((short) 1);

        categoriaDto = new CategoriaDto();
        categoriaDto.setNombreCategoria("Maquillaje");
        categoriaDto.setEstado((short) 1);
    }

    @Test
    @DisplayName("Debe retornar lista de todas las categorías")
    void testObtenerCategorias_DebeRetornarListaDeCategorias() {
        // ARRANGE
        List<Categoria> categoriasEsperadas = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categoriasEsperadas);

        // ACT
        List<Categoria> resultado = categoriaService.obtenerCategorias();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Maquillaje", resultado.get(0).getNombreCategoria());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar categoría cuando existe el ID")
    void testObtenerCategoriaId_CuandoExiste_DebeRetornarCategoria() {
        // ARRANGE
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // ACT
        Categoria resultado = categoriaService.obtenerCategoriaId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Maquillaje", resultado.getNombreCategoria());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la categoría no existe")
    void testObtenerCategoriaId_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.obtenerCategoriaId(999L)
        );

        assertTrue(exception.getMessage().contains("Categoría"));
        verify(categoriaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe crear categoría exitosamente con datos válidos")
    void testInsertarCategoria_ConDatosValidos_DebeCrearCategoria() {
        // ARRANGE
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // ACT
        Categoria resultado = categoriaService.insertarCategoria(categoriaDto);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Maquillaje", resultado.getNombreCategoria());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el nombre está vacío")
    void testInsertarCategoria_ConNombreVacio_DebeLanzarExcepcion() {
        // ARRANGE
        categoriaDto.setNombreCategoria("");

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> categoriaService.insertarCategoria(categoriaDto)
        );

        assertTrue(exception.getMessage().contains("nombre"));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el nombre es null")
    void testInsertarCategoria_ConNombreNull_DebeLanzarExcepcion() {
        // ARRANGE
        categoriaDto.setNombreCategoria(null);

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> categoriaService.insertarCategoria(categoriaDto)
        );

        assertTrue(exception.getMessage().contains("nombre"));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe actualizar categoría exitosamente")
    void testActualizarCategoria_ConDatosValidos_DebeActualizarCategoria() {
        // ARRANGE
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1L);
        categoriaActualizada.setNombreCategoria("Skincare");
        categoriaActualizada.setEstado((short) 1);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // ACT
        Categoria resultado = categoriaService.actualizarCategoria(categoriaActualizada);

        // ASSERT
        assertNotNull(resultado);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException al actualizar sin ID")
    void testActualizarCategoria_SinId_DebeLanzarExcepcion() {
        // ARRANGE
        Categoria categoriaSinId = new Categoria();
        categoriaSinId.setNombreCategoria("Test");

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> categoriaService.actualizarCategoria(categoriaSinId)
        );

        assertTrue(exception.getMessage().contains("ID"));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al actualizar categoría inexistente")
    void testActualizarCategoria_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        Categoria categoriaInexistente = new Categoria();
        categoriaInexistente.setId(999L);
        categoriaInexistente.setNombreCategoria("Test");

        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.actualizarCategoria(categoriaInexistente)
        );

        assertTrue(exception.getMessage().contains("Categoría"));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe eliminar categoría exitosamente")
    void testEliminarCategoria_CuandoExiste_DebeEliminarCategoria() {
        // ARRANGE
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        doNothing().when(categoriaRepository).deleteById(1L);

        // ACT
        String resultado = categoriaService.eliminarCategoria(1L);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.contains("eliminada exitosamente"));
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al eliminar categoría inexistente")
    void testEliminarCategoria_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.eliminarCategoria(999L)
        );

        assertTrue(exception.getMessage().contains("Categoría"));
        verify(categoriaRepository, never()).deleteById(any());
    }
}
