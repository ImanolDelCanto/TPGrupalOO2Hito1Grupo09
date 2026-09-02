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

	@Override
	public String toString() {
		return "PuestoDesarmable [" + super.toString() + ", cantidadCarpas=" + cantidadCarpas + ", tiempoMontaje=" + tiempoMontaje + "]";
	}
	
	
}
