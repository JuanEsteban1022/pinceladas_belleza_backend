# 📮 Guía de Uso de Postman

## 🚀 Importar la Colección

### Paso 1: Importar la Colección
1. Abre Postman
2. Click en **"Import"** (esquina superior izquierda)
3. Arrastra el archivo `Pinceladas_Belleza_API.postman_collection.json`
4. Click en **"Import"**

### Paso 2: Importar el Entorno (Environment)
1. Click en **"Import"** nuevamente
2. Arrastra el archivo `Pinceladas_Belleza.postman_environment.json`
3. Click en **"Import"**

### Paso 3: Activar el Entorno
1. En la esquina superior derecha, verás un dropdown de entornos
2. Selecciona **"Pinceladas Belleza - Local"**
3. ¡Listo! Ahora puedes usar las variables de entorno

---

## 🔐 Flujo de Autenticación

### 1. Registrar un Usuario (Primera vez)
```
POST {{base_url}}/auth/signup
```

**Body (JSON):**
```json
{
    "username": "admin",
    "email": "admin@pinceladasbelleza.com",
    "password": "Admin123!",
    "fullName": "Administrador"
}
```

**Respuesta esperada:** `200 OK`

### 2. Iniciar Sesión
```
POST {{base_url}}/auth/login
```

**Body (JSON):**
```json
{
    "username": "admin",
    "password": "Admin123!"
}
```

**Respuesta esperada:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400000,
    "usuario": {
        "id": 1,
        "username": "admin",
        "email": "admin@pinceladasbelleza.com"
    }
}
```

**✨ Magia de Postman:** El token se guarda automáticamente en la variable `{{jwt_token}}` gracias al script de prueba.

---

## 📝 Uso de los Endpoints

### Variables Disponibles

La colección usa estas variables de entorno:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `{{base_url}}` | URL base de la API | `http://localhost:8080` |
| `{{jwt_token}}` | Token JWT (se guarda automáticamente) | `eyJhbGciOi...` |
| `{{producto_id}}` | ID del último producto creado | `1` |
| `{{categoria_id}}` | ID de la última categoría creada | `1` |
| `{{proveedor_id}}` | ID del último proveedor creado | `1` |

### Autenticación Automática

Todos los endpoints (excepto `/auth/**`) ya tienen configurada la autenticación Bearer Token:

```
Authorization: Bearer {{jwt_token}}
```

No necesitas configurar nada manualmente, ¡ya está listo!

---

## 🎯 Ejemplos de Uso

### Categorías

#### 1. Crear una Categoría
```
POST {{base_url}}/category/create
```

**Body:**
```json
{
    "nombreCategoria": "Maquillaje",
    "estado": true
}
```

**Respuesta:**
```json
{
    "id": 1,
    "nombreCategoria": "Maquillaje",
    "estado": true
}
```

#### 2. Obtener Todas las Categorías
```
GET {{base_url}}/category
```

#### 3. Obtener Categoría por ID
```
GET {{base_url}}/category/1
```

#### 4. Actualizar Categoría
```
PATCH {{base_url}}/category/update
```

**Body:**
```json
{
    "id": 1,
    "nombreCategoria": "Maquillaje Premium",
    "estado": true
}
```

#### 5. Eliminar Categoría
```
DELETE {{base_url}}/category/1
```

---

### Proveedores

#### 1. Crear un Proveedor
```
POST {{base_url}}/proveedor/create
```

**Body:**
```json
{
    "nombre": "Distribuidora Beauty SA",
    "contacto": "contacto@beautysa.com"
}
```

#### 2. Obtener Todos los Proveedores
```
GET {{base_url}}/proveedor
```

#### 3. Obtener Proveedor por ID
```
GET {{base_url}}/proveedor/1
```

#### 4. Actualizar Proveedor
```
PATCH {{base_url}}/proveedor/update
```

**Body:**
```json
{
    "id": 1,
    "nombre": "Distribuidora Beauty Premium SA",
    "contacto": "info@beautypremium.com"
}
```

---

### Productos

#### 1. Crear un Producto
```
POST {{base_url}}/productos/create
```

**Body:**
```json
{
    "nombre": "Labial Rojo Mate",
    "descripcion": "Labial de larga duración con acabado mate",
    "precio": 25.50,
    "cantidadStock": 100,
    "categoriaId": 1,
    "proveedorId": 1,
    "urlDrive": "https://drive.google.com/file/ejemplo"
}
```

**Nota:** Asegúrate de que `categoriaId` y `proveedorId` existan antes de crear un producto.

#### 2. Obtener Todos los Productos
```
GET {{base_url}}/productos
```

#### 3. Obtener Producto por ID
```
GET {{base_url}}/productos/1
```

#### 4. Actualizar Producto
```
PATCH {{base_url}}/productos/update
```

**Body:**
```json
{
    "id": 1,
    "nombre": "Labial Rojo Mate Premium",
    "descripcion": "Labial de larga duración con acabado mate premium",
    "precio": 30.00,
    "cantidadStock": 150
}
```

#### 5. Eliminar Producto
```
DELETE {{base_url}}/productos/1
```

---

## 🔄 Flujo Completo de Prueba

Sigue este orden para probar toda la API:

### 1. Autenticación
- ✅ Registrar usuario (`POST /auth/signup`)
- ✅ Iniciar sesión (`POST /auth/login`)

### 2. Crear Datos Base
- ✅ Crear categoría (`POST /category/create`)
- ✅ Crear proveedor (`POST /proveedor/create`)

### 3. Gestionar Productos
- ✅ Crear producto (`POST /productos/create`)
- ✅ Obtener todos los productos (`GET /productos`)
- ✅ Obtener producto por ID (`GET /productos/1`)
- ✅ Actualizar producto (`PATCH /productos/update`)
- ✅ Eliminar producto (`DELETE /productos/1`)

---

## 🎨 Scripts de Prueba Automáticos

La colección incluye scripts que se ejecutan automáticamente:

### Script de Login
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set('jwt_token', jsonData.token);
    console.log('Token guardado: ' + jsonData.token);
}
```

Este script guarda automáticamente el token JWT cuando inicias sesión.

### Script de Creación
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set('producto_id', jsonData.id);
    console.log('Producto creado con ID: ' + jsonData.id);
}
```

Este script guarda el ID del recurso creado para usarlo en otras peticiones.

---

## 🐛 Manejo de Errores

### Respuestas de Error Estándar

Todas las respuestas de error siguen este formato:

```json
{
    "timestamp": "2025-10-06T11:20:30",
    "status": 404,
    "error": "Not Found",
    "message": "Producto no encontrado con id: '999'",
    "path": "/productos/999"
}
```

### Códigos de Estado Comunes

| Código | Significado | Ejemplo |
|--------|-------------|---------|
| `200` | OK | Operación exitosa |
| `201` | Created | Recurso creado exitosamente |
| `400` | Bad Request | Datos inválidos o incompletos |
| `401` | Unauthorized | Token inválido o expirado |
| `404` | Not Found | Recurso no encontrado |
| `409` | Conflict | Recurso duplicado |
| `500` | Internal Server Error | Error del servidor |

---

## 💡 Tips y Trucos

### 1. Ver Variables de Entorno
- Click en el ícono del ojo 👁️ en la esquina superior derecha
- Verás todas las variables y sus valores actuales

### 2. Editar Variables Manualmente
- Click en el ícono del ojo 👁️
- Click en "Edit" junto al entorno
- Modifica los valores según necesites

### 3. Ver la Consola de Postman
- Menú inferior: "Console"
- Aquí verás los logs de los scripts de prueba

### 4. Guardar Respuestas como Ejemplos
- Después de hacer una petición exitosa
- Click en "Save Response"
- Click en "Save as Example"
- Útil para documentar respuestas esperadas

### 5. Ejecutar Toda la Colección
- Click derecho en la colección
- "Run collection"
- Ejecuta todos los endpoints en orden

---

## 🔧 Configuración Avanzada

### Cambiar el Puerto o Host

Si tu API corre en otro puerto o host:

1. Click en el ícono del ojo 👁️
2. Click en "Edit" junto al entorno
3. Modifica `base_url`:
   - Producción: `https://api.pinceladasbelleza.com`
   - Desarrollo: `http://localhost:8080`
   - Otro puerto: `http://localhost:3000`

### Crear Múltiples Entornos

Puedes crear entornos para diferentes ambientes:

- **Local:** `http://localhost:8080`
- **Desarrollo:** `https://dev.pinceladasbelleza.com`
- **Producción:** `https://api.pinceladasbelleza.com`

---

## 📚 Recursos Adicionales

### Documentación de la API
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Archivos Relacionados
- `SWAGGER_README.md` - Guía de Swagger
- `EXCEPTION_HANDLING_README.md` - Manejo de errores
- `TESTING_GUIDE.md` - Guía de pruebas unitarias

---

## ❓ Preguntas Frecuentes

### ¿El token expira?
Sí, el token JWT expira después de 24 horas. Simplemente vuelve a hacer login.

### ¿Puedo usar la colección en equipo?
Sí, puedes compartir los archivos JSON con tu equipo o usar Postman Workspaces.

### ¿Cómo pruebo endpoints protegidos?
Todos los endpoints (excepto `/auth/**`) requieren el token JWT. Asegúrate de:
1. Haber iniciado sesión
2. Que el token esté guardado en `{{jwt_token}}`
3. Que el entorno esté activado

### ¿Qué hago si obtengo 401 Unauthorized?
1. Verifica que iniciaste sesión
2. Verifica que el token no haya expirado
3. Verifica que el entorno esté activado
4. Intenta hacer login nuevamente

---

## 🎉 ¡Listo para Usar!

Ahora tienes todo lo necesario para probar tu API:

✅ Colección completa de Postman  
✅ Entorno configurado  
✅ Variables automáticas  
✅ Autenticación JWT integrada  
✅ Scripts de prueba automáticos  
✅ Ejemplos de uso  

**¡Feliz testing!** 🚀
