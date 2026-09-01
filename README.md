# Epicentro Gourmet — Hito 1

Trabajo práctico de **Orientación a Objetos II** — Licenciatura en Sistemas, UNLa.

Sistema de gestión para los festivales gastronómicos de Epicentro Gourmet: unidades de
venta, personal, platos y pedidos, con persistencia en MySQL a través de Hibernate.

| | |
|---|---|
| **Grupo** | 09 |
| **Comisión** | Turno noche |
| **Entrega** | 3 de septiembre de 2026 |

## Integrantes

| Apellido y Nombre | Usuario GitHub |
|---|---|
| Del Canto, Imanol | ImanolDelCanto |
| *completar* | *completar* |
| *completar* | *completar* |
| *completar* | *completar* |

## Casos de uso

| Caso de uso | Clase | Responsable |
|---|---|---|
| Costo salarial mensual por unidad de venta | `test/CasoDeUso_CostoSalarialPorUnidad` | Del Canto, Imanol |
| *pendiente* | | |
| *pendiente* | | |
| *pendiente* | | |

---

## Requisitos

| Herramienta | Versión | Para qué |
|---|---|---|
| JDK | 21 (probado también en 11 y 17) | compilar y ejecutar |
| MySQL Server | 8.0.46 | la base de datos |
| MySQL Workbench | 8.0 CE | ver y consultar la base |
| Eclipse IDE for Java Developers | 2025-06 | el entorno |
| Librerías Hibernate | 5.4.11.Final (19 JAR) | ORM y driver, provistas por la cátedra |

Los 19 JAR **no están en el repositorio**: son los que provee la cátedra junto con los
apuntes de Hibernate. Hay que descargarlos del campus y dejarlos en una carpeta local.

> **Autenticación de MySQL.** Al instalar el servidor hay que elegir
> *Use Legacy Authentication Method* (`mysql_native_password`). El Connector/J 8.0.19
> que provee la cátedra es de 2020 y con `caching_sha2_password` exige parámetros
> extra en la URL; sin ellos falla con `Public Key Retrieval is not allowed`.

---

## Puesta en marcha

### 1. Crear la base de datos

En MySQL Workbench, abrir una pestaña de consulta y ejecutar:

```sql
CREATE DATABASE bd_epicentro_gourmet;
```

Las **tablas no se crean a mano**: las genera Hibernate al levantar la SessionFactory,
leyendo los archivos de mapeo. Así el esquema no puede quedar desincronizado del modelo.

### 2. Configurar el proyecto en Eclipse

1. `File → Import → General → Existing Projects into Workspace` y elegir esta carpeta.
2. Clic derecho en el proyecto → `Properties → Java Build Path → Libraries`.
3. Seleccionar **`Classpath`** (no *Modulepath*) → `Add External JARs...`
4. Agregar los **19 archivos `.jar`** de la cátedra.

> **Importante para quien clone el repo:** el archivo `.classpath` guarda rutas
> absolutas de la máquina donde se creó el proyecto. Al importar van a aparecer errores
> de compilación: hay que rehacer el paso 3 apuntando a la ruta local de los JAR.

También conviene poner el workspace en UTF-8, en
`Window → Preferences → General → Workspace → Text file encoding`. Sin eso, los
comentarios con acentos rompen la compilación con `unmappable character for encoding`.

### 3. Ajustar la contraseña

En `src/hibernate.cfg.xml`, poner la contraseña del usuario `root` de MySQL:

```xml
<property name="connection.password">root</property>
```

---

## Orden de ejecución

| # | Clase | Qué hace | Se corre |
|---|---|---|---|
| 1 | `test/TestConexion` | Verifica la conexión y genera el esquema desde los mapeos. | las veces que quieras |
| 2 | `test/CargarUnidadesYStaff` | Carga 2 unidades de venta y 5 empleados asignados a ellas. | **una sola vez** |
| 3 | `test/CasoDeUso_CostoSalarialPorUnidad` | El caso de uso. | las veces que quieras |

Para ejecutar: clic derecho sobre la clase → `Run As → Java Application`, o **`Ctrl+F11`**
con el archivo abierto.

### Qué esperar

**1 · TestConexion** — al final de las líneas `INFO`:

```
=========================================
            CONEXION OK
=========================================
  UnidadDeVenta : 2
  Personal      : 5
  Cocinero      : 3
  Cajero        : 2
=========================================
```

La primera vez los cuatro dan 0: las tablas se acaban de crear y están vacías.

**2 · CargarUnidadesYStaff**

```
Datos cargados: 2 unidades y 5 empleados
```

**Correrlo dos veces falla** con `Duplicate entry`. No es un error del programa: las
restricciones `unique` sobre el DNI y el código único están declaradas en los mapeos y
MySQL las hace cumplir. Para volver a cargar hay que vaciar las tablas primero:

```sql
USE bd_epicentro_gourmet;
SET SQL_SAFE_UPDATES = 0;
UPDATE unidaddeventa SET idResponsable = NULL;
DELETE FROM cocinero;  DELETE FROM cajero;  DELETE FROM personal;
DELETE FROM foodtruck; DELETE FROM puestodesarmable; DELETE FROM unidaddeventa;
SET SQL_SAFE_UPDATES = 1;
```

El `UPDATE` va primero: rompe la referencia circular entre la unidad y su responsable.
Después, las hijas antes que los padres, por las claves foráneas.

**3 · CasoDeUso_CostoSalarialPorUnidad**

```
=== COSTO SALARIAL MENSUAL POR UNIDAD DE VENTA ===

Puesto Empanadas del Norte [PD00000001] - 40.0 m2 - 3 carpas
  responsable: Cocinero Gomez, Lucia - DNI 33444555 - ingreso 2021-07-15 (5 anios) - Pasteleria
  staff: 2 empleados
    Cocinero Gomez, Lucia ... cobra 900000,00
    Cajero Sosa, Pedro ... cobra 690000,00
  COSTO SALARIAL: 1590000,00

FoodTruck La Parrilla Rodante [FT00000001] - 25.5 m2 - patente AB123CD
  ...
  COSTO SALARIAL: 2490000,00

=== RESUMEN ===
Costo salarial total del predio: 4080000,00
Unidad mas costosa: La Parrilla Rodante (2490000,00)
```

---

## Estructura del proyecto

```
EpicentroGourmet/
└── src/
    ├── hibernate.cfg.xml     conexión a la BD + lista de mapeos
    ├── datos/                las clases del modelo (POJOs)
    ├── mapeos/               un .hbm.xml por jerarquía
    ├── dao/                  acceso a datos + HibernateUtil
    ├── negocio/              clases ABM: reglas de negocio
    └── test/                 un main por caso de uso
```

El flujo va siempre en un sentido:

```
test  →  negocio (ABM)  →  dao  →  Hibernate  →  MySQL
```

Una clase de `test` nunca llama directo a un `Dao`, y un `Dao` nunca llama a un `ABM`.
El `ABM` decide **si se puede hacer**; el `Dao` sabe **cómo se guarda**.

---

## Decisiones de modelado

**Herencia con `<joined-subclass>`** — una tabla por clase. `cocinero` y `cajero`
guardan solo sus atributos propios más una columna que es a la vez clave primaria y
foránea hacia `personal`. No repite columnas ni deja `NULL` innecesarios; el costo es
un `JOIN` por consulta.

**El costo salarial se calcula en Java, no con `SUM` en la consulta** — `getSueldoTotal()`
es abstracto en `Personal`: el cocinero suma su plus por categoría y el cajero no. El
método `getCostoSalarial()` recorre el staff sin preguntar de qué tipo es cada empleado.
Agregar un rol nuevo no obliga a tocar ese método.

**`Costos` no es una clase** — sus cuatro atributos están directamente en `Festival`.
Mantiene el modelo dentro de las 10 clases que pide el enunciado y evita mapear un
`<component>`, que no forma parte del material de la cátedra. La tabla resultante es
idéntica.

**`Pedido` y `ItemPedido` son una composición** — un ítem no existe fuera de su pedido.

**`idResponsable` es nullable** — hay una dependencia circular entre `UnidadDeVenta` y
`Personal`: la unidad necesita un responsable que es parte de su staff, y el staff
necesita que la unidad exista. Se resuelve cargando en tres pasos: primero la unidad
sin responsable, después el personal, y por último un `update` de la unidad.

---

## Estado

| | |
|---|---|
| Modelo de clases | ✅ 10 clases |
| Entorno y conexión | ✅ |
| Herencia `Personal → Cocinero / Cajero` | ✅ mapeada, con datos |
| Uno a muchos `UnidadDeVenta → Personal` | ✅ mapeada, con datos |
| Capas DAO y ABM | ✅ para `Personal` y `UnidadDeVenta` |
| Caso de uso — costo salarial por unidad | ✅ |
| Clases `Festival`, `Plato`, `Pedido`, `ItemPedido` | ⬜ pendientes |
| Casos de uso 2, 3 y 4 | ⬜ pendientes |

Las clases `UnidadDeVenta`, `FoodTruck` y `PuestoDesarmable` están en una versión
provisoria, para poder desarrollar el caso de uso en paralelo.
