package datos;

// provisorio: la version definitiva la hace otro integrante
public class PuestoDesarmable extends UnidadDeVenta {

	private int cantidadCarpas;
	private int tiempoMontaje;

	public PuestoDesarmable() {
	}

	public PuestoDesarmable(String nombreComercial, double superficie, String codigoUnico,
			int cantidadCarpas, int tiempoMontaje) {
		super(nombreComercial, superficie, codigoUnico);
		this.cantidadCarpas = cantidadCarpas;
		this.tiempoMontaje = tiempoMontaje;
	}

	public int getCantidadCarpas() { return cantidadCarpas; }
	public void setCantidadCarpas(int c) { this.cantidadCarpas = c; }

	public int getTiempoMontaje() { return tiempoMontaje; }
	public void setTiempoMontaje(int t) { this.tiempoMontaje = t; }

	@Override
	public String toString() {
		return "Puesto " + super.toString() + " - " + cantidadCarpas + " carpas";
	}
}
