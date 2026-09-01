package datos;

// provisorio: la version definitiva la hace otro integrante
public class FoodTruck extends UnidadDeVenta {

	private String patente;
	private boolean requiereElectricidad;

	public FoodTruck() {
	}

	public FoodTruck(String nombreComercial, double superficie, String codigoUnico,
			String patente, boolean requiereElectricidad) {
		super(nombreComercial, superficie, codigoUnico);
		this.patente = patente;
		this.requiereElectricidad = requiereElectricidad;
	}

	public String getPatente() { return patente; }
	public void setPatente(String patente) { this.patente = patente; }

	public boolean isRequiereElectricidad() { return requiereElectricidad; }
	public void setRequiereElectricidad(boolean r) { this.requiereElectricidad = r; }

	@Override
	public String toString() {
		return "FoodTruck " + super.toString() + " - patente " + patente;
	}
}
