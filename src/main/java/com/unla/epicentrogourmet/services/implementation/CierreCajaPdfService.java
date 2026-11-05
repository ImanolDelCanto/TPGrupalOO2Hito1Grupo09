package com.unla.epicentrogourmet.services.implementation;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
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

import com.unla.epicentrogourmet.dtos.CierreCajaDTO;
import com.unla.epicentrogourmet.entities.ItemPedido;
import com.unla.epicentrogourmet.entities.Pedido;
import com.unla.epicentrogourmet.services.ICierreCajaPdfService;

// Arma el comprobante del cierre de caja en PDF con OpenPDF.
// Igual que el recibo: no consulta la base, recibe el DTO que ya armo
// PedidoService. Aca solo se dibuja.
@Service("cierreCajaPdfService")
public class CierreCajaPdfService implements ICierreCajaPdfService {

	private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
	private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10);
	private static final Font ETIQUETA = FontFactory.getFont(FontFactory.HELVETICA, 11);
	private static final Font VALOR = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
	private static final Font TOTAL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
	private static final Font NOTA = FontFactory.getFont(FontFactory.HELVETICA, 8);

	@Override
	public byte[] generar(CierreCajaDTO cierre, String emitidoPor) {
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		Document documento = new Document(PageSize.A4, 56, 56, 56, 56);

		try {
			PdfWriter.getInstance(documento, salida);
			documento.open();

			agregarEncabezado(documento);
			agregarDatosDelCierre(documento, cierre, emitidoPor);
			agregarPedidos(documento, cierre);
			agregarTotal(documento, cierre);
			agregarPie(documento);

			documento.close();
		} catch (DocumentException e) {
			throw new IllegalStateException("No se pudo generar el cierre de caja en PDF", e);
		}

		return salida.toByteArray();
	}

	// ---------- las partes del comprobante ----------

	private void agregarEncabezado(Document documento) throws DocumentException {
		Paragraph titulo = new Paragraph("Epicentro Gourmet", TITULO);
		titulo.setAlignment(Element.ALIGN_CENTER);
		documento.add(titulo);

		Paragraph subtitulo = new Paragraph("Cierre de caja", SUBTITULO);
		subtitulo.setAlignment(Element.ALIGN_CENTER);
		subtitulo.setSpacingAfter(24);
		documento.add(subtitulo);
	}

	private void agregarDatosDelCierre(Document documento, CierreCajaDTO cierre, String emitidoPor)
			throws DocumentException {
		PdfPTable datos = new PdfPTable(2);
		datos.setWidthPercentage(100);
		datos.setSpacingAfter(20);

		datos.addCell(celda("Unidad de venta", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(cierre.getNombreUnidad(), VALOR, Element.ALIGN_RIGHT));

		datos.addCell(celda("Fecha", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(cierre.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), VALOR,
				Element.ALIGN_RIGHT));

		datos.addCell(celda("Cajero", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(emitidoPor, VALOR, Element.ALIGN_RIGHT));

		datos.addCell(celda("Pedidos del dia", ETIQUETA, Element.ALIGN_LEFT));
		datos.addCell(celda(String.valueOf(cierre.getCantidadPedidos()), VALOR, Element.ALIGN_RIGHT));

		documento.add(datos);
	}

	// la misma tabla que la pantalla: numero, detalle y total de cada pedido
	private void agregarPedidos(Document documento, CierreCajaDTO cierre) throws DocumentException {
		if (cierre.getPedidos().isEmpty()) {
			Paragraph vacio = new Paragraph("Hoy no hubo pedidos.", ETIQUETA);
			vacio.setSpacingAfter(20);
			documento.add(vacio);
			return;
		}

		PdfPTable tabla = new PdfPTable(3);
		tabla.setWidthPercentage(100);
		tabla.setWidths(new float[] { 1, 6, 2 });   // la columna del detalle es la mas ancha
		tabla.setSpacingAfter(20);

		tabla.addCell(celda("#", SUBTITULO, Element.ALIGN_LEFT));
		tabla.addCell(celda("Detalle", SUBTITULO, Element.ALIGN_LEFT));
		tabla.addCell(celda("Total", SUBTITULO, Element.ALIGN_RIGHT));

		for (Pedido pedido : cierre.getPedidos()) {
			tabla.addCell(celda(String.valueOf(pedido.getIdPedido()), ETIQUETA, Element.ALIGN_LEFT));
			tabla.addCell(celda(detalleDe(pedido), ETIQUETA, Element.ALIGN_LEFT));
			tabla.addCell(celda(pesos(pedido.getTotal()), ETIQUETA, Element.ALIGN_RIGHT));
		}

		documento.add(tabla);
	}

	private void agregarTotal(Document documento, CierreCajaDTO cierre) throws DocumentException {
		PdfPTable total = new PdfPTable(2);
		total.setWidthPercentage(100);
		total.setSpacingAfter(24);

		total.addCell(celdaDeTotal("TOTAL RECAUDADO", TOTAL, Element.ALIGN_LEFT));
		total.addCell(celdaDeTotal(pesos(cierre.getTotal()), TOTAL, Element.ALIGN_RIGHT));

		documento.add(total);
	}

	private void agregarPie(Document documento) throws DocumentException {
		Paragraph emision = new Paragraph(
				"Emitido el " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
						+ ". Con la caja cerrada no se cargan mas pedidos en el dia.",
				NOTA);
		documento.add(emision);
	}

	// ---------- ayudantes ----------

	// "2 x Hamburguesa", un plato por renglon
	private String detalleDe(Pedido pedido) {
		StringBuilder detalle = new StringBuilder();
		for (ItemPedido item : pedido.getItems()) {
			if (detalle.length() > 0)
				detalle.append("\n");
			detalle.append(item.getCantidad()).append(" x ").append(item.getPlato().getNombre());
		}
		return detalle.toString();
	}

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
}