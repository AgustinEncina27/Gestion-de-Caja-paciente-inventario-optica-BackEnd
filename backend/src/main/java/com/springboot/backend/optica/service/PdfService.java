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
	public byte[] generarReporteMovimiento(Movimiento movimiento) throws IOException {
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
                
                //Parte para la optica
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText("------------------------------------------------------------------------------------------------------------------------------------");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Reporte de Movimiento");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText("Local: " + movimiento.getLocal().getNombre()+"                                                Fecha: " + movimiento.getFecha().format(formatter));
                contentStream.newLine();
                
                //INFORMACION DEL PACIENTE
                if(movimiento.getPaciente() != null) {
                	contentStream.showText("Número de Ficha:"+movimiento.getPaciente().getFicha());
                    contentStream.newLine();	
                }
                contentStream.showText("Paciente: " + (movimiento.getPaciente() != null ? movimiento.getPaciente().getNombreCompleto() : "---"));
                contentStream.newLine();
                contentStream.showText("Celular: " + (movimiento.getPaciente() != null ? movimiento.getPaciente().getCelular() : "---"));
                contentStream.newLine();
                contentStream.showText("Domicilio: " + (movimiento.getPaciente() != null ? movimiento.getPaciente().getDireccion() : "---"));
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Graduaciones");
                contentStream.newLine();
                contentStream.endText();
                float startY = 700; // posición vertical inicial
                float leftX = 50;   // columna izquierda para Medidas Persona
                float rightX = 300; // columna derecha para Medidas del Armazón
                float lineSpacing = 15;

                if (movimiento.getPaciente() != null &&
                    movimiento.getPaciente().getHistorialFichas() != null &&
                    !movimiento.getPaciente().getHistorialFichas().isEmpty()) {

                    Optional<FichaGraduacion> ultimaFichaOpt = movimiento.getPaciente().getHistorialFichas().stream()
                        .max(Comparator.comparing(FichaGraduacion::getFecha));

                    if (ultimaFichaOpt.isPresent()) {
                        FichaGraduacion ficha = ultimaFichaOpt.get();

                        // Título
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Ficha de graduación del paciente");
                        contentStream.endText();

                        startY -= lineSpacing;

                        // Fecha
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Fecha: " + (ficha.getFecha() != null ? ficha.getFecha().toString() : "---"));
                        contentStream.endText();

                        startY -= (lineSpacing * 2);

                        // Títulos columnas
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Medidas Persona");
                        contentStream.endText();

                        contentStream.beginText();
                        contentStream.newLineAtOffset(rightX, startY);
                        contentStream.showText("Medidas del armazón");
                        contentStream.endText();

                        startY -= lineSpacing;

                        // Medidas Persona - línea 1
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("D.N.P: Derecho: " + safe(ficha.getDnpDerecho()) + ", Izquierdo: " + safe(ficha.getDnpIzquierdo()));
                        contentStream.endText();

                        // Medidas Armazón - línea 1
                        contentStream.beginText();
                        contentStream.newLineAtOffset(rightX, startY);
                        contentStream.showText("P: " + safe(ficha.getPuente()) + ", D.M: " + safe(ficha.getDiagonalMayor()));
                        contentStream.endText();

                        startY -= lineSpacing;

                        // Medidas Persona - línea 2
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Alt. Película: " + safe(ficha.getAlturaPelicula()));
                        contentStream.endText();

                        // Medidas Armazón - línea 2
                        contentStream.beginText();
                        contentStream.newLineAtOffset(rightX, startY);
                        contentStream.showText("Largo: " + safe(ficha.getLargo()) + ", Altura: " + safe(ficha.getAlturaArmazon()));
                        contentStream.endText();

                        startY -= lineSpacing;

                        // Medidas Persona - línea 3
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Alt.P: Derecho: " + safe(ficha.getAlturaPupilarDerecho()) + ", Izquierdo: " + safe(ficha.getAlturaPupilarIzquierdo()));
                        contentStream.endText();

                        startY -= (lineSpacing * 2);

                        // Graduación Ojo Derecho
                        Optional<Graduacion> graduacionDerecho = ficha.getGraduaciones().stream()
                            .filter(g -> g.getOjo() == Graduacion.Ojo.DERECHO)
                            .findFirst();

                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Graduación Ojo Derecho:");
                        contentStream.endText();

                        startY -= lineSpacing;

                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        if (graduacionDerecho.isPresent()) {
                            Graduacion g = graduacionDerecho.get();
                            contentStream.showText("Esférico: " + safe(g.getEsferico()) +
                                ", Cilíndrico: " + safe(g.getCilindrico()) +
                                ", Eje: " + safe(g.getEje()) +
                                ", Adición: " + safe(g.getAdicion()) +
                                ", Cerca: " + safe(g.getCerca()));
                        } else {
                            contentStream.showText("---");
                        }
                        contentStream.endText();

                        startY -= (lineSpacing * 2);

                        // Graduación Ojo Izquierdo
                        Optional<Graduacion> graduacionIzquierdo = ficha.getGraduaciones().stream()
                            .filter(g -> g.getOjo() == Graduacion.Ojo.IZQUIERDO)
                            .findFirst();

                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("Graduación Ojo Izquierdo:");
                        contentStream.endText();

                        startY -= lineSpacing;

                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        if (graduacionIzquierdo.isPresent()) {
                            Graduacion g = graduacionIzquierdo.get();
                            contentStream.showText("Esférico: " + safe(g.getEsferico()) +
                                ", Cilíndrico: " + safe(g.getCilindrico()) +
                                ", Eje: " + safe(g.getEje()) +
                                ", Adición: " + safe(g.getAdicion()) +
                                ", Cerca: " + safe(g.getCerca()));
                        } else {
                            contentStream.showText("---");
                        }
                        contentStream.endText();

                    } else {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(leftX, startY);
                        contentStream.showText("No hay ficha de graduación registrada.");
                        contentStream.endText();
                    }

                } else {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(leftX, startY);
                    contentStream.showText("No hay información de ficha disponible para el paciente.");
                    contentStream.endText();
                }
                
                //DETALLE DE LA COMPRA
                if(!movimiento.getDetalles().isEmpty()) {
	                contentStream.newLine();
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                contentStream.showText("Detalle de la compra:");
	                contentStream.setFont(PDType1Font.HELVETICA, 10);
	                contentStream.newLine();
	                for (DetalleMovimiento detalle : movimiento.getDetalles()) {
	                    contentStream.showText(detalle.getProducto().getModelo() +" - Marca:"+detalle.getProducto().getMarca().getNombre()+ " - Cantidad: " + detalle.getCantidad() + " - Precio: " + detalle.getSubtotal());
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
	                    contentStream.newLine();
	                }
	            }
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Total: " + total);
                if(movimiento.getDescuento() !=null ) {
                	contentStream.showText("       Total con descuento: " + movimiento.getTotal());
                    contentStream.showText("       Descuento: " + movimiento.getDescuento()+"%");
                }
                contentStream.showText("       Adeuda: " + deuda);
                contentStream.newLine();
                
                //INFORMACION DEL MEDICO
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Información del Médico:");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                if(movimiento.getPaciente() != null) {
                    contentStream.showText("Médico: " + (movimiento.getPaciente().getMedico() != null ? movimiento.getPaciente().getMedico() : "---"));
                }
                contentStream.newLine();
                contentStream.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }
	
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

	                    // Graduación Ojo Derecho
	                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText("Graduación Ojo Derecho:");
	                    contentStream.endText();
	                    y -= spacing;

	                    contentStream.setFont(PDType1Font.HELVETICA, 10);
	                    Optional<Graduacion> gradDer = ficha.getGraduaciones().stream().filter(g -> g.getOjo() == Graduacion.Ojo.DERECHO).findFirst();
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText(gradDer.map(g -> "Esférico: " + safe(g.getEsferico()) + ", Cilíndrico: " + safe(g.getCilindrico()) + ", Eje: " + safe(g.getEje()) + ", Adición: " + safe(g.getAdicion()) + ", Cerca: " + safe(g.getCerca())).orElse("---"));
	                    contentStream.endText();
	                    y -= spacing;

	                    // Graduación Ojo Izquierdo
	                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText("Graduación Ojo Izquierdo:");
	                    contentStream.endText();
	                    y -= spacing;

	                    contentStream.setFont(PDType1Font.HELVETICA, 10);
	                    Optional<Graduacion> gradIzq = ficha.getGraduaciones().stream().filter(g -> g.getOjo() == Graduacion.Ojo.IZQUIERDO).findFirst();
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(leftX, y);
	                    contentStream.showText(gradIzq.map(g -> "Esférico: " + safe(g.getEsferico()) + ", Cilíndrico: " + safe(g.getCilindrico()) + ", Eje: " + safe(g.getEje()) + ", Adición: " + safe(g.getAdicion()) + ", Cerca: " + safe(g.getCerca())).orElse("---"));
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
}
