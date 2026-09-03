package negocio;

import java.util.List;

import dao.UnidadDeVentaDao;
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

	public List<UnidadDeVenta> traerTodasConStaff() {
		return dao.traerTodasConStaff();
	}
	
	public List<UnidadDeVenta> traerTodasConPedidos() {
	    return dao.traerTodasConPedidos();
	}
}
