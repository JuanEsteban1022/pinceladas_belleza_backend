# Sistema de Manejo de Excepciones

## 📋 Descripción General

El proyecto implementa un sistema robusto de manejo de excepciones centralizado que proporciona respuestas de error consistentes y significativas en toda la API.

## 🏗️ Arquitectura

### Componentes Principales

1. **Excepciones Personalizadas** (`exception/`)
2. **Manejador Global** (`GlobalExceptionHandler.java`)
3. **Respuesta de Error Estándar** (`ErrorResponse.java`)

## 🎯 Excepciones Personalizadas

### 1. ResourceNotFoundException
**Uso:** Cuando un recurso solicitado no existe en la base de datos.

**Código HTTP:** `404 NOT FOUND`

**Ejemplo:**
```java
throw new ResourceNotFoundException("Producto", "id", 123);
// Mensaje: "Producto no encontrado con id: '123'"
```

### 2. BadRequestException
**Uso:** Cuando los datos enviados son inválidos o incompletos.

**Código HTTP:** `400 BAD REQUEST`

**Ejemplo:**
```java
throw new BadRequestException("El nombre del producto es requerido");
```

### 3. DuplicateResourceException
**Uso:** Cuando se intenta crear un recurso que ya existe.

**Código HTTP:** `409 CONFLICT`

**Ejemplo:**
```java
throw new DuplicateResourceException("Usuario", "email", "user@example.com");
// Mensaje: "Usuario ya existe con email: 'user@example.com'"
```

### 4. UnauthorizedException
**Uso:** Cuando las credenciales son inválidas o el token no es válido.

**Código HTTP:** `401 UNAUTHORIZED`

**Ejemplo:**
```java
throw new UnauthorizedException("Token inválido o expirado");
```

## 🛡️ GlobalExceptionHandler

Maneja todas las excepciones de forma centralizada usando `@RestControllerAdvice`.

### Excepciones Manejadas:

| Excepción | Código HTTP | Descripción |
|-----------|-------------|-------------|
| `ResourceNotFoundException` | 404 | Recurso no encontrado |
| `BadRequestException` | 400 | Solicitud incorrecta |
| `DuplicateResourceException` | 409 | Recurso duplicado |
| `UnauthorizedException` | 401 | No autorizado |
| `BadCredentialsException` | 401 | Credenciales inválidas |
| `UsernameNotFoundException` | 401 | Usuario no encontrado |
| `ExpiredJwtException` | 401 | Token JWT expirado |
| `MalformedJwtException` | 401 | Token JWT malformado |
| `SignatureException` | 401 | Firma JWT inválida |
| `MethodArgumentNotValidException` | 400 | Error de validación |
| `MethodArgumentTypeMismatchException` | 400 | Tipo de argumento incorrecto |
| `IllegalArgumentException` | 400 | Argumento ilegal |
| `Exception` | 500 | Error interno del servidor |

## 📝 Formato de Respuesta de Error

Todas las respuestas de error siguen el formato estándar de `ErrorResponse`:

```json
{
  "timestamp": "2025-10-06T10:45:30",
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado con id: '123'",
  "path": "/productos/123",
  "details": []
}
```

### Campos:

- **timestamp**: Fecha y hora del error (ISO 8601)
- **status**: Código de estado HTTP
- **error**: Descripción del tipo de error HTTP
- **message**: Mensaje descriptivo del error
- **path**: Ruta del endpoint donde ocurrió el error
- **details**: Lista de detalles adicionales (usado en validaciones)

## 🔧 Implementación en Servicios

### Antes (sin manejo de excepciones):
```java
@Override
public Producto getById(Long id) {
    return productosRepository.findById(id).orElse(null);
}
```

### Después (con manejo de excepciones):
```java
@Override
public Producto getById(Long id) {
    return productosRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
}
```

## 🎮 Implementación en Controladores

### Antes (con try-catch):
```java
@GetMapping("/{id}")
public ResponseEntity<Producto> getById(@PathVariable Long id) {
    try {
        return new ResponseEntity<>(productos.getById(id), HttpStatus.OK);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

### Después (sin try-catch):
```java
@GetMapping("/{id}")
public ResponseEntity<Producto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productos.getById(id));
}
```

**Ventaja:** El código es más limpio y las excepciones son manejadas automáticamente por `GlobalExceptionHandler`.

## ✅ Validaciones Implementadas

### Productos:
- ✅ Nombre requerido y no vacío
- ✅ Precio mayor a 0
- ✅ Categoría debe existir
- ✅ Proveedor debe existir
- ✅ ID requerido para actualizar

### Categorías:
- ✅ Nombre requerido y no vacío
- ✅ ID requerido para actualizar

### Proveedores:
- ✅ Nombre requerido y no vacío
- ✅ ID requerido para actualizar

## 🧪 Ejemplos de Respuestas

### Recurso No Encontrado (404)
**Request:** `GET /productos/999`

**Response:**
```json
{
  "timestamp": "2025-10-06T10:45:30",
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado con id: '999'",
  "path": "/productos/999"
}
```

### Validación Fallida (400)
**Request:** `POST /productos/create` con nombre vacío

**Response:**
```json
{
  "timestamp": "2025-10-06T10:45:30",
  "status": 400,
  "error": "Bad Request",
  "message": "El nombre del producto es requerido",
  "path": "/productos/create"
}
```

### Token Expirado (401)
**Request:** `GET /productos` con token expirado

**Response:**
```json
{
  "timestamp": "2025-10-06T10:45:30",
  "status": 401,
  "error": "Unauthorized",
  "message": "El token JWT ha expirado",
  "path": "/productos"
}
```

### Error Interno (500)
**Response:**
```json
{
  "timestamp": "2025-10-06T10:45:30",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Ha ocurrido un error interno en el servidor",
  "path": "/productos"
}
```

## 📚 Mejores Prácticas

1. **Usa excepciones específicas**: Lanza la excepción más apropiada según el contexto
2. **Mensajes descriptivos**: Proporciona mensajes claros que ayuden a entender el problema
3. **No captures excepciones en controladores**: Deja que `GlobalExceptionHandler` las maneje
4. **Valida en servicios**: Implementa validaciones de negocio en la capa de servicio
5. **Logging**: Todas las excepciones se registran automáticamente con `@Slf4j`

## 🔍 Debugging

Los logs de excepciones se registran automáticamente:

```
ERROR - ResourceNotFoundException: Producto no encontrado con id: '999'
ERROR - BadRequestException: El nombre del producto es requerido
```

## 🚀 Integración con Swagger

Las respuestas de error están documentadas en Swagger con las anotaciones `@ApiResponses`:

```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Éxito"),
    @ApiResponse(responseCode = "404", description = "No encontrado"),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
})
```

## 📝 Notas Adicionales

- El sistema captura y maneja automáticamente excepciones de Spring Security
- Las excepciones de JWT son manejadas específicamente para proporcionar mensajes claros
- El formato de respuesta es consistente en toda la API
- Los errores de validación incluyen detalles específicos de cada campo
