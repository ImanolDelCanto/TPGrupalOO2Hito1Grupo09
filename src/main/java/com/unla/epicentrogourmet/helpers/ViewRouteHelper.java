package com.unla.epicentrogourmet.helpers;

// Todas las rutas de vistas en un solo lugar
// Si se mueve una plantilla, se cambia aca y no en cada controller
public class ViewRouteHelper {
	/**** Views ****/
	// HOME
	public final static String INDEX = "home/index";

	// USER
	public final static String USER_LOGIN = "user/login";

	// REGISTRO
	public final static String REGISTRO_UNIDAD = "registro/unidad";

	// ADMIN
	public final static String ADMIN_INDEX = "admin/index";

	// REPORTES
	public final static String REPORTE_UNIDADES = "reporte/unidades";
	public final static String REPORTE_MAYOR_CANON = "reporte/mayor-canon";
	public final static String REPORTE_RANKING = "reporte/ranking";
	public final static String REPORTE_CANON = "reporte/canon";
	public final static String REPORTE_RECAUDACION = "reporte/recaudacion";
	public final static String REPORTE_LIQUIDACION = "reporte/liquidacion";
	public final static String REPORTE_RENTABILIDAD = "reporte/rentabilidad";
	public final static String REPORTE_PLATO_ESTRELLA = "reporte/plato-estrella";

	// RESPONSABLE
	public final static String RESPONSABLE_INDEX = "responsable/index";
	public final static String RESPONSABLE_PEDIDO = "responsable/pedido";  //formulario para cargar un pedido
	public final static String RESPONSABLE_PEDIDOS = "responsable/pedidos";  //listado de los pedidos cargados
	public final static String RESPONSABLE_PLATOS = "responsable/platos";   // menu + alta de plato
	
	// CAJERO
	public final static String CAJERO_PEDIDO = "cajero/pedido";      		// formulario para cargar un pedido
	public final static String CAJERO_PEDIDOS = "cajero/pedidos";    		// listado de los pedidos de su unidad
	public final static String CAJERO_CIERRE = "cajero/cierre";     		// resumen de los pedidos del dia

	// PERSONAL (ABM del staff, lo usa el responsable)
	public final static String PERSONAL_LISTA = "personal/lista";
	public final static String PERSONAL_FORMULARIO = "personal/formulario";
	public final static String PERSONAL_EDITAR = "personal/editar";

	// EMPLEADO
	public final static String EMPLEADO_INDEX = "empleado/index";

	/**** Redirects ****/
	public final static String ROUTE = "/index";
	public final static String ADMIN_ROOT = "/admin";
	public final static String RESPONSABLE_ROOT = "/responsable";
	public final static String EMPLEADO_ROOT = "/empleado";
	public final static String PERSONAL_ROOT = "/empleados";
}