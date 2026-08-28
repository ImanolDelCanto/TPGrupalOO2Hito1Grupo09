# Epicentro Gourmet — Hito 1

Trabajo práctico de **Orientación a Objetos II** — Licenciatura en Sistemas, UNLa.

Sistema de gestión para los festivales gastronómicos de Epicentro Gourmet: unidades de
venta, personal, platos y pedidos, con persistencia en MySQL a través de Hibernate.

| | |
|---|---|
| **Grupo** | 9 |
| **Integrantes** | Enzo Diaz · Imanol Del Canto · Erika Baez |
| **Comisión** | Turno noche |
| **Entrega** | 3 de septiembre de 2026 |

---

## Requisitos

| Herramienta | Versión | Para qué |
|---|---|---|
| JDK | 21 | compilar y ejecutar |
| MySQL Server | 8.0.46 | la base de datos |
| MySQL Workbench | 8.0 CE | ver y consultar la base |
| Librerías Hibernate |

---

## Puesta en marcha

### 1. Crear la base de datos

En MySQL Workbench, abrir una pestaña de consulta y ejecutar:

```sql
CREATE DATABASE bd_epicentro_gourmet;
```

Las **tablas no se crean a mano**: las genera Hibernate en el paso 4, leyendo los
archivos de mapeo. Así el esquema no puede quedar desincronizado del modelo.

### 2. Configurar el proyecto en Eclipse

1. `File → Import → General → Existing Projects into Workspace` y elegir esta carpeta.
2. Clic derecho en el proyecto → `Properties → Java Build Path → Libraries`.
3. Seleccionar **`Classpath`** (no *Modulepath*) → `Add External JARs...`
4. Agregar los **19 archivos `.jar`** de la carpeta `lib`.


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
| 1 | `test/TestConexion` | Verifica que Java llegue a MySQL. No toca tablas. | las veces que quieras |
| 2 | `test/TestCrearTablas` | Genera el esquema desde los mapeos y lista las tablas. | las veces que quieras |
| 3 | `test/CargarPersonal` | Inserta 5 empleados de prueba. | **una sola vez** |
| 4 | `test/CasoDeUso_ListarPersonal` | Caso de uso: consulta el personal. | las veces que quieras |

Para ejecutar: clic derecho sobre la clase → `Run As → Java Application`, o **`Ctrl+F11`**
con el archivo abierto.

### Qué esperar en cada paso

**1 · TestConexion** — al final de las líneas `INFO`:

```
=========================================
            CONEXION OK
=========================================
  Servidor MySQL : 8.0.46
  Base de datos  : bd_epicentro_gourmet
=========================================
```

**2 · TestCrearTablas** — muestra el `CREATE TABLE` que Hibernate genera y después:

```
  TABLAS EN LA BASE: 3
  - cajero
  - cocinero
  - personal
```

**3 · CargarPersonal**

```
>>> 5 empleados cargados (3 cocineros, 2 cajeros)
```

**Correrlo dos veces falla** con `Duplicate entry ... for key 'dni'`. No es un error del
programa: la restricción `unique` sobre el DNI está declarada en el mapeo y MySQL la
hace cumplir. Para volver a cargar hay que vaciar las tablas primero:

```sql
DELETE FROM cocinero;
DELETE FROM cajero;
DELETE FROM personal;
```

En ese orden — primero las hijas, después el padre, por las claves foráneas.

**4 · CasoDeUso_ListarPersonal**

```
Personal del festival (5 empleados):
Cocinero [Perez, Juan - DNI 30111222 - ingreso 2019-03-01 (7 anios) - especialidad Parrilla - plus 150000.0]
Cajero [Lopez, Ana - DNI 32444555 - ingreso 2022-06-15 (4 anios) - turno noche]
...

Cocineros de Parrilla (2):
...
```

---

## Estructura del proyecto

```
EpicentroGourmet/
├── src/
│   ├── hibernate.cfg.xml     conexión a la BD + lista de mapeos
│   ├── datos/                las clases del modelo (POJOs)
│   ├── mapeos/               un .hbm.xml por entidad
│   ├── dao/                  acceso a datos + HibernateUtil
│   ├── negocio/              clases ABM: reglas de negocio
│   └── test/                 un main por caso de uso
└── lib/                      los 19 JAR de Hibernate y el driver
```

El flujo va siempre en un sentido:

```
test  →  negocio (ABM)  →  dao  →  Hibernate  →  MySQL
```

Una clase de `test` nunca llama directo a un `Dao`, y un `Dao` nunca llama a un `ABM`.
El `ABM` decide **si se puede hacer**; el `Dao` sabe **cómo se guarda**.

---

## Estado

| | |
|---|---|
| Modelo de clases | ✅ 10 clases |
| Entorno y conexión | ✅ |
| Herencia `Personal → Cocinero / Cajero` | ✅ mapeada, con datos |
| Capas DAO y ABM | ✅ para `Personal` |
| Caso de uso 1 — listar personal | ✅ |
| Relación uno-a-muchos | ⬜ pendiente |
| Clases `Festival`, `UnidadDeVenta`, `Plato`, `Pedido`, `ItemPedido` | ⬜ pendientes |
| Casos de uso 2, 3 y 4 | ⬜ pendientes |
