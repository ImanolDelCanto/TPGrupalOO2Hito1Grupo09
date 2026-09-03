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

	// Ocupa superficie y, si necesita electricidad, paga el plus del festival.
	@Override
	public double getCostoOperativo() {
		double costo = getSuperficie() * getFestival().getCostoPorSuperficie();
		if (requiereElectricidad)
			costo += getFestival().getPlusElectricidad();
		return costo;
	}

	@Override
	public String toString() {
		return "FoodTruck [" + super.toString() + ", patente=" + patente + ", requiereElectricidad=" + requiereElectricidad + "]";
	}
	
	
}
