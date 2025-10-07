package com.management.backend_pinceladas_belleza.proveedores.services;

import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.proveedores.dto.ProveedorDto;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import com.management.backend_pinceladas_belleza.proveedores.repository.ProveedoresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ProveedorService")
class ProveedorServiceTest {

    @Mock
    private ProveedoresRepository proveedoresRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    private Proveedor proveedor;
    private ProveedorDto proveedorDto;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor Test");
        proveedor.setContacto("test@proveedor.com");
        proveedor.setFechaCreated(LocalDate.now());

        proveedorDto = new ProveedorDto();
        proveedorDto.setNombre("Proveedor Test");
        proveedorDto.setContacto("test@proveedor.com");
    }

    @Test
    @DisplayName("Debe retornar lista de todos los proveedores")
    void testGetProveedor_DebeRetornarListaDeProveedores() {
        // ARRANGE
        List<Proveedor> proveedoresEsperados = Arrays.asList(proveedor);
        when(proveedoresRepository.findAll()).thenReturn(proveedoresEsperados);

        // ACT
        List<Proveedor> resultado = proveedorService.getProveedor();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Proveedor Test", resultado.get(0).getNombre());
        verify(proveedoresRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar proveedor cuando existe el ID")
    void testGetProveedorById_CuandoExiste_DebeRetornarProveedor() {
        // ARRANGE
        when(proveedoresRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        // ACT
        Proveedor resultado = proveedorService.getProveedorById(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Proveedor Test", resultado.getNombre());
        verify(proveedoresRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el proveedor no existe")
    void testGetProveedorById_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        when(proveedoresRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> proveedorService.getProveedorById(999L)
        );

        assertTrue(exception.getMessage().contains("Proveedor"));
        verify(proveedoresRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe crear proveedor exitosamente con datos válidos")
    void testCreateProveedor_ConDatosValidos_DebeCrearProveedor() {
        // ARRANGE
        when(proveedoresRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        // ACT
        Proveedor resultado = proveedorService.createProveedor(proveedorDto);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Proveedor Test", resultado.getNombre());
        verify(proveedoresRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el nombre está vacío")
    void testCreateProveedor_ConNombreVacio_DebeLanzarExcepcion() {
        // ARRANGE
        proveedorDto.setNombre("");

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> proveedorService.createProveedor(proveedorDto)
        );

        assertTrue(exception.getMessage().contains("nombre"));
        verify(proveedoresRepository, never()).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el nombre es null")
    void testCreateProveedor_ConNombreNull_DebeLanzarExcepcion() {
        // ARRANGE
        proveedorDto.setNombre(null);

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> proveedorService.createProveedor(proveedorDto)
        );

        assertTrue(exception.getMessage().contains("nombre"));
        verify(proveedoresRepository, never()).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("Debe actualizar proveedor exitosamente")
    void testUpdateProveedor_ConDatosValidos_DebeActualizarProveedor() {
        // ARRANGE
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setId(1L);
        proveedorActualizado.setNombre("Proveedor Actualizado");
        proveedorActualizado.setContacto("nuevo@proveedor.com");

        when(proveedoresRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedoresRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        // ACT
        Proveedor resultado = proveedorService.updateProveedor(proveedorActualizado);

        // ASSERT
        assertNotNull(resultado);
        verify(proveedoresRepository, times(1)).findById(1L);
        verify(proveedoresRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException al actualizar sin ID")
    void testUpdateProveedor_SinId_DebeLanzarExcepcion() {
        // ARRANGE
        Proveedor proveedorSinId = new Proveedor();
        proveedorSinId.setNombre("Test");

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> proveedorService.updateProveedor(proveedorSinId)
        );

        assertTrue(exception.getMessage().contains("ID"));
        verify(proveedoresRepository, never()).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("Debe eliminar proveedor exitosamente")
    void testDeleteProveedor_CuandoExiste_DebeEliminarProveedor() {
        // ARRANGE
        when(proveedoresRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        doNothing().when(proveedoresRepository).deleteById(1L);

        // ACT
        String resultado = proveedorService.deleteProveedor(1L);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.contains("eliminado exitosamente"));
        verify(proveedoresRepository, times(1)).findById(1L);
        verify(proveedoresRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al eliminar proveedor inexistente")
    void testDeleteProveedor_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        when(proveedoresRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> proveedorService.deleteProveedor(999L)
        );

        assertTrue(exception.getMessage().contains("Proveedor"));
        verify(proveedoresRepository, never()).deleteById(any());
    }
}
