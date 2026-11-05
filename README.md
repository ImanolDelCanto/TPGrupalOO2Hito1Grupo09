# Epicentro Gourmet

Trabajo práctico de **Orientación a Objetos II** — Licenciatura en Sistemas, UNLa.

Aplicación web para administrar los festivales gastronómicos de Epicentro Gourmet: unidades de venta, personal, platos, pedidos y reportes, con inicio de sesión por roles.

| | |
|---|---|
| **Grupo** | 09 |
| **Comisión** | Turno noche |

| Integrante | Usuario de GitHub |
|---|---|
| Del Canto, Imanol | [ImanolDelCanto](https://github.com/ImanolDelCanto) |
| Baez, Erika | [BzErika](https://github.com/BzErika) |
| Diaz, Enzo | [enzodiaz013](https://github.com/enzodiaz013) — participó en el Hito 1 |

**Diagrama de clases:** [ver en Google Drive](https://drive.google.com/file/d/1EwO5vxwPRNBBZXelgn_nRokPmesBcIGD/view?usp=sharing)

**Tecnologías:** Java 21 · Spring Boot 3.5 · Spring Data JPA (Hibernate) · MySQL 8 · Spring Security 6 con BCrypt · Thymeleaf y Bootstrap 5 · JavaMailSender (Gmail) · OpenPDF

---

## Cómo correrlo

### 1. Requisitos

- **Java 21**
- **MySQL 8**, el Server y Workbench
- **Git**. En Windows usamos la terminal Git Bash.

Maven no hace falta: el proyecto trae su propio `mvnw`.

### 2. Clonar

```bash
git clone https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09.git
cd TPGrupalOO2Hito1Grupo09
```

### 3. Crear la base vacía

En MySQL Workbench:

```sql
CREATE DATABASE bd_epicentro_hito2;
```

Las tablas no se crean a mano: Hibernate las genera al arrancar, a partir de las entidades.

### 4. Variables de entorno

Ninguna contraseña está en el repositorio. `application.yml` las lee de estas variables:

| Variable | Qué es |
|---|---|
| `DB_URL` | URL de conexión a MySQL |
| `DB_USER` | Usuario de MySQL |
| `DB_PASS` | Contraseña de MySQL |
| `MAIL_USER` | Cuenta de Gmail que manda los mails |
| `MAIL_PASS` | **Contraseña de aplicación** de esa cuenta (16 caracteres), no la contraseña normal |

En Git Bash, agregalas al final de `~/.bashrc` (por ejemplo con `nano ~/.bashrc`):

```bash
export DB_URL='jdbc:mysql://127.0.0.1:3306/bd_epicentro_hito2?serverTimezone=America/Argentina/Buenos_Aires'
export DB_USER='root'
export DB_PASS='TU_PASSWORD'
export MAIL_USER='tu_cuenta@gmail.com'
export MAIL_PASS='TU_CONTRASEÑA_DE_APLICACION'
```

Después recargalas con `source ~/.bashrc`.

> **Usá `127.0.0.1`, no `localhost`.** Para MySQL son dos cuentas distintas, y con `localhost` puede rechazar una contraseña correcta.

> Sin las variables de mail la app arranca igual, pero el alta de empleados y el reenvío de acceso no se completan: mandar la clave por mail es parte de la operación.

Si corrés desde el botón Run de un IDE, ese botón no lee `~/.bashrc`. En ese caso cargalas en Windows con `setx` desde PowerShell y reiniciá el IDE.

### 5. Arrancar

```bash
./mvnw spring-boot:run
```

Abrí <http://localhost:8080>. En cada arranque, `data.sql` borra y vuelve a cargar los datos de prueba.

### Usuarios de prueba

| Rol | Email | Contraseña |
|---|---|---|
| Administrador | `admin@epicentro.com` | `admin1234` |
| Responsable | `laura@epicentro.com` | `resp1234` |
| Responsable de un festival que todavía no empezó | `pablo@epicentro.com` | `resp1234` |
| Empleado cajero | `luis@epicentro.com` | `emp1234` |
| Empleada cocinera | `sofia@epicentro.com` | `emp1234` |
| Empleado dado de baja | `baja@epicentro.com` | `emp1234` — no puede entrar |

---

## Roles

| Rol | Qué puede hacer |
|---|---|
| Sin sesión | Ver los festivales y registrar su unidad de venta |
| Administrador | Ver festivales y unidades, los reportes generales y modificar los datos de las unidades |
| Responsable | Cargar el menú y los pedidos de su unidad, administrar su staff y ver los reportes de su unidad |
| Empleado | Ver su liquidación, descargar su recibo en PDF y ver los festivales donde trabajó |
| Empleado cajero | Además, cargar pedidos, cerrar la caja del día y descargar su comprobante en PDF |

Los permisos se controlan en tres lugares: `SecurityConfiguration` exige sesión iniciada, `@PreAuthorize` en cada controller exige el rol, y cada service verifica que el dato pertenezca al usuario logueado.

---

## Casos de uso y pull requests

### Hito 2

| PR | Caso de uso | Integrante |
|---|---|---|
| [#1] | Migración del Hito 1 a Spring Boot: entidades JPA y repositorios | Imanol |
| [#2] | Seguridad con usuarios y roles, login, liquidación de haberes del empleado, página de inicio y datos de prueba | Imanol |
| [#3] | Registro de unidad de venta con su responsable | Erika |
| [#4] | Mail de bienvenida | Erika |
| [#5] | Alta de pedido | Erika |
| [#6] | Alta de platos | Erika |
| [#7] | Arreglo: Lombok con Java 23 o superior | Erika |
| [#8] | Recibo de sueldo en PDF | Imanol |
| [#9] | Pantalla del empleado: sus festivales y si trabaja hoy | Imanol |
| [#10] | Datos de prueba para reportes y cierre de caja | Erika |
| [#11] | ABM de empleados: alta con mail, baja lógica, reactivación y filtros | Imanol |
| [#12] | Reporte de unidades de venta por festival | Erika |
| [#13] | Reporte de unidades con mayor canon | Erika |
| [#14] | Ranking de unidades por recaudación | Erika |
| [#15] | Canon de la unidad del responsable | Erika |
| [#16] | Recaudación del festival | Erika |
| [#17] | Liquidación de haberes del staff | Erika |
| [#18] | Rentabilidad neta de la unidad | Erika |
| [#19] | Plato estrella de la unidad | Erika |
| [#20] | El cajero carga y ve pedidos | Erika |
| [#21] | Cierre de caja del cajero | Erika |
| [#22] | Paginación del staff y reenvío de credenciales *(casos de uso propios)* | Imanol |
| [#23] | Buscador de pedidos por fecha y de platos por nombre | Imanol |
| [#24] | Corrección: plato estrella por unidades vendidas | Erika |
| [#25] | Corrección: el cierre de caja bloquea la carga de pedidos | Erika |
| [#26] | Bloqueo de pedidos y platos cuando el festival no está activo | Erika |
| [#27] | Rentabilidad neta entre dos fechas | Erika |
| [#28] | Mejoras de validaciones y arreglo del login | Erika |
| [#29] | Paginación de pedidos | Erika |
| [#30] | Comprobante de cierre de caja en PDF *(caso de uso propio)* | Erika |
| [#31] | Modificación de unidades de venta por el administrador *(caso de uso propio)* | Erika |

### Casos de uso propios

El enunciado pide, por integrante, dos casos de uso además de los del enunciado.

**Imanol Del Canto**

- **Paginación del staff.** El listado de empleados se pide de a una página: la consulta a la base trae solo esas filas. Se combina con los filtros por apellido y por puesto.
- **Reenvío de credenciales.** La clave del alta se manda una sola vez por mail y en la base queda hasheada, así que no se puede recuperar. Si el empleado la pierde, el responsable le genera una nueva y se la manda. Si el mail falla, la clave no cambia.

**Erika Baez**

- **Comprobante de cierre de caja en PDF** ([#30]). Con la caja ya cerrada, el cajero descarga un PDF con la unidad, la fecha, el cajero, los pedidos del día y el total.
- **Modificación de unidades de venta por el administrador** ([#31]). Desde la pantalla de cada festival, el administrador corrige los datos de una unidad registrada. La superficie, la electricidad y el montaje cambian el canon, así que solo se pueden modificar si el festival todavía no empezó. El código se valida con el formato de su tipo y no puede repetirse.

### Hito 1

El proyecto del Hito 1 (Eclipse con Hibernate) quedó en `referencia-hito1/`, solo para consulta.

| Caso de uso | Integrante |
|---|---|
| Costo salarial mensual por unidad de venta | Imanol |
| Costo operativo por unidad de venta | Imanol |
| Cajeros de turno noche con antigüedad, en Food Trucks | Enzo |
| Rendimiento económico por unidad de venta | Erika |

---

## Estructura

```
src/main/java/com/unla/epicentrogourmet/
├── configuration/      seguridad: qué es público, login y logout
├── controllers/        reciben la URL, llaman al service y eligen la vista
├── services/           interfaces con la lógica de negocio
│   └── implementation/ sus implementaciones
├── repositories/       consultas a la base con Spring Data JPA
├── entities/           el modelo del diagrama de clases
├── dtos/               datos para formularios y reportes, sin tabla
├── components/         factories de unidades de venta y de personal
└── helpers/            ViewRouteHelper: el nombre de cada vista

src/main/resources/
├── templates/          vistas Thymeleaf
├── application.yml     configuración; lee las variables de entorno
└── data.sql            datos de prueba
```

[#1]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/1
[#2]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/2
[#3]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/3
[#4]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/4
[#5]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/5
[#6]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/6
[#7]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/7
[#8]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/8
[#9]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/9
[#10]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/10
[#11]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/11
[#12]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/12
[#13]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/13
[#14]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/14
[#15]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/15
[#16]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/16
[#17]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/17
[#18]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/18
[#19]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/19
[#20]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/20
[#21]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/21
[#22]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/22
[#23]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/23
[#24]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/24
[#25]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/25
[#26]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/26
[#27]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/27
[#28]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/28
[#29]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/29
[#30]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/30
[#31]: https://github.com/ImanolDelCanto/TPGrupalOO2Hito1Grupo09/pull/31
