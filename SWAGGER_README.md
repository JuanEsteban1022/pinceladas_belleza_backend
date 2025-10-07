# Documentación de API con Swagger

## 🚀 Acceso a Swagger UI

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API en:

```
http://localhost:8080/swagger-ui.html
```

O también puedes acceder a través de:

```
http://localhost:8080/swagger-ui/index.html
```

## 📄 Documentación OpenAPI (JSON)

La especificación OpenAPI en formato JSON está disponible en:

```
http://localhost:8080/v3/api-docs
```

## 🔐 Autenticación en Swagger

La API utiliza autenticación JWT (Bearer Token). Para probar los endpoints protegidos:

1. **Registrar un usuario** (si no tienes uno):
   - Usa el endpoint `POST /auth/signup`
   - Proporciona los datos requeridos en el body

2. **Iniciar sesión**:
   - Usa el endpoint `POST /auth/login`
   - Proporciona tus credenciales
   - Copia el token JWT de la respuesta

3. **Autorizar en Swagger**:
   - Haz clic en el botón **"Authorize"** (🔒) en la parte superior derecha
   - Ingresa el token en el formato: `Bearer <tu-token-jwt>`
   - Haz clic en **"Authorize"**
   - Ahora puedes probar todos los endpoints protegidos

## 📚 Endpoints Disponibles

### Autenticación
- `POST /auth/signup` - Registrar nuevo usuario
- `POST /auth/login` - Iniciar sesión

### Productos
- `GET /productos` - Obtener todos los productos
- `GET /productos/{id}` - Obtener producto por ID
- `POST /productos/create` - Crear nuevo producto
- `PATCH /productos/update` - Actualizar producto
- `DELETE /productos/{id}` - Eliminar producto

### Categorías
- `GET /category` - Obtener todas las categorías
- `GET /category/{id}` - Obtener categoría por ID
- `POST /category/create` - Crear nueva categoría
- `PATCH /category/update` - Actualizar categoría
- `DELETE /category/{id}` - Eliminar categoría

### Proveedores
- `GET /proveedor` - Obtener todos los proveedores
- `GET /proveedor/{id}` - Obtener proveedor por ID
- `POST /proveedor/create` - Crear nuevo proveedor
- `PATCH /proveedor/update` - Actualizar proveedor

## ⚙️ Configuración

La configuración de Swagger se encuentra en:
- **Clase de configuración**: `OpenApiConfiguration.java`
- **Configuración en application.yml**:
  ```yaml
  springdoc:
    api-docs:
      path: /v3/api-docs
    swagger-ui:
      path: /swagger-ui.html
      enabled: true
      operations-sorter: method
      tags-sorter: alpha
  ```

## 🔧 Personalización

Para personalizar la documentación de un endpoint, usa las siguientes anotaciones:

```java
@Operation(summary = "Resumen breve", description = "Descripción detallada")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Éxito"),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
```

## 📝 Notas

- Todos los endpoints excepto `/auth/**` y `/swagger-ui/**` requieren autenticación JWT
- Los tokens JWT expiran después de 24 horas (configurable en `application.yml`)
- La documentación se actualiza automáticamente al modificar los controladores
