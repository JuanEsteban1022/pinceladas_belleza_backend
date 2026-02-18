# Implementación de GraphQL

Este proyecto ahora incluye soporte para GraphQL junto con la API REST existente.

## Configuración

### Dependencias Agregadas
- `spring-boot-starter-graphql` - Starter de GraphQL para Spring Boot

### Endpoint de GraphQL
- **API GraphQL**: `http://localhost:8080/graphql`
- **Interfaz GraphiQL**: `http://localhost:8080/graphiql`

## Esquema

El esquema de GraphQL está definido en `src/main/resources/graphql/schema.graphqls` e incluye:

### Tipos
- `Producto` - Información del producto con relaciones a Categoría y Proveedor
- `Categoria` - Información de la categoría
- `Proveedor` - Información del proveedor  
- `Venta` - Información de ventas
- `VentaMensual` - Estadísticas de ventas mensuales
- `Pedido` - Información de pedidos

### Consultas (Queries)
- `productos` - Obtener todos los productos
- `productoById(id: ID!)` - Obtener producto por ID
- `categorias` - Obtener todas las categorías
- `categoriaById(id: ID!)` - Obtener categoría por ID
- `proveedores` - Obtener todos los proveedores
- `proveedorById(id: ID!)` - Obtener proveedor por ID
- `ventas` - Obtener todas las ventas
- `ventaById(id: ID!)` - Obtener venta por ID
- `ventasMensuales` - Obtener estadísticas de ventas mensuales
- `pedidos` - Obtener todos los pedidos
- `pedidoById(id: ID!)` - Obtener pedido por ID

### Mutaciones (Mutations)
- `createProducto(input: ProductoInput!)` - Crear nuevo producto
- `updateProducto(input: ProductoUpdateInput!)` - Actualizar producto existente
- `deleteProducto(id: ID!)` - Eliminar producto
- `createCategoria(input: CategoriaInput!)` - Crear nueva categoría
- `updateCategoria(input: CategoriaUpdateInput!)` - Actualizar categoría existente
- `deleteCategoria(id: ID!)` - Eliminar categoría
- `createVenta(input: VentaInput!)` - Crear nueva venta
- `createPedido(input: PedidoInput!)` - Crear nuevo pedido

## Resolvers

### ProductosResolver
Ubicado en: `src/main/java/com/management/backend_pinceladas_belleza/productos/graphql/ProductosResolver.java`

Maneja todas las operaciones GraphQL de Producto usando el `ProductoService` existente.

### CategoriasResolver  
Ubicado en: `src/main/java/com/management/backend_pinceladas_belleza/categorias/graphql/CategoriasResolver.java`

Maneja todas las operaciones GraphQL de Categoría usando el `CategoriaService` existente.

### VentasResolver
Ubicado en: `src/main/java/com/management/backend_pinceladas_belleza/ventas/graphql/VentasResolver.java`

Maneja operaciones GraphQL de Venta con datos de prueba (similar al controlador existente).

### PedidosResolver
Ubicado en: `src/main/java/com/management/backend_pinceladas_belleza/pedidos/graphql/PedidosResolver.java`

Maneja operaciones GraphQL de Pedido con datos de prueba (similar al controlador existente).

## Ejemplos de Consultas

### Obtener todos los productos
```graphql
query {
  productos {
    id
    nombre
    descripcion
    precio
    cantidadStock
    categoria {
      id
      nombreCategoria
    }
    proveedor {
      id
      nombre
    }
  }
}
```

### Obtener producto por ID
```graphql
query {
  productoById(id: 1) {
    id
    nombre
    precio
    categoria {
      nombreCategoria
    }
  }
}
```

### Crear nuevo producto
```graphql
mutation {
  createProducto(input: {
    nombre: "Nuevo Producto"
    descripcion: "Descripción del producto"
    precio: 99.99
    cantidadStock: 10
    categoriaId: 1
    proveedorId: 1
  }) {
    id
    nombre
    precio
  }
}
```

## Probando con GraphiQL

1. Inicia la aplicación
2. Abre el navegador en `http://localhost:8080/graphiql`
3. Usa la interfaz para probar consultas y mutaciones
4. La documentación del esquema está disponible en la pestaña "Docs" en el lado derecho

## Integración con Seguridad Existente

Los endpoints de GraphQL heredan la configuración de Spring Security existente. La autenticación JWT funcionará de la misma manera que con los endpoints REST.

## Próximos Pasos

1. Reemplazar los datos de prueba en VentasResolver y PedidosResolver con implementaciones de servicios reales
2. Agregar manejo de errores y validación apropiados
3. Considerar agregar suscripciones de GraphQL para actualizaciones en tiempo real
4. Agregar pruebas específicas de GraphQL
