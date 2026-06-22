# Sistema de Pedidos — Java Console App

Aplicación de consola desarrollada en Java que implementa la gestión completa de un sistema de pedidos para un local gastronómico. Permite administrar categorías, productos, usuarios y pedidos mediante un menú interactivo, con persistencia en memoria durante la ejecución.

---

## Tabla de contenidos

- [Requisitos](#requisitos)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Cómo compilar y ejecutar](#cómo-compilar-y-ejecutar)
- [Datos de ejemplo](#datos-de-ejemplo)
- [Funcionalidades](#funcionalidades)
- [Decisiones de diseño](#decisiones-de-diseño)
- [Autor](#autor)

---

## Requisitos

- Java 17 o superior (se usan `switch` expressions y `Stream` API)
- JDK instalado y `javac` / `java` disponibles en el PATH
- No requiere dependencias externas ni base de datos

---

## Estructura del proyecto

```
SistemaPedidos/
│
--- Main.java                        # Punto de entrada; carga datos de ejemplo y menú principal
│
--- entities/                        # Clases de dominio
│   --- Base.java                    # Superclase abstracta (id, eliminado, createdAt)
│   --- Categoria.java               # Categoría de productos
│   --- Producto.java                # Producto del catálogo
│   --- DetallePedido.java           # Ítem de un pedido (cantidad + subtotal)
│   --- Usuario.java                 # Usuario del sistema
│   --- Pedido.java                  # Pedido con detalles y total
│
--- enums/                           # Enumeraciones
│   --- Estado.java                  # PENDIENTE | CONFIRMADO | TERMINADO | CANCELADO
│   --- FormaPago.java               # TARJETA | TRANSFERENCIA | EFECTIVO
│   --- Rol.java                     # ADMIN | USUARIO
│
--- interfaces/
│   --- Calculable.java              # Contrato: void calcularTotal()
│
--- services/
│   --- DataStore.java               # Repositorio singleton en memoria
│   --- InputHelper.java             # Utilidades de lectura y validación por consola
│
--- menus/
    --- MenuCategorias.java          # CRUD de categorías (Épica 1)
    --- MenuProductos.java           # CRUD de productos  (Épica 2)
    --- MenuUsuarios.java            # CRUD de usuarios   (Épica 3)
    --- MenuPedidos.java             # CRUD de pedidos    (Épica 4)
```

---

## Cómo compilar y ejecutar

### Desde la raíz del proyecto

**1. Compilar**

```bash
javac -d out \
  interfaces/Calculable.java \
  enums/Estado.java enums/FormaPago.java enums/Rol.java \
  entities/Base.java entities/Categoria.java entities/Producto.java \
  entities/DetallePedido.java entities/Usuario.java entities/Pedido.java \
  services/DataStore.java services/InputHelper.java \
  menus/MenuCategorias.java menus/MenuProductos.java \
  menus/MenuUsuarios.java menus/MenuPedidos.java \
  Main.java
```

**2. Ejecutar**

```bash
java -cp out Main
```

### Desde NetBeans / IntelliJ IDEA

1. Crear un nuevo proyecto Java vacío.
2. Copiar las carpetas `entities`, `enums`, `interfaces`, `services` y `menus` dentro de `src/`.
3. Copiar `Main.java` también dentro de `src/`.
4. Ejecutar `Main` como clase principal.

---

## Datos de ejemplo

Al iniciar, la aplicación carga automáticamente un conjunto de datos de prueba para que se pueda explorar el sistema sin tener que cargar todo manualmente:

| Entidad    | Cantidad | Detalle                                              |
|------------|----------|------------------------------------------------------|
| Categorías | 3        | Bebidas, Postres, Comidas                            |
| Productos  | 6        | Café, Té, Torta de chocolate, Flan, Milanesa, Pizza  |
| Usuarios   | 2        | Gonza Flores (ADMIN), Mayra Arroyo (USUARIO)         |
| Pedidos    | 1        | Pedido de Luis: 2 cafés + 1 torta, pago con tarjeta  |

---

## Funcionalidades

### Épica 1 — Categorías

|Descripción |
|-----------------------|
| Listar categorías activas con id, nombre y descripción |
| Crear categoría validando nombre no vacío y único |
| Editar nombre y/o descripción; re-valida unicidad del nuevo nombre |
| Baja lógica (`eliminado = true`); bloqueada si tiene productos activos |

### Épica 2 — Productos

| Descripción |
|-----------------------|
| Listar productos (todos o filtrados por categoría) |
| Crear producto con validaciones de precio ≥ 0, stock ≥ 0 y categoría existente |
| ditar campos individuales, incluyendo cambio de categoría |
| Baja lógica; el objeto se conserva en memoria para no romper detalles históricos |

### Épica 3 — Usuarios

| Descripción |
|-----------------------|
| Listar usuarios activos con id, nombre, apellido, mail y rol |
| Crear usuario validando mail no vacío y único en el sistema |
| Editar datos; re-valida unicidad del mail si se modifica |
| Baja lógica; los pedidos históricos del usuario se conservan consultables |

### Épica 4 — Pedidos

| Descripción |
|-----------------------|
| Listar pedidos activos (todos o por usuario) con estado, forma de pago y total |
| Crear pedido usando obligatoriamente `addDetallePedido(...)` y la interfaz `Calculable` para `calcularTotal()`; ante cualquier excepción se cancela el pedido completo |
| Actualizar estado y/o forma de pago mediante opciones numéricas |
| Baja lógica del pedido y de todos sus detalles |

---

## Decisiones de diseño

**Patrón Singleton en `DataStore`**
Garantiza que todas las clases del sistema accedan a la misma instancia de las colecciones en memoria, evitando duplicación de datos entre menús.

**Relaciones bidireccionales automáticas**
- `Producto` se registra en su `Categoria` desde el constructor, sin necesidad de llamar a `categoria.agregarProducto(...)` manualmente.
- `Pedido` se registra en su `Usuario` desde el constructor, sin necesidad de llamar a `usuario.agregarPedido(...)` manualmente.

**Interfaz `Calculable`**
`Pedido` implementa `Calculable` y su método `calcularTotal()` es invocado explícitamente al finalizar la carga de detalles en `MenuPedidos`, cumpliendo el contrato definido por la cátedra.

**Baja lógica (`eliminado = true`)**
Ninguna entidad se elimina físicamente de las colecciones. Esto preserva la integridad referencial: un `DetallePedido` puede seguir referenciando un `Producto` dado de baja sin lanzar excepciones.

**Cancelación ante errores en pedidos**
Si durante la carga de detalles ocurre una excepción (por ejemplo, stock insuficiente), el pedido parcialmente construido se descarta de la lista del usuario y no se agrega al `DataStore`, dejando las colecciones en estado consistente.

**`InputHelper` centralizado**
Toda la lectura de datos del usuario pasa por esta clase. Esto evita repetir bloques `try/catch` y validaciones en cada menú.

---

## Autor

Proyecto desarrollado para la **Tecnicatura Universitaria en Programación a Distancia**.  
Autor: Flores Gonzalo
