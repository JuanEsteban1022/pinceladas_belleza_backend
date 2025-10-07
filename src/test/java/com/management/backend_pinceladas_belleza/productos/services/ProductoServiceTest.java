package com.management.backend_pinceladas_belleza.productos.services;

import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import com.management.backend_pinceladas_belleza.categorias.repository.CategoriaRepository;
import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import com.management.backend_pinceladas_belleza.productos.repository.ProductosRepository;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import com.management.backend_pinceladas_belleza.proveedores.repository.ProveedoresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoService
 * 
 * @ExtendWith(MockitoExtension.class) - Habilita Mockito para crear mocks
 * @Mock - Crea objetos simulados (mocks) de las dependencias
 * @InjectMocks - Inyecta los mocks en la clase que estamos probando
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductosRepository productosRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProveedoresRepository proveedoresRepository;

    @InjectMocks
    private ProductoService productoService;

    private Productos producto;
    private ProductosDto productoDto;
    private Categoria categoria;
    private Proveedor proveedor;

    /**
     * Este método se ejecuta ANTES de cada prueba
     * Aquí preparamos los datos de prueba que usaremos
     */
    @BeforeEach
    void setUp() {
        // Crear una categoría de prueba
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombreCategoria("Maquillaje");

        // Crear un proveedor de prueba
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor Test");

        // Crear un producto de prueba
        producto = Productos.builder()
                .id(1L)
                .nombre("Labial Rojo")
                .descripcion("Labial de larga duración")
                .precio(new BigDecimal("25.50"))
                .cantidadStock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .fechaCreacion(LocalDate.now())
                .urlDrive("http://drive.google.com/test")
                .build();

        // Crear un DTO de producto de prueba
        productoDto = ProductosDto.builder()
                .nombre("Labial Rojo")
                .descripcion("Labial de larga duración")
                .precio(new BigDecimal("25.50"))
                .cantidadStock(100)
                .categoriaId(1L)
                .proveedorId(1L)
                .urlDrive("http://drive.google.com/test")
                .build();
    }

    /**
     * PRUEBA 1: Obtener todos los productos exitosamente
     */
    @Test
    @DisplayName("Debe retornar lista de todos los productos")
    void testGetAll_DebeRetornarListaDeProductos() {
        // ARRANGE (Preparar): Configuramos el comportamiento del mock
        List<Productos> productosEsperados = Arrays.asList(producto);
        when(productosRepository.findAll()).thenReturn(productosEsperados);

        // ACT (Actuar): Ejecutamos el método que queremos probar
        List<Productos> resultado = productoService.getAll();

        // ASSERT (Afirmar): Verificamos que el resultado sea el esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Labial Rojo", resultado.get(0).getNombre());
        
        // Verificamos que el método del repositorio fue llamado exactamente 1 vez
        verify(productosRepository, times(1)).findAll();
    }

    /**
     * PRUEBA 2: Obtener producto por ID exitosamente
     */
    @Test
    @DisplayName("Debe retornar producto cuando existe el ID")
    void testGetById_CuandoExiste_DebeRetornarProducto() {
        // ARRANGE
        when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));

        // ACT
        Productos resultado = productoService.getById(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Labial Rojo", resultado.getNombre());
        verify(productosRepository, times(1)).findById(1L);
    }

    /**
     * PRUEBA 3: Lanzar excepción cuando el producto no existe
     */
    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el producto no existe")
    void testGetById_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        when(productosRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Verificamos que se lance la excepción correcta
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.getById(999L)
        );

        // Verificamos el mensaje de la excepción
        assertTrue(exception.getMessage().contains("Producto"));
        assertTrue(exception.getMessage().contains("999"));
        verify(productosRepository, times(1)).findById(999L);
    }

    /**
     * PRUEBA 4: Crear producto exitosamente
     */
    @Test
    @DisplayName("Debe crear producto exitosamente con datos válidos")
    void testCreateProducto_ConDatosValidos_DebeCrearProducto() {
        // ARRANGE
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(proveedoresRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(productosRepository.save(any(Productos.class))).thenReturn(producto);

        // ACT
        Productos resultado = productoService.createProducto(productoDto);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Labial Rojo", resultado.getNombre());
        assertEquals(new BigDecimal("25.50"), resultado.getPrecio());
        
        // Verificamos que se llamaron los métodos necesarios
        verify(categoriaRepository, times(1)).findById(1L);
        verify(proveedoresRepository, times(1)).findById(1L);
        verify(productosRepository, times(1)).save(any(Productos.class));
    }

    /**
     * PRUEBA 5: Lanzar excepción cuando el nombre está vacío
     */
    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el nombre está vacío")
    void testCreateProducto_ConNombreVacio_DebeLanzarExcepcion() {
        // ARRANGE
        productoDto.setNombre("");

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> productoService.createProducto(productoDto)
        );

        assertTrue(exception.getMessage().contains("nombre"));
        
        // Verificamos que NO se llamó al repositorio
        verify(productosRepository, never()).save(any(Productos.class));
    }

    /**
     * PRUEBA 6: Lanzar excepción cuando el precio es inválido
     */
    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el precio es 0 o negativo")
    void testCreateProducto_ConPrecioInvalido_DebeLanzarExcepcion() {
        // ARRANGE
        productoDto.setPrecio(BigDecimal.ZERO);

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> productoService.createProducto(productoDto)
        );

        assertTrue(exception.getMessage().contains("precio"));
        verify(productosRepository, never()).save(any(Productos.class));
    }

    /**
     * PRUEBA 7: Lanzar excepción cuando la categoría no existe
     */
    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la categoría no existe")
    void testCreateProducto_ConCategoriaInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());
        productoDto.setCategoriaId(999L);

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.createProducto(productoDto)
        );

        assertTrue(exception.getMessage().contains("Categoría"));
        verify(productosRepository, never()).save(any(Productos.class));
    }

    /**
     * PRUEBA 8: Lanzar excepción cuando el proveedor no existe
     */
    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el proveedor no existe")
    void testCreateProducto_ConProveedorInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(proveedoresRepository.findById(999L)).thenReturn(Optional.empty());
        productoDto.setProveedorId(999L);

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.createProducto(productoDto)
        );

        assertTrue(exception.getMessage().contains("Proveedor"));
        verify(productosRepository, never()).save(any(Productos.class));
    }

    /**
     * PRUEBA 9: Actualizar producto exitosamente
     */
    @Test
    @DisplayName("Debe actualizar producto exitosamente")
    void testUpdateProducto_ConDatosValidos_DebeActualizarProducto() {
        // ARRANGE
        Productos productoActualizado = Productos.builder()
                .id(1L)
                .nombre("Labial Rosa")
                .precio(new BigDecimal("30.00"))
                .build();

        when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productosRepository.save(any(Productos.class))).thenReturn(producto);

        // ACT
        Productos resultado = productoService.updateProducto(productoActualizado);

        // ASSERT
        assertNotNull(resultado);
        verify(productosRepository, times(1)).findById(1L);
        verify(productosRepository, times(1)).save(any(Productos.class));
    }

    /**
     * PRUEBA 10: Lanzar excepción al actualizar sin ID
     */
    @Test
    @DisplayName("Debe lanzar BadRequestException al actualizar sin ID")
    void testUpdateProducto_SinId_DebeLanzarExcepcion() {
        // ARRANGE
        Productos productoSinId = new Productos();
        productoSinId.setNombre("Test");

        // ACT & ASSERT
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> productoService.updateProducto(productoSinId)
        );

        assertTrue(exception.getMessage().contains("ID"));
        verify(productosRepository, never()).save(any(Productos.class));
    }

    /**
     * PRUEBA 11: Eliminar producto exitosamente
     */
    @Test
    @DisplayName("Debe eliminar producto exitosamente")
    void testDeleteProducto_CuandoExiste_DebeEliminarProducto() {
        // ARRANGE
        when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));
        doNothing().when(productosRepository).deleteById(1L);

        // ACT
        String resultado = productoService.deleteProducto(1L);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.contains("eliminado exitosamente"));
        verify(productosRepository, times(1)).findById(1L);
        verify(productosRepository, times(1)).deleteById(1L);
    }

    /**
     * PRUEBA 12: Lanzar excepción al eliminar producto inexistente
     */
    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al eliminar producto inexistente")
    void testDeleteProducto_CuandoNoExiste_DebeLanzarExcepcion() {
        // ARRANGE
        when(productosRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.deleteProducto(999L)
        );

        assertTrue(exception.getMessage().contains("Producto"));
        verify(productosRepository, never()).deleteById(anyLong());
    }
}
