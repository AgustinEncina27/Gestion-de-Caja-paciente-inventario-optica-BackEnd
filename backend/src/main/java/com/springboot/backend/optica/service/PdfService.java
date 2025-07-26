package com.springboot.backend.optica.service;

import org.springframework.stereotype.Service;

import com.springboot.backend.optica.modelo.CajaMovimiento;
import com.springboot.backend.optica.modelo.DetalleAdicional;
import com.springboot.backend.optica.modelo.DetalleMovimiento;
import com.springboot.backend.optica.modelo.FichaGraduacion;
import com.springboot.backend.optica.modelo.Graduacion;
import com.springboot.backend.optica.modelo.Movimiento;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.time.format.DateTimeFormatter;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Optional;

@Service
public class PdfService implements IPdfService {
		
	@Override
	public byte[] generarReporteMovimientoOptica(Movimiento movimiento) throws IOException {
	    try (PDDocument document = new PDDocument()) {
	        PDPage page = new PDPage();
	        document.addPage(page);

	        double deuda = 0;
	        float x = 40;
	        float y = 750;
	        float spacing = 12;

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("static/images/logo2.png");
	            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "imagen");
	            contentStream.drawImage(pdImage, 430, 680, 100, 100);

	            // Encabezado principal
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
	            escribir(contentStream, x, y, "REPORTE DE MOVIMIENTO");
	            y -= spacing;

	            contentStream.setFont(PDType1Font.HELVETICA, 9);
	            escribir(contentStream, x, y, "Local: " + movimiento.getLocal().getNombre());
	            escribir(contentStream, 300, y, "Fecha: " + movimiento.getFecha().format(formatter));
	            y -= spacing * 1.5;

	            // Datos del Paciente
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	            escribir(contentStream, x, y, "Datos del Paciente:");
	            y -= spacing;

	            contentStream.setFont(PDType1Font.HELVETICA, 9);
	            if (movimiento.getPaciente() != null) {
	                escribir(contentStream, x, y, "Ficha: " + movimiento.getPaciente().getFicha());
	                escribir(contentStream, 150, y, "Paciente: " + safe(movimiento.getPaciente().getNombreCompleto()));
	                escribir(contentStream, 300, y, "Celular: " + safe(movimiento.getPaciente().getCelular()));
	                y -= spacing;
	                
	                escribir(contentStream, x, y, "Médico: " + safe(movimiento.getPaciente().getMedico()));
	                escribir(contentStream, 150, y, "Obra Social: " + safe(movimiento.getPaciente().getObraSocial()));
	                y -= spacing;

	                escribir(contentStream, x, y, "Domicilio: " + safe(movimiento.getPaciente().getDireccion()));
	                y -= spacing * 1.5;
	            }

	            // Ficha de Graduación
	            if (movimiento.getPaciente() != null && movimiento.getPaciente().getHistorialFichas() != null 
	            	    && !movimiento.getPaciente().getHistorialFichas().isEmpty()) {

	            	    Optional<FichaGraduacion> fichaOpt = movimiento.getPaciente().getHistorialFichas().stream()
	            	        .max(Comparator.comparing(FichaGraduacion::getFecha).thenComparing(FichaGraduacion::getId));

	            	    if (fichaOpt.isPresent()) {
	            	        FichaGraduacion ficha = fichaOpt.get();

	            	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	            	        contentStream.beginText();
	            	        contentStream.newLineAtOffset(x, y);
	            	        contentStream.showText("Ficha de Graduación (Fecha: " + (ficha.getFecha() != null ? ficha.getFecha().format(formatter) : "---") + ")");
	            	        contentStream.endText();
	            	        y -= spacing;
	            	        float rightX = 300;

	            	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	            	        escribir(contentStream, x, y, "Medidas Persona");
	            	        escribir(contentStream, rightX, y, "Medidas del armazón");
	            	        y -= spacing;

	            	        contentStream.setFont(PDType1Font.HELVETICA, 9);
	            	        escribir(contentStream, x, y, "D.N.P: Derecho: " + safe(ficha.getDnpDerecho()) + ", Izquierdo: " + safe(ficha.getDnpIzquierdo()));
	            	        escribir(contentStream, rightX, y, "P: " + safe(ficha.getPuente()) + ", D.M: " + safe(ficha.getDiagonalMayor()));
	            	        y -= spacing;

	            	        escribir(contentStream, x, y, "Alt. Película: " + safe(ficha.getAlturaPelicula()));
	            	        escribir(contentStream, rightX, y, "Largo: " + safe(ficha.getLargo()) + ", Altura: " + safe(ficha.getAlturaArmazon()));
	            	        y -= spacing;

	            	        escribir(contentStream, x, y, "Alt.P: Derecho: " + safe(ficha.getAlturaPupilarDerecho()) + ", Izquierdo: " + safe(ficha.getAlturaPupilarIzquierdo()));
	            	        y -= spacing;

	            	        // === GRADUACIÓN - Ojo Derecho ===
	            	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	            	        escribir(contentStream, x, y, "Lejos - Ojo Derecho:");
	            	        escribir(contentStream, rightX, y, "Cerca - Ojo Derecho:");
	            	        y -= spacing;

	            	        Optional<Graduacion> gradLejosDer = ficha.getGraduaciones().stream()
	            	            .filter(g -> g.getOjo() == Graduacion.Ojo.DERECHO && g.getTipo() == Graduacion.TipoGraduacion.LEJOS)
	            	            .findFirst();

	            	        Optional<Graduacion> gradCercaDer = ficha.getGraduaciones().stream()
	            	            .filter(g -> g.getOjo() == Graduacion.Ojo.DERECHO && g.getTipo() == Graduacion.TipoGraduacion.CERCA)
	            	            .findFirst();

	            	        contentStream.setFont(PDType1Font.HELVETICA, 9);
	            	        escribir(contentStream, x, y, generarTextoGraduacionSimple(gradLejosDer));
	            	        escribir(contentStream, rightX, y, generarTextoGraduacionConAdicion(gradCercaDer, ficha.getAdicionDerecho()));
	            	        y -= spacing;

	            	        // === GRADUACIÓN - Ojo Izquierdo ===
	            	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	            	        escribir(contentStream, x, y, "Lejos - Ojo Izquierdo:");
	            	        escribir(contentStream, rightX, y, "Cerca - Ojo Izquierdo:");
	            	        y -= spacing;

	            	        Optional<Graduacion> gradLejosIzq = ficha.getGraduaciones().stream()
	            	            .filter(g -> g.getOjo() == Graduacion.Ojo.IZQUIERDO && g.getTipo() == Graduacion.TipoGraduacion.LEJOS)
	            	            .findFirst();

	            	        Optional<Graduacion> gradCercaIzq = ficha.getGraduaciones().stream()
	            	            .filter(g -> g.getOjo() == Graduacion.Ojo.IZQUIERDO && g.getTipo() == Graduacion.TipoGraduacion.CERCA)
	            	            .findFirst();

	            	        contentStream.setFont(PDType1Font.HELVETICA, 9);
	            	        escribir(contentStream, x, y, generarTextoGraduacionSimple(gradLejosIzq));
	            	        escribir(contentStream, rightX, y, generarTextoGraduacionConAdicion(gradCercaIzq, ficha.getAdicionIzquierdo()));
	            	        y -= spacing * 2;
	            	    }
	            	}

	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	            escribir(contentStream, x, y, "Detalle de la Compra:");
	            escribir(contentStream, 300, y, "Adicionales:");
	            y -= spacing;

	            // Detalles normales
	            contentStream.setFont(PDType1Font.HELVETICA, 9);
	            float yDetalles = y;
	            for (DetalleMovimiento d : movimiento.getDetalles()) {
	                escribir(contentStream, x, yDetalles, d.getProducto().getModelo() + " - Marca: " + d.getProducto().getMarca().getNombre() 
	                    + " - Cant: " + d.getCantidad() + " - $" + d.getSubtotal());
	                yDetalles -= spacing;
	            }

	            // Detalles adicionales
	            float yAdicionales = y;
	            for (DetalleAdicional d : movimiento.getDetallesAdicionales()) {
	                escribir(contentStream, 300, yAdicionales, d.getDescripcion() + " - $" + d.getSubtotal());
	                yAdicionales -= spacing;
	            }

	            // Ajuste el Y final al más bajo de ambos
	            y = Math.min(yDetalles, yAdicionales) - spacing / 2;

		        // Pagos realizados (controlamos altura fija)
	            if (!movimiento.getCajaMovimientos().isEmpty()) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	                escribir(contentStream, x, y, "Pagos:");
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
	                escribir(contentStream, 300, y, "Totales:");
	                y -= spacing;

	                contentStream.setFont(PDType1Font.HELVETICA, 9);

	                // Guardamos la Y inicial de totales
	                float yTotales = y;

	                for (CajaMovimiento p : movimiento.getCajaMovimientos()) {
	                    escribir(contentStream, x, y, p.getMetodoPago().getNombre() + ": " + p.getMonto());
	                    y -= spacing;
	                    deuda += p.getMonto();
	                }

	                // Ahora imprimimos totales a la derecha exactamente a la misma altura
	                deuda = movimiento.getTotal() - deuda;

	                escribir(contentStream, 300, yTotales, "Total: " + movimiento.getTotal());
	                yTotales -= spacing;

	                if (movimiento.getDescuento() != null) {
	                    escribir(contentStream, 300, yTotales, "Total c/ descuento: " + movimiento.getTotal() + " (" + movimiento.getDescuento() + "%)");
	                    yTotales -= spacing;
	                }

	                escribir(contentStream, 300, yTotales, "Saldo Adeudado: " + deuda);

	                // Bajamos Y general para lo que venga después
	                y = Math.min(y, yTotales) - spacing;
	            }
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        document.save(out);
	        return out.toByteArray();
	    }
	}

	
	@Override
	public byte[] generarReporteMovimientoCliente(Movimiento movimiento) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            double deuda=0;
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            	//Parte del paciente
            	InputStream imageStream = getClass().getClassLoader().getResourceAsStream("static/images/logo2.png");
            	PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "imagen");

                contentStream.drawImage(pdImage, 40, 700, 100, 100);
                
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(40, 740);
                
                contentStream.showText("                                                                                Fecha: " +movimiento.getFecha().format(formatter));
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("Local: " + movimiento.getLocal().getNombre()+"     Dirección: "+movimiento.getLocal().getDireccion()+"     Celular: "+movimiento.getLocal().getCelular());
                contentStream.newLine();
                if(movimiento.getPaciente() != null) {
                	contentStream.showText("Número de Ficha:"+movimiento.getPaciente().getFicha()+"     Paciente: " + (movimiento.getPaciente() != null ? movimiento.getPaciente().getNombreCompleto() : "---"));
                    contentStream.newLine();
                    contentStream.showText("Obra Social: " + (movimiento.getPaciente().getObraSocial() != null ? movimiento.getPaciente().getObraSocial() : "---"));
                    contentStream.newLine();
                }
                
                
                //DETALLE DE LA COMPRA
                if(!movimiento.getDetalles().isEmpty()) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.showText("Detalle de la compra:");
	                contentStream.newLine();
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                for (DetalleMovimiento detalle : movimiento.getDetalles()) {
	                    contentStream.showText(detalle.getProducto().getModelo() +" - Marca: "+detalle.getProducto().getMarca().getNombre()+ " - Cantidad: " + detalle.getCantidad() + " - Precio: " + detalle.getSubtotal());
	                    contentStream.newLine();
	                }
                }
                
                //DETALLE ADIOCIONAL
                if(!movimiento.getDetallesAdicionales().isEmpty()) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.showText("Detalle adicionales:");
	                contentStream.newLine();
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                for (DetalleAdicional detalle : movimiento.getDetallesAdicionales()) {
	                    contentStream.showText(detalle.getDescripcion() +" - Precio: " + detalle.getSubtotal());
	                    contentStream.newLine();
	                }
                }
                
                //DETALLE DEL PAGO
                if(!movimiento.getCajaMovimientos().isEmpty()) {

	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.showText("Pagos Realizados:");
	                contentStream.newLine();
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                for (CajaMovimiento pago : movimiento.getCajaMovimientos()) {
	                    contentStream.showText("Método: " + pago.getMetodoPago().getNombre() + " - Monto: " + pago.getMonto());
	                    deuda+=pago.getMonto();
	                    contentStream.newLine();
	                }
	            }
                
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Total: " + movimiento.getTotal());
                if(movimiento.getDescuento() !=null ) {
                	contentStream.showText("       Total con descuento: " + movimiento.getTotal());
                    contentStream.showText("       Descuento: " + movimiento.getDescuento()+"%");
                }
                deuda= movimiento.getTotal()-deuda;
                contentStream.showText("       Adeuda: " + deuda);
                contentStream.newLine();
                
                contentStream.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }
	
	// Función utilitaria para valores que pueden ser null
	private String safe(Number valor) {
	    return valor != null ? valor.toString() : "---";
	}
	
	private String safe(String value) {
	    return (value != null && !value.trim().isEmpty()) ? value : "---";
	}
	
	// Método auxiliar para escribir
	private void escribir(PDPageContentStream cs, float x, float y, String texto) throws IOException {
	    cs.beginText();
	    cs.newLineAtOffset(x, y);
	    cs.showText(texto);
	    cs.endText();
	}
	
	private String generarTextoGraduacionSimple(Optional<Graduacion> grad) {
	    return grad.map(g -> String.format("Esf: %s | Cil: %s | Eje: %s",
	        formatFloat(g.getEsferico()), formatFloat(g.getCilindrico()), safe(g.getEje())))
	        .orElse("Sin datos");
	}

	private String generarTextoGraduacionConAdicion(Optional<Graduacion> grad, Float adicion) {
	    String textoGrad = grad.map(g -> String.format("Esf: %s | Cil: %s | Eje: %s",
	        formatFloat(g.getEsferico()), formatFloat(g.getCilindrico()), safe(g.getEje())))
	        .orElse("Sin datos");
	    return textoGrad + " | Adición: " + formatFloat(adicion);
	}

	private String formatFloat(Float valor) {
	    if (valor == null) return "-";
	    return valor > 0 ? "+" + String.format("%.2f", valor) : String.format("%.2f", valor);
	}
}
