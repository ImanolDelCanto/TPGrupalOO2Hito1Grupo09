package negocio;

import java.util.List;

import dao.UnidadDeVentaDao;
import datos.PuestoDesarmable;
import datos.UnidadDeVenta;

public class UnidadDeVentaABM {

	private UnidadDeVentaDao dao = new UnidadDeVentaDao();

	public long agregar(UnidadDeVenta unidad) {
		if (!unidad.validarCodigo())
			throw new IllegalArgumentException(
					"El codigo unico debe tener 10 caracteres: " + unidad.getCodigoUnico());
		return dao.agregar(unidad);
	}

	public void actualizar(UnidadDeVenta unidad) {
		dao.actualizar(unidad);
	}

	public UnidadDeVenta traerUnidadYStaff(long idUnidad) {
		return dao.traerUnidadYStaff(idUnidad);
	}

	// El Dao ya trae solo lo que corresponde; el ABM solo valida la entrada.
	public List<PuestoDesarmable> traerPuestosPorTiempoDeMontaje(String festival, int minutos) {
		if (minutos <= 0)
			throw new IllegalArgumentException("El tiempo de montaje tiene que ser mayor a cero");
		return dao.traerPuestosPorTiempoDeMontaje(festival, minutos);
	}

	public List<UnidadDeVenta> traerTodasConStaff() {
		return dao.traerTodasConStaff();
	}
	
	public List<UnidadDeVenta> traerTodasConPedidos() {
	    return dao.traerTodasConPedidos();
	}
}
