package com.management.backend_pinceladas_belleza.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.backend_pinceladas_belleza.productos.dto.ProductosDto;
import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import com.management.backend_pinceladas_belleza.productos.interfaces.IProductos;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de Integración para ProductosController
 * 
 * @WebMvcTest - Carga solo el controlador y sus dependencias
 * @AutoConfigureMockMvc - Configura MockMvc automáticamente
 * @MockBean - Crea un mock del servicio
 * @WithMockUser - Simula un usuario autenticado
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración - ProductosController")
class ProductosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IProductos productosService;

    @MockBean
    private com.management.backend_pinceladas_belleza.config.jwt.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

    private Productos producto;
    private ProductosDto productoDto;

    @BeforeEach
    void setUp() {
        producto = Productos.builder()
                .id(1L)
                .nombre("Labial Rojo")
                .descripcion("Labial de larga duración")
                .precio(new BigDecimal("25.50"))
                .cantidadStock(100)
                .build();

        productoDto = ProductosDto.builder()
                .nombre("Labial Rojo")
                .descripcion("Labial de larga duración")
                .precio(new BigDecimal("25.50"))
                .cantidadStock(100)
                .categoriaId(1L)
                .proveedorId(1L)
                .build();
    }

    /**
     * PRUEBA 1: GET /productos - Obtener todos los productos
     */
    @Test
    @WithMockUser
    @DisplayName("GET /productos - Debe retornar lista de productos con status 200")
    void testGetAll_DebeRetornarListaDeProductos() throws Exception {
        // ARRANGE
        List<Productos> productos = Arrays.asList(producto);
        when(productosService.getAll()).thenReturn(productos);

        // ACT & ASSERT
        mockMvc.perform(get("/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Labial Rojo")))
                .andExpect(jsonPath("$[0].precio", is(25.50)));
    }

    /**
     * PRUEBA 2: GET /productos/{id} - Obtener producto por ID
     */
    @Test
    @WithMockUser
    @DisplayName("GET /productos/{id} - Debe retornar producto cuando existe")
    void testGetById_CuandoExiste_DebeRetornarProducto() throws Exception {
        // ARRANGE
        when(productosService.getById(1L)).thenReturn(producto);

        // ACT & ASSERT
        mockMvc.perform(get("/productos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Labial Rojo")))
                .andExpect(jsonPath("$.precio", is(25.50)));
    }

    /**
     * PRUEBA 3: GET /productos/{id} - Producto no encontrado
     */
    @Test
    @WithMockUser
    @DisplayName("GET /productos/{id} - Debe retornar 404 cuando no existe")
    void testGetById_CuandoNoExiste_DebeRetornar404() throws Exception {
        // ARRANGE
        when(productosService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Producto", "id", 999L));

        // ACT & ASSERT
        mockMvc.perform(get("/productos/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Producto")));
    }

    /**
     * PRUEBA 4: POST /productos/create - Crear producto exitosamente
     */
    @Test
    @WithMockUser
    @DisplayName("POST /productos/create - Debe crear producto con status 201")
    void testCreate_ConDatosValidos_DebeCrearProducto() throws Exception {
        // ARRANGE
        when(productosService.createProducto(any(ProductosDto.class))).thenReturn(producto);

        // ACT & ASSERT
        mockMvc.perform(post("/productos/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Labial Rojo")))
                .andExpect(jsonPath("$.precio", is(25.50)));
    }

    /**
     * PRUEBA 5: POST /productos/create - Datos inválidos
     */
    @Test
    @WithMockUser
    @DisplayName("POST /productos/create - Debe retornar 400 con datos inválidos")
    void testCreate_ConDatosInvalidos_DebeRetornar400() throws Exception {
        // ARRANGE
        when(productosService.createProducto(any(ProductosDto.class)))
                .thenThrow(new BadRequestException("El nombre del producto es requerido"));

        // ACT & ASSERT
        mockMvc.perform(post("/productos/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("nombre")));
    }

    /**
     * PRUEBA 6: PATCH /productos/update - Actualizar producto
     */
    @Test
    @WithMockUser
    @DisplayName("PATCH /productos/update - Debe actualizar producto con status 200")
    void testUpdate_ConDatosValidos_DebeActualizarProducto() throws Exception {
        // ARRANGE
        when(productosService.updateProducto(any(Productos.class))).thenReturn(producto);

        // ACT & ASSERT
        mockMvc.perform(patch("/productos/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Labial Rojo")));
    }

    /**
     * PRUEBA 7: DELETE /productos/{id} - Eliminar producto
     */
    @Test
    @WithMockUser
    @DisplayName("DELETE /productos/{id} - Debe eliminar producto con status 200")
    void testDelete_CuandoExiste_DebeEliminarProducto() throws Exception {
        // ARRANGE
        when(productosService.deleteProducto(1L)).thenReturn("Producto eliminado exitosamente");

        // ACT & ASSERT
        mockMvc.perform(delete("/productos/{id}", 1L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eliminado exitosamente")));
    }

    /**
     * PRUEBA 8: DELETE /productos/{id} - Producto no encontrado
     */
    @Test
    @WithMockUser
    @DisplayName("DELETE /productos/{id} - Debe retornar 404 cuando no existe")
    void testDelete_CuandoNoExiste_DebeRetornar404() throws Exception {
        // ARRANGE
        when(productosService.deleteProducto(999L))
                .thenThrow(new ResourceNotFoundException("Producto", "id", 999L));

        // ACT & ASSERT
        mockMvc.perform(delete("/productos/{id}", 999L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    /**
     * PRUEBA 9: Verificar formato JSON de respuesta de error
     */
    @Test
    @WithMockUser
    @DisplayName("Debe retornar formato de error estándar")
    void testErrorResponse_DebeRetornarFormatoEstandar() throws Exception {
        // ARRANGE
        when(productosService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Producto", "id", 999L));

        // ACT & ASSERT
        mockMvc.perform(get("/productos/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").exists());
    }
}
