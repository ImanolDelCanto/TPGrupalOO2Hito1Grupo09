package datos;

public class PuestoDesarmable extends UnidadDeVenta{
	private int cantidadCarpas;
	private int tiempoMontaje;
	
	public PuestoDesarmable() {
	}

	public PuestoDesarmable(String nombreComercial, double superficie, String codigoUnico, Festival festival,
			Personal responsable, int cantidadCarpas, int tiempoMontaje) {
        super(nombreComercial, superficie, codigoUnico, festival, responsable);
		this.cantidadCarpas = cantidadCarpas;
		this.tiempoMontaje = tiempoMontaje;
	}

	public int getCantidadCarpas() {
		return cantidadCarpas;
	}

	public void setCantidadCarpas(int cantidadCarpas) {
		this.cantidadCarpas = cantidadCarpas;
	}

	public int getTiempoMontaje() {
		return tiempoMontaje;
	}

	public void setTiempoMontaje(int tiempoMontaje) {
		this.tiempoMontaje = tiempoMontaje;
	}

	// Ocupa superficie y ademas hay que montarlo: se cobra por minuto de montaje.
	@Override
	public double getCostoOperativo() {
		return getSuperficie() * getFestival().getCostoPorSuperficie()
				+ tiempoMontaje * getFestival().getCostoPorMontaje();
	}

	@Override
	public String getDetalleEspecifico() {
	    return "carpas: " + cantidadCarpas + ", tiempo de montaje: " + tiempoMontaje + " min";
	}
	
	@Override
	public String toString() {
		return "PuestoDesarmable [" + super.toString() + ", cantidadCarpas=" + cantidadCarpas + ", tiempoMontaje=" + tiempoMontaje + "]";
	}
	
	
}
