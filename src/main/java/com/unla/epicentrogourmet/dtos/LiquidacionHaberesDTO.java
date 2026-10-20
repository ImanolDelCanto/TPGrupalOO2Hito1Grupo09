package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;

// DTO: no persiste, no tiene tabla, no tiene relaciones.
// Es la forma en que viaja el resultado de un calculo hasta la vista.
public class LiquidacionHaberesDTO {

	private String personal;
	private String rol;
	private String festival;
	private int diasFestival;
	private BigDecimal sueldoBase;
	private BigDecimal plus;
	private BigDecimal antiguedad;
	private BigDecimal totalACobrar;

	public LiquidacionHaberesDTO() {
	}

	public String getPersonal() { return personal; }
	public void setPersonal(String personal) { this.personal = personal; }

	public String getRol() { return rol; }
	public void setRol(String rol) { this.rol = rol; }

	public String getFestival() { return festival; }
	public void setFestival(String festival) { this.festival = festival; }

	public int getDiasFestival() { return diasFestival; }
	public void setDiasFestival(int diasFestival) { this.diasFestival = diasFestival; }

	public BigDecimal getSueldoBase() { return sueldoBase; }
	public void setSueldoBase(BigDecimal sueldoBase) { this.sueldoBase = sueldoBase; }

	public BigDecimal getPlus() { return plus; }
	public void setPlus(BigDecimal plus) { this.plus = plus; }

	public BigDecimal getAntiguedad() { return antiguedad; }
	public void setAntiguedad(BigDecimal antiguedad) { this.antiguedad = antiguedad; }

	public BigDecimal getTotalACobrar() { return totalACobrar; }
	public void setTotalACobrar(BigDecimal totalACobrar) { this.totalACobrar = totalACobrar; }
}
