# 📚 Guía Completa de Pruebas Unitarias

## 🎯 ¿Qué son las Pruebas Unitarias?

Las **pruebas unitarias** son pequeños tests que verifican que una parte específica de tu código (una "unidad") funciona correctamente de forma aislada.

### ¿Por qué son importantes?

✅ **Detectan errores temprano** - Antes de que lleguen a producción  
✅ **Documentan el código** - Las pruebas muestran cómo usar tu código  
✅ **Facilitan cambios** - Puedes modificar código con confianza  
✅ **Mejoran el diseño** - Te obligan a escribir código más limpio  

---

## 🏗️ Estructura de una Prueba Unitaria

Todas las pruebas siguen el patrón **AAA**:

```java
@Test
void nombreDescriptivoDeLaPrueba() {
    // ARRANGE (Preparar): Configura los datos y comportamiento
    when(repository.findById(1L)).thenReturn(Optional.of(producto));
    
    // ACT (Actuar): Ejecuta el método que quieres probar
    Producto resultado = service.getById(1L);
    
    // ASSERT (Afirmar): Verifica que el resultado sea el esperado
    assertEquals("Labial Rojo", resultado.getNombre());
}
```

---

## 🔧 Herramientas que Usamos

### 1. **JUnit 5** - Framework de pruebas
```java
@Test                    // Marca un método como prueba
@BeforeEach             // Se ejecuta antes de cada prueba
@DisplayName("...")     // Nombre descriptivo de la prueba
```

### 2. **Mockito** - Crea objetos simulados (mocks)
```java
@Mock                   // Crea un objeto simulado
@InjectMocks           // Inyecta los mocks en la clase a probar
when(...).thenReturn(...) // Define comportamiento del mock
verify(...)            // Verifica que se llamó un método
```

### 3. **AssertJ / JUnit Assertions** - Verificaciones
```java
assertEquals(esperado, actual)      // Verifica igualdad
assertNotNull(objeto)               // Verifica que no sea null
assertTrue(condicion)               // Verifica que sea verdadero
assertThrows(Exception.class, ...) // Verifica que lance excepción
```

---

## 📝 Anatomía de una Clase de Prueba

```java
@ExtendWith(MockitoExtension.class)  // 1. Habilita Mockito
@DisplayName("Pruebas - MiServicio")  // 2. Nombre descriptivo
class MiServicioTest {

    @Mock                             // 3. Dependencias simuladas
    private MiRepository repository;

    @InjectMocks                      // 4. Clase que probamos
    private MiServicio servicio;

    private MiEntidad entidad;        // 5. Datos de prueba

    @BeforeEach                       // 6. Preparación antes de cada test
    void setUp() {
        entidad = new MiEntidad();
        entidad.setId(1L);
        entidad.setNombre("Test");
    }

    @Test                             // 7. Prueba individual
    @DisplayName("Debe hacer algo")
    void testMetodo() {
        // Prueba aquí
    }
}
```

---

## 🎓 Conceptos Clave Explicados

### ¿Qué es un Mock?

Un **mock** es un objeto "falso" que simula el comportamiento de un objeto real.

**¿Por qué usar mocks?**
- No queremos acceder a la base de datos real en las pruebas
- Queremos controlar exactamente qué devuelve cada método
- Las pruebas son más rápidas y predecibles

**Ejemplo:**
```java
// En lugar de llamar a la base de datos real...
@Mock
private ProductosRepository productosRepository;

// Le decimos qué debe devolver
when(productosRepository.findById(1L))
    .thenReturn(Optional.of(producto));
```

### ¿Qué significa @InjectMocks?

Inyecta automáticamente todos los `@Mock` en la clase que estamos probando.

```java
@Mock
private ProductosRepository repository;  // Mock 1

@Mock
private CategoriaRepository categoriaRepo;  // Mock 2

@InjectMocks
private ProductoService service;  // Recibe ambos mocks
```

---

## 🧪 Tipos de Pruebas

### 1. **Prueba de Caso Exitoso** (Happy Path)

Verifica que todo funcione cuando los datos son correctos.

```java
@Test
@DisplayName("Debe crear producto exitosamente")
void testCrearProducto_ConDatosValidos() {
    // ARRANGE
    when(repository.save(any())).thenReturn(producto);
    
    // ACT
    Producto resultado = service.crear(productoDto);
    
    // ASSERT
    assertNotNull(resultado);
    assertEquals("Labial", resultado.getNombre());
    verify(repository, times(1)).save(any());
}
```

### 2. **Prueba de Excepción**

Verifica que se lancen excepciones cuando algo está mal.

```java
@Test
@DisplayName("Debe lanzar excepción cuando el nombre está vacío")
void testCrearProducto_ConNombreVacio() {
    // ARRANGE
    productoDto.setNombre("");
    
    // ACT & ASSERT
    BadRequestException exception = assertThrows(
        BadRequestException.class,
        () -> service.crear(productoDto)
    );
    
    assertTrue(exception.getMessage().contains("nombre"));
}
```

### 3. **Prueba de Recurso No Encontrado**

Verifica el comportamiento cuando algo no existe.

```java
@Test
@DisplayName("Debe lanzar excepción cuando el producto no existe")
void testObtenerProducto_CuandoNoExiste() {
    // ARRANGE
    when(repository.findById(999L)).thenReturn(Optional.empty());
    
    // ACT & ASSERT
    assertThrows(
        ResourceNotFoundException.class,
        () -> service.getById(999L)
    );
}
```

---

## 🔍 Métodos de Mockito Más Usados

### when().thenReturn()
Define qué debe devolver un método cuando se llama.

```java
when(repository.findById(1L)).thenReturn(Optional.of(producto));
// Cuando se llame a findById(1L), devuelve el producto
```

### verify()
Verifica que un método fue llamado.

```java
verify(repository, times(1)).save(any());
// Verifica que save() fue llamado exactamente 1 vez

verify(repository, never()).delete(any());
// Verifica que delete() NUNCA fue llamado
```

### any()
Acepta cualquier argumento de ese tipo.

```java
when(repository.save(any(Producto.class))).thenReturn(producto);
// Acepta cualquier objeto Producto
```

### doNothing()
Para métodos void que no devuelven nada.

```java
doNothing().when(repository).deleteById(1L);
// El método deleteById no hace nada (es void)
```

---

## 📊 Assertions Más Comunes

```java
// Verificar igualdad
assertEquals(esperado, actual);

// Verificar que no sea null
assertNotNull(objeto);

// Verificar que sea null
assertNull(objeto);

// Verificar verdadero/falso
assertTrue(condicion);
assertFalse(condicion);

// Verificar que lance excepción
assertThrows(MiExcepcion.class, () -> metodo());

// Verificar que contenga texto
assertTrue(mensaje.contains("error"));

// Verificar tamaño de lista
assertEquals(3, lista.size());
```

---

## 🚀 Cómo Ejecutar las Pruebas

### Desde la Terminal:
```bash
# Ejecutar todas las pruebas
./gradlew test

# Ejecutar pruebas de una clase específica
./gradlew test --tests ProductoServiceTest

# Ejecutar con reporte detallado
./gradlew test --info
```

### Desde IntelliJ IDEA:
1. Click derecho en la clase de prueba
2. Selecciona "Run 'NombreTest'"
3. O usa el ícono ▶️ verde junto al método

### Ver Reportes:
Los reportes HTML se generan en:
```
build/reports/tests/test/index.html
```

---

## 📖 Ejemplo Completo Paso a Paso

Vamos a crear una prueba desde cero:

### Paso 1: Crear la clase de prueba
```java
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    // Aquí irán nuestras pruebas
}
```

### Paso 2: Definir mocks y la clase a probar
```java
@Mock
private ProductosRepository repository;

@InjectMocks
private ProductoService service;
```

### Paso 3: Preparar datos de prueba
```java
private Producto producto;

@BeforeEach
void setUp() {
    producto = new Producto();
    producto.setId(1L);
    producto.setNombre("Labial");
}
```

### Paso 4: Escribir la prueba
```java
@Test
@DisplayName("Debe obtener producto por ID")
void testGetById() {
    // ARRANGE: Configuro el mock
    when(repository.findById(1L))
        .thenReturn(Optional.of(producto));
    
    // ACT: Ejecuto el método
    Producto resultado = service.getById(1L);
    
    // ASSERT: Verifico el resultado
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals("Labial", resultado.getNombre());
    
    // Verifico que se llamó al repositorio
    verify(repository, times(1)).findById(1L);
}
```

---

## 🎯 Buenas Prácticas

### ✅ DO (Hacer):

1. **Nombres descriptivos**
   ```java
   @Test
   @DisplayName("Debe lanzar excepción cuando el precio es negativo")
   void testCrearProducto_ConPrecioNegativo_DebeLanzarExcepcion()
   ```

2. **Una prueba, un concepto**
   - Cada prueba debe verificar UNA cosa específica

3. **Usar @BeforeEach para preparación común**
   ```java
   @BeforeEach
   void setUp() {
       // Datos que todas las pruebas necesitan
   }
   ```

4. **Verificar comportamiento, no implementación**
   ```java
   // ✅ Bueno: Verifico el resultado
   assertEquals("Labial", producto.getNombre());
   
   // ❌ Malo: Verifico detalles internos
   verify(repository).findById(1L);
   verify(repository).save(any());
   verify(logger).info(any());  // Demasiado detallado
   ```

### ❌ DON'T (No hacer):

1. **No probar métodos privados directamente**
   - Se prueban indirectamente a través de métodos públicos

2. **No depender del orden de ejecución**
   - Cada prueba debe ser independiente

3. **No usar datos reales de base de datos**
   - Usa mocks para simular el repositorio

4. **No hacer pruebas muy largas**
   - Si una prueba es muy larga, probablemente estás probando demasiado

---

## 📈 Cobertura de Código

### ¿Qué es la cobertura?
Es el porcentaje de tu código que está cubierto por pruebas.

### Ver cobertura en IntelliJ:
1. Click derecho en la clase de prueba
2. "Run with Coverage"
3. Verás líneas verdes (cubiertas) y rojas (no cubiertas)

### Meta recomendada:
- **70-80%** es un buen objetivo
- **100%** no siempre es necesario ni práctico

---

## 🐛 Debugging de Pruebas

### Si una prueba falla:

1. **Lee el mensaje de error**
   ```
   Expected: "Labial"
   Actual: null
   ```

2. **Verifica los mocks**
   ```java
   // ¿Configuraste el mock correctamente?
   when(repository.findById(1L)).thenReturn(Optional.of(producto));
   ```

3. **Usa breakpoints**
   - Pon un breakpoint en la prueba
   - Ejecuta en modo debug
   - Inspecciona variables

4. **Imprime valores**
   ```java
   System.out.println("Resultado: " + resultado);
   ```

---

## 📚 Recursos Adicionales

### Documentación Oficial:
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

### Comandos Útiles:
```bash
# Ejecutar solo pruebas que fallaron
./gradlew test --rerun-tasks

# Ejecutar con más detalle
./gradlew test --info

# Limpiar y ejecutar pruebas
./gradlew clean test
```

---

## 🎓 Ejercicio Práctico

Intenta crear una prueba para este método:

```java
public Producto actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
    if (nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
        throw new BadRequestException("El precio debe ser mayor a 0");
    }
    
    Producto producto = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    
    producto.setPrecio(nuevoPrecio);
    return repository.save(producto);
}
```

### Pruebas que deberías crear:
1. ✅ Actualizar precio exitosamente
2. ✅ Lanzar excepción con precio 0
3. ✅ Lanzar excepción con precio negativo
4. ✅ Lanzar excepción cuando producto no existe

---

## 🎉 ¡Felicidades!

Ahora sabes cómo crear pruebas unitarias. Recuerda:

- **Practica** escribiendo pruebas para tu código
- **Empieza simple** y ve agregando complejidad
- **Las pruebas son tu red de seguridad** cuando haces cambios
- **Un código bien probado es un código confiable**

¡Sigue practicando! 🚀
