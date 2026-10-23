package com.unla.epicentrogourmet.services.implementation;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import com.unla.epicentrogourmet.dtos.LiquidacionHaberesDTO;
import com.unla.epicentrogourmet.services.IReciboPdfService;

// Arma el recibo de sueldo en PDF con OpenPDF.
// No consulta la base: recibe el DTO que ya calculo LiquidacionService.
// Asi la cuenta del sueldo vive en un solo lugar y aca solo se dibuja.
@Service("reciboPdfService")
public class ReciboPdfService implements IReciboPdfService {

	private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
	private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10);
	private static final Font ETIQUETA = FontFactory.getFont(FontFactory.HELVETICA, 11);
	private static final Font VALOR = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
	private static final Font TOTAL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
	private static final Font NOTA = FontFactory.getFont(FontFactory.HELVETICA, 8);

	@Override
	public byte[] generar(LiquidacionHaberesDTO liquidacion) {
		// El PDF se arma en memoria, no en un archivo: lo unico que sale
		// de aca son los bytes.
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		Document documento = new Document(PageSize.A4, 56, 56, 56, 56);

		try {
			PdfWriter.getInstance(documento, salida);
			documento.open();

			agregarEncabezado(documento);
			agregarDatosDelEmpleado(documento, liquidacion);
			agregarDetalleDeHaberes(documento, liquidacion);
			agregarPie(documento);

			documento.close();
		} catch (DocumentException e) {
			// Si el PDF no se puede armar no hay nada para devolver.
			throw new IllegalStateException("No se pudo generar el recibo en PDF", e);
		}

		return salida.toByteArray();
	}

	// ---------- las partes del recibo ----------

	private void agregarEncabezado(Document documento) throws DocumentException {
		Paragraph titulo = new Paragraph("Epicentro Gourmet", TITULO);
		titulo.setAlignment(Element.ALIGN_CENTER);
		documento.add(titulo);

		Paragraph subtitulo = new Paragraph("Recibo de haberes", SUBTITULO);
		subtitulo.setAlignment(Element.ALIGN_CENTER);
		subtitulo.setSpacingAfter(24);
		documento.add(subtitulo);
	}

	private void agregarDatosDelEmpleado(Document documento, LiquidacionHaberesDTO liquidacion)
			throws DocumentException {
		PdfPTable datos = new PdfPTable(2);
		datos.setWidthPercentage(100);
		datos.setSpacingAfter(20);

		datos.addCell(celda("Empleado", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(liquidacion.getPersonal(), VALOR, Element.ALIGN_RIGHT));

		datos.addCell(celda("Puesto", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(liquidacion.getRol(), VALOR, Element.ALIGN_RIGHT));

		datos.addCell(celda("Festival", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(liquidacion.getFestival(), VALOR, Element.ALIGN_RIGHT));

		datos.addCell(celda("Duracion del evento", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(liquidacion.getDiasFestival() + " dias", VALOR, Element.ALIGN_RIGHT));

		documento.add(datos);
	}

	private void agregarDetalleDeHaberes(Document documento, LiquidacionHaberesDTO liquidacion)
			throws DocumentException {
		PdfPTable detalle = new PdfPTable(2);
		detalle.setWidthPercentage(100);
		detalle.setSpacingAfter(24);

		detalle.addCell(celda("Concepto", SUBTITULO, Element.ALIGN_LEFT));
		detalle.addCell(celda("Importe", SUBTITULO, Element.ALIGN_RIGHT));

		detalle.addCell(celda("Sueldo base", ETIQUETA, Element.ALIGN_LEFT));
		detalle.addCell(celda(pesos(liquidacion.getSueldoBase()), VALOR, Element.ALIGN_RIGHT));

		// Las mismas dos condiciones que la pantalla: el cocinero cobra plus
		// por categoria y el cajero cobra antiguedad, nunca los dos.
		if (esMayorACero(liquidacion.getPlus())) {
			detalle.addCell(celda("Plus por categoria", ETIQUETA, Element.ALIGN_LEFT));
			detalle.addCell(celda(pesos(liquidacion.getPlus()), VALOR, Element.ALIGN_RIGHT));
		}
		if (esMayorACero(liquidacion.getAntiguedad())) {
			detalle.addCell(celda("Antiguedad", ETIQUETA, Element.ALIGN_LEFT));
			detalle.addCell(celda(pesos(liquidacion.getAntiguedad()), VALOR, Element.ALIGN_RIGHT));
		}

		detalle.addCell(celdaDeTotal("TOTAL A COBRAR", TOTAL, Element.ALIGN_LEFT));
		detalle.addCell(celdaDeTotal(pesos(liquidacion.getTotalACobrar()), TOTAL, Element.ALIGN_RIGHT));

		documento.add(detalle);
	}

	private void agregarPie(Document documento) throws DocumentException {
		Paragraph formula = new Paragraph(
				"Formula del enunciado: (int(dias / 15) + 1) x 50% x (sueldo base + adicional). "
						+ "Los montos salen de los costos cargados en el festival.",
				NOTA);
		formula.setSpacingAfter(6);
		documento.add(formula);

		Paragraph emision = new Paragraph(
				"Emitido el " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
				NOTA);
		documento.add(emision);
	}

	// ---------- ayudantes ----------

	private PdfPCell celda(String texto, Font fuente, int alineacion) {
		PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
		celda.setBorder(Rectangle.NO_BORDER);
		celda.setHorizontalAlignment(alineacion);
		celda.setPadding(6);
		return celda;
	}

	// igual que la anterior pero con una linea arriba, para separar el total
	private PdfPCell celdaDeTotal(String texto, Font fuente, int alineacion) {
		PdfPCell celda = celda(texto, fuente, alineacion);
		celda.setBorder(Rectangle.TOP);
		celda.setPaddingTop(10);
		return celda;
	}

	private String pesos(BigDecimal monto) {
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		return "$ " + new DecimalFormat("#,##0.00", simbolos).format(monto);
	}

	private boolean esMayorACero(BigDecimal monto) {
		return monto != null && monto.signum() > 0;
	}
}