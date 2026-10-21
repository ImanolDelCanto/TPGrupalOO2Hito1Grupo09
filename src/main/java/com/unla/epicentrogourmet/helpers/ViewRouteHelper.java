package com.unla.epicentrogourmet.helpers;

// Como en el repo de la catedra: todas las rutas de vistas en un solo lugar.
// Si se mueve una plantilla, se cambia aca y no en cada controller.
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

	// RESPONSABLE
	public final static String RESPONSABLE_INDEX = "responsable/index";

	// EMPLEADO
	public final static String EMPLEADO_INDEX = "empleado/index";

	/**** Redirects ****/
	public final static String ROUTE = "/index";
	public final static String ADMIN_ROOT = "/admin";
	public final static String RESPONSABLE_ROOT = "/responsable";
	public final static String EMPLEADO_ROOT = "/empleado";
}
