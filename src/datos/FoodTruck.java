package datos;

public class FoodTruck extends UnidadDeVenta{
	private String patente;
	private boolean requiereElectricidad;
	
	public FoodTruck() {
	}

	public FoodTruck(String nombreComercial, double superficie, String codigoUnico, Festival festival,
			Personal responsable, String patente, boolean requiereElectricidad) {
		super(nombreComercial, superficie, codigoUnico, festival, responsable);
		this.patente = patente;
		this.requiereElectricidad = requiereElectricidad;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public boolean isRequiereElectricidad() {
		return requiereElectricidad;
	}

	public void setRequiereElectricidad(boolean requiereElectricidad) {
		this.requiereElectricidad = requiereElectricidad;
	}

	@Override
	public String toString() {
		return "FoodTruck [patente=" + patente + ", requiereElectricidad=" + requiereElectricidad + "]";
	}
	
	
}
