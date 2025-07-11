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
	        double total = 0;
	        float x = 50;
	        float y = 750;
	        float spacing = 15;

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("static/images/logo2.png");
	            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "imagen");
	            contentStream.drawImage(pdImage, 450, 710, 100, 50);

	            // Encabezado
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(x, y);
	            contentStream.showText("Reporte de Movimiento");
	            contentStream.endText();
	            y -= spacing;

	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(x, y);
	            contentStream.showText("Local: " + movimiento.getLocal().getNombre() + "    Fecha: " + movimiento.getFecha().format(formatter));
	            contentStream.endText();
	            y -= spacing;

	            if (movimiento.getPaciente() != null) {
	                String[] datosPaciente = {
	                    "Nro. de Ficha: " + movimiento.getPaciente().getFicha(),
	                    "Paciente: " + safe(movimiento.getPaciente().getNombreCompleto()),
	                    "Celular: " + safe(movimiento.getPaciente().getCelular()),
	                    "Domicilio: " + safe(movimiento.getPaciente().getDireccion()),
	                    "Obra Social: " + safe(movimiento.getPaciente().getObraSocial()),
	                    "Médico: " + safe(movimiento.getPaciente().getMedico())
	                };
	                for (String dato : datosPaciente) {
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(x, y);
	                    contentStream.showText(dato);
	                    contentStream.endText();
	                    y -= spacing;
	                }
	            }

	            y -= spacing;

	            // Ficha de graduación
	            if (movimiento.getPaciente() != null && movimiento.getPaciente().getHistorialFichas() != null && !movimiento.getPaciente().getHistorialFichas().isEmpty()) {
	            	Optional<FichaGraduacion> ultimaFichaOpt = movimiento.getPaciente().getHistorialFichas().stream()
	            		    .max(Comparator
	            		        .comparing(FichaGraduacion::getFecha)
	            		        .thenComparing(FichaGraduacion::getId));

	                if (ultimaFichaOpt.isPresent()) {
	                    FichaGraduacion ficha = ultimaFichaOpt.get();

	                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(x, y);
	                    contentStream.showText("Ficha de graduación del paciente");
	                    contentStream.endText();
	                    y -= spacing;

	                    contentStream.setFont(PDType1Font.HELVETICA, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(x, y);
	                    contentStream.showText("Fecha: " + (ficha.getFecha() != null ? ficha.getFecha().toString() : "---"));
	                    contentStream.endText();
	                    y -= spacing * 2;

	                    float leftX = 50;
	                    float rightX = 300;

	                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText("Medidas Persona");
	                    contentStream.endText();

	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(rightX, y);
	                    contentStream.showText("Medidas del armazón");
	                    contentStream.endText();
	                    y -= spacing;

	                    contentStream.setFont(PDType1Font.HELVETICA, 10);
	                    String[] medidasPersona = {
	                        "D.N.P: Derecho: " + safe(ficha.getDnpDerecho()) + ", Izquierdo: " + safe(ficha.getDnpIzquierdo()),
	                        "Alt. Película: " + safe(ficha.getAlturaPelicula()),
	                        "Alt.P: Derecho: " + safe(ficha.getAlturaPupilarDerecho()) + ", Izquierdo: " + safe(ficha.getAlturaPupilarIzquierdo())
	                    };
	                    String[] medidasArmazon = {
	                        "P: " + safe(ficha.getPuente()) + ", D.M: " + safe(ficha.getDiagonalMayor()),
	                        "Largo: " + safe(ficha.getLargo()) + ", Altura: " + safe(ficha.getAlturaArmazon())
	                    };

	                    for (int i = 0; i < medidasPersona.length; i++) {
	                        if (i < medidasPersona.length) {
	                            contentStream.beginText();
	                            contentStream.newLineAtOffset(leftX, y);
	                            contentStream.showText(medidasPersona[i]);
	                            contentStream.endText();
	                        }
	                        if (i < medidasArmazon.length) {
	                            contentStream.beginText();
	                            contentStream.newLineAtOffset(rightX, y);
	                            contentStream.showText(medidasArmazon[i]);
	                            contentStream.endText();
	                        }
	                        y -= spacing;
	                    }

	                    y -= spacing;

	                 // === GRADUACIÓN - Ojo Derecho ===
	                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText("Lejos - Ojo Derecho:");
	                    contentStream.endText();

	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(rightX, y);
	                    contentStream.showText("Cerca - Ojo Derecho:");
	                    contentStream.endText();
	                    y -= spacing;

	                    Optional<Graduacion> gradLejosDer = ficha.getGraduaciones().stream()
	                        .filter(g -> g.getOjo() == Graduacion.Ojo.DERECHO && g.getTipo() == Graduacion.TipoGraduacion.LEJOS)
	                        .findFirst();

	                    Optional<Graduacion> gradCercaDer = ficha.getGraduaciones().stream()
	                        .filter(g -> g.getOjo() == Graduacion.Ojo.DERECHO && g.getTipo() == Graduacion.TipoGraduacion.CERCA)
	                        .findFirst();

	                    contentStream.setFont(PDType1Font.HELVETICA, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText(generarTextoGraduacionSimple(gradLejosDer));
	                    contentStream.endText();

	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(rightX, y);
	                    contentStream.showText(generarTextoGraduacionConAdicion(gradCercaDer, ficha.getAdicionDerecho()));
	                    contentStream.endText();
	                    y -= spacing;

	                    // === GRADUACIÓN - Ojo Izquierdo ===
	                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText("Lejos - Ojo Izquierdo:");
	                    contentStream.endText();

	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(rightX, y);
	                    contentStream.showText("Cerca - Ojo Izquierdo:");
	                    contentStream.endText();
	                    y -= spacing;

	                    Optional<Graduacion> gradLejosIzq = ficha.getGraduaciones().stream()
	                        .filter(g -> g.getOjo() == Graduacion.Ojo.IZQUIERDO && g.getTipo() == Graduacion.TipoGraduacion.LEJOS)
	                        .findFirst();

	                    Optional<Graduacion> gradCercaIzq = ficha.getGraduaciones().stream()
	                        .filter(g -> g.getOjo() == Graduacion.Ojo.IZQUIERDO && g.getTipo() == Graduacion.TipoGraduacion.CERCA)
	                        .findFirst();

	                    contentStream.setFont(PDType1Font.HELVETICA, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText(generarTextoGraduacionSimple(gradLejosIzq));
	                    contentStream.endText();

	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(rightX, y);
	                    contentStream.showText(generarTextoGraduacionConAdicion(gradCercaIzq, ficha.getAdicionIzquierdo()));
	                    contentStream.endText();
	                    y -= spacing * 2;
	                }
	            }

	            // Información adicional
	            if (!movimiento.getDetalles().isEmpty()) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.beginText();
	                contentStream.newLineAtOffset(x, y);
	                contentStream.showText("Detalle de la compra:");
	                contentStream.endText();
	                y -= spacing;
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                for (DetalleMovimiento d : movimiento.getDetalles()) {
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(x, y);
	                    contentStream.showText(d.getProducto().getModelo() + " - Marca: " + d.getProducto().getMarca().getNombre() + " - Cantidad: " + d.getCantidad() + " - Precio: " + d.getSubtotal());
	                    contentStream.endText();
	                    y -= spacing;
	                    total += d.getSubtotal();
	                }
	            }

	            if (!movimiento.getDetallesAdicionales().isEmpty()) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.beginText();
	                contentStream.newLineAtOffset(x, y);
	                contentStream.showText("Detalle adicionales:");
	                contentStream.endText();
	                y -= spacing;
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                for (DetalleAdicional d : movimiento.getDetallesAdicionales()) {
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(x, y);
	                    contentStream.showText(d.getDescripcion() + " - Precio: " + d.getSubtotal());
	                    contentStream.endText();
	                    y -= spacing;
	                    total += d.getSubtotal();
	                }
	            }

	            if (!movimiento.getCajaMovimientos().isEmpty()) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.beginText();
	                contentStream.newLineAtOffset(x, y);
	                contentStream.showText("Pagos Realizados:");
	                contentStream.endText();
	                y -= spacing;
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                for (CajaMovimiento p : movimiento.getCajaMovimientos()) {
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(x, y);
	                    contentStream.showText("Método: " + p.getMetodoPago().getNombre() + " - Monto: " + p.getMonto());
	                    contentStream.endText();
	                    y -= spacing;
	                    deuda += p.getMonto();
	                }
	            }

	            deuda = movimiento.getTotal() - deuda;

	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(x, y);
	            contentStream.showText("Total: " + total);
	            contentStream.endText();
	            y -= spacing;

	            if (movimiento.getDescuento() != null) {
	                contentStream.beginText();
	                contentStream.newLineAtOffset(x, y);
	                contentStream.showText("Total con descuento: " + movimiento.getTotal() + "    Descuento: " + movimiento.getDescuento() + "%");
	                contentStream.endText();
	                y -= spacing;
	            }

	            contentStream.beginText();
	            contentStream.newLineAtOffset(x, y);
	            contentStream.showText("Adeuda: " + deuda);
	            contentStream.endText();	            
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
            double total=0;
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            	//Parte del paciente
            	InputStream imageStream = getClass().getClassLoader().getResourceAsStream("static/images/logo2.png");
            	PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "imagen");

                contentStream.drawImage(pdImage, 40, 730, 100, 50);
                
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(50, 750);
                
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
	                    total+=detalle.getSubtotal();
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
	                    total+=detalle.getSubtotal();
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
                contentStream.showText("Total: " + total);
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
