----------- DIAGRAMA DE CLASES -----------
<-- https://drive.google.com/file/d/1EwO5vxwPRNBBZXelgn_nRokPmesBcIGD/view?usp=sharing -->


# Epicentro Gourmet — Hito 1

Trabajo práctico de **Orientación a Objetos II** — Licenciatura en Sistemas, UNLa.

Sistema de gestión para los festivales gastronómicos de Epicentro Gourmet: unidades de
venta, personal, platos y pedidos, con persistencia en MySQL a través de Hibernate.

| | |
|---|---|
| **Grupo** | 09 |
| **Integrantes** | Enzo Diaz · Imanol Del Canto · Erika Baez |
| **Comisión** | Turno noche |
| **Entrega** | 3 de septiembre de 2026 |

| Apellido y Nombre | Usuario GitHub |
|---|---|
| Diaz, Enzo | 
| Del Canto, Imanol | ImanolDelCanto |
| Baez, Erika | 

## Casos de uso

| Caso de uso | Clase | Responsable |
|---|---|---|
| Costo salarial mensual por unidad de venta | Del Canto, Imanol |
| Cajeros de turno noche con antigüedad, en Food Trucks | Diaz, Enzo |
| Rendimiento económico por unidad de venta | Baez, Erika |
| Costo operativo por unidad de venta | Del Canto, Imanol |

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

> **Importante para quien clone el repo:** el archivo `.classpath` guarda rutas
> absolutas de la máquina donde se creó el proyecto. Al importar van a aparecer errores
> de compilación: hay que rehacer el paso 3 apuntando a la ruta local de los JAR.


### 3. Ajustar la contraseña

En `src/hibernate.cfg.xml`, poner la contraseña del usuario `root` de MySQL:

```xml
<property name="connection.password">root</property>
```

---

## Orden de ejecución

Los tests **se corren en este orden**. Cada uno asume que los anteriores ya pasaron.

| # | Clase | Qué hace | Se corre |
|---|---|---|---|
| 1 | `test/TestConexion` | Verifica la conexión y genera el esquema desde los mapeos. | las veces que quieras |
| 2 | `test/CargarUnidadesYStaff` | Carga 1 festival, 4 unidades de venta y 8 empleados asignados a ellas. | **una sola vez** |
| 3 | `test/CargarPedidos` | Carga 8 platos y 5 pedidos con sus ítems. Necesita el paso 2. | **una sola vez** |
| 4 | `test/CasoDeUso_CostoSalarialPorUnidad` | Caso de uso. | las veces que quieras |
| 5 | `test/CasoDeUso_CostoOperativoPorUnidad` | Caso de uso. | las veces que quieras |
| 6 | `test/CasoDeUso_CajerosAntiguosFoodTruck` | Caso de uso. | las veces que quieras |
| 7 | `test/CasoDeUso_RendimientoPorUnidad` | Caso de uso. | las veces que quieras |


### Qué esperar en cada paso

**1 · TestConexion** — al final de las líneas `INFO`:

```
=========================================
            CONEXION OK
=========================================
  UnidadDeVenta : 4
  Personal      : 8
  Cocinero      : 4
  Cajero        : 4
  Festival      : 1
  Plato         : 8
  Pedido        : 5
  ItemPedido    : 9
=========================================
```

La primera vez dan todos 0: las tablas se acaban de crear y están vacías. Se generan
diez: `festival`, `unidadDeVenta`, `foodTruck`, `puestoDesarmable`, `personal`, `cocinero`,
`cajero`, `plato`, `pedido` e `itemPedido`.

**2 · CargarUnidadesYStaff**

```
Datos cargados: 1 festival, 4 unidades y 8 empleados
```

El orden importa y está forzado por dos restricciones: una unidad no puede existir sin
festival (`not-null`), y el responsable de una unidad es parte de su propio staff, así que
todavía no existe cuando la unidad se guarda. Por eso va festival → unidades sin
responsable → personal → `update` de la unidad con su responsable.

**Correrlo dos veces falla** con `Duplicate entry`. No es un error del programa: las
restricciones `unique` sobre el DNI, el código único y la patente están declaradas en los
mapeos y MySQL las hace cumplir. Para volver a cargar, lo más simple es rehacer la base:

```sql
DROP DATABASE bd_epicentro_gourmet;
CREATE DATABASE bd_epicentro_gourmet;
```

Y volver a correr `TestConexion`, que regenera el esquema. Si preferís conservar la base,
hay que vaciar las tablas en orden.

```sql
USE bd_epicentro_gourmet;
SET SQL_SAFE_UPDATES = 0;
UPDATE unidadDeVenta SET idResponsable = NULL;
DELETE FROM itemPedido;  DELETE FROM pedido;  DELETE FROM plato;
DELETE FROM cocinero;    DELETE FROM cajero;  DELETE FROM personal;
DELETE FROM foodTruck;   DELETE FROM puestoDesarmable;
DELETE FROM unidadDeVenta;
DELETE FROM festival;
SET SQL_SAFE_UPDATES = 1;
```

**3 · CargarPedidos**

```
Datos cargados: 8 platos y 5 pedidos
```

**4 · CasoDeUso_CostoSalarialPorUnidad**

```
=== COSTO SALARIAL MENSUAL POR UNIDAD DE VENTA ===

FoodTruck [UnidadDeVenta [idUnidad=1, nombreComercial=La Parrilla Rodante, ...], patente=AB123CD, requiereElectricidad=true]
  responsable: Cocinero Perez, Juan - DNI 30111222 - ingreso 2019-03-01 (7 anios) - Parrilla
  staff: 3 empleados
    Cajero [Lopez, Ana - ... , turno=noche, recaudacion=2000.0] cobra 700000,00
    Cocinero Diaz, Marcos - ... - Parrilla cobra 840000,00
    Cocinero Perez, Juan - ... - Parrilla cobra 950000,00
  COSTO SALARIAL: 2490000,00
...

=== RESUMEN ===
Costo salarial total del predio: 6420000,00
Unidad mas costosa: La Parrilla Rodante (2490000,00)
```

**5 · CasoDeUso_CostoOperativoPorUnidad**

```
=== COSTO OPERATIVO POR UNIDAD DE VENTA ===

Sabores de Verano
  superficie: 1500,00 por m2   montaje: 800,00 por minuto   electricidad: 12000,00
    Cerveza Artesanal             20,00 m2       54000,00
    La Parrilla Rodante           25,50 m2       50250,00
    Sushi al Paso                 18,00 m2       39000,00
    Empanadas del Norte           40,00 m2      132000,00
  COSTO OPERATIVO DEL FESTIVAL: 275250,00
  La mas cara de operar: Empanadas del Norte (132000,00)
```

**6 · CasoDeUso_CajerosAntiguosFoodTruck**

```
=== CAJEROS DE TURNO NOCHE CON MAS DE 2 ANIOS, EN FOOD TRUCKS ===

Cajero [Lopez, Ana - DNI 32444555 - ingreso 2022-06-15 (4 anios), turno=noche, recaudacion=2000.0]
   unidad: FoodTruck [UnidadDeVenta [idUnidad=1, nombreComercial=La Parrilla Rodante, ...], patente=AB123CD, ...]

Total: 1 cajeros
```

**7 · CasoDeUso_RendimientoPorUnidad**

```
=== RENDIMIENTO ECONOMICO POR UNIDAD DE VENTA ===

PuestoDesarmable [UnidadDeVenta [idUnidad=4, nombreComercial=Cerveza Artesanal, ...], cantidadCarpas=1, tiempoMontaje=30]
  tipo: PuestoDesarmable (carpas: 1, tiempo de montaje: 30 min)
  cantidad de pedidos: 1
  FACTURACION: 104000,00
  MARGEN: 65000,00
...

=== RESUMEN DEL PREDIO ===
Facturacion total: 289200,00
Margen total: 170600,00
Unidad mas rentable: Cerveza Artesanal (PuestoDesarmable) con margen 65000,00
```

---

## Estructura del proyecto


## Estructura del proyecto

```
EpicentroGourmet/
└── src/
    ├── hibernate.cfg.xml     conexión a la BD + lista de mapeos
    ├── datos/                las 10 clases del modelo 
    ├── mapeos/               un .hbm.xml por jerarquía
    ├── dao/                  acceso a datos + HibernateUtil
    ├── negocio/              clases ABM: reglas de negocio
    └── test/                 un main por caso de uso
```

## Decisiones de modelado

**Herencia con `<joined-subclass>`** — una tabla por clase, en las dos jerarquías
(`Personal` y `UnidadDeVenta`). Las hijas guardan solo sus atributos propios más una
columna que es a la vez clave primaria y foránea hacia el padre. No repite columnas ni
deja `NULL` innecesarios; el costo es un `JOIN` por consulta.

**El costo salarial se calcula en Java, no con `SUM` en la consulta** — `getSueldoTotal()`
es abstracto en `Personal`: el cocinero suma su plus por categoría y el cajero no. El
método `getCostoSalarial()` recorre el staff sin preguntar de qué tipo es cada empleado.
Agregar un rol nuevo no obliga a tocar ese método.

**`Costos` no es una clase** — sus cuatro atributos están directamente en `Festival`.
Mantiene el modelo dentro de las 10 clases que pide el enunciado.

**`idResponsable` es nullable** — hay una dependencia circular entre `UnidadDeVenta` y
`Personal`: la unidad necesita un responsable que es parte de su staff, y el staff
necesita que la unidad exista. Se resuelve cargando en tres pasos: primero la unidad sin
responsable, después el personal, y por último un `update` de la unidad.

**El dueño de cada relación está declarado con `inverse="true"`** — la FK `idUnidad` vive
en `personal` y en `plato`, la `idFestival` en `unidadDeVenta`. El lado marcado `inverse`
no escribe la columna, solo la lee. Si los dos lados quedaran sin `inverse`, ambos
intentarían escribirla y sobrarían `UPDATE`s.

---
