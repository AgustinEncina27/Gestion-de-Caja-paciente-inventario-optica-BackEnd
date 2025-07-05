package com.springboot.backend.optica.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;

import com.springboot.backend.optica.dto.MarcaResumenDTO;
import com.springboot.backend.optica.modelo.Producto;
import com.springboot.backend.optica.modelo.ProductoLocal;
import com.springboot.backend.optica.modelo.ProductoResumen;

@Service
public class ExcelServiceImp {
	
	public byte[] generarExcelStockPorLocal(List<ProductoLocal> productoLocales) throws IOException {
        // Ordenar productos por modelo
        productoLocales.sort((p1, p2) -> p1.getProducto().getModelo().compareToIgnoreCase(p2.getProducto().getModelo()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stock Local");

        // Encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Modelo");
        headerRow.createCell(1).setCellValue("Marca");
        headerRow.createCell(2).setCellValue("Costo");
        headerRow.createCell(3).setCellValue("Precio");
        headerRow.createCell(4).setCellValue("Stock");

        int rowNum = 1;
        for (ProductoLocal productoLocal : productoLocales) {
            if (productoLocal.getStock() > 0) {
                Producto producto = productoLocal.getProducto();
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(producto.getModelo());
                row.createCell(1).setCellValue(producto.getMarca().getNombre());
                row.createCell(2).setCellValue(producto.getCosto() != null ? producto.getCosto() : 0);
                row.createCell(3).setCellValue(producto.getPrecio());
                row.createCell(4).setCellValue(productoLocal.getStock());
            }
        }

        // Ajustar columnas
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 3000);

        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
	
	public byte[] generarExcelProductosVendidos(Map<String, ProductoResumen> resumen) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Productos Vendidos");

	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("Modelo");
	    header.createCell(1).setCellValue("Categorías");
	    header.createCell(2).setCellValue("Descripción");
	    header.createCell(3).setCellValue("Marca");
	    header.createCell(4).setCellValue("Cantidad Vendida");
	    header.createCell(5).setCellValue("Costo");
	    header.createCell(6).setCellValue("Precio");
	    header.createCell(7).setCellValue("Ganancia");

	    int rowIdx = 1;
	    for (ProductoResumen resumenItem : resumen.values()) {
	        Row row = sheet.createRow(rowIdx++);

	        if (resumenItem.esCristal) {
	            row.createCell(0).setCellValue("Cristal Adicional"); // Modelo
	            row.createCell(1).setCellValue(""); // Categorías
	            row.createCell(2).setCellValue(""); // Descripción
	            row.createCell(3).setCellValue(resumenItem.descripcionCristal); // Marca
	            row.createCell(4).setCellValue(resumenItem.cantidad); // Cantidad
	            row.createCell(5).setCellValue(""); // Costo
	            row.createCell(6).setCellValue(resumenItem.totalCristal); // Precio acumulado
	            row.createCell(7).setCellValue(""); // Ganancia
	        } else {
	            Producto p = resumenItem.producto;
	            double costo = p.getCosto() != null ? p.getCosto() : 0.0;
	            double precio = p.getPrecio();
	            double ganancia = (precio - costo) * resumenItem.cantidad;

	            row.createCell(0).setCellValue(p.getModelo());

	            String categoriasTexto = p.getCategorias() != null
	                    ? p.getCategorias().stream().map(c -> c.getNombre()).collect(Collectors.joining(", "))
	                    : "";

	            row.createCell(1).setCellValue(categoriasTexto);
	            row.createCell(2).setCellValue(p.getDescripcion() != null ? p.getDescripcion() : "");
	            row.createCell(3).setCellValue(p.getMarca().getNombre());
	            row.createCell(4).setCellValue(resumenItem.cantidad);
	            row.createCell(5).setCellValue(costo);
	            row.createCell(6).setCellValue(precio);
	            row.createCell(7).setCellValue(ganancia);
	        }
	    }

	    // Ajustar anchos de columnas
	    sheet.setColumnWidth(0, 20 * 256); // Modelo
	    sheet.setColumnWidth(1, 30 * 256); // Categorías
	    sheet.setColumnWidth(2, 30 * 256); // Descripción
	    sheet.setColumnWidth(3, 20 * 256); // Marca
	    sheet.setColumnWidth(4, 15 * 256); // Cantidad
	    sheet.setColumnWidth(5, 15 * 256); // Costo
	    sheet.setColumnWidth(6, 15 * 256); // Precio
	    sheet.setColumnWidth(7, 15 * 256); // Ganancia

	    workbook.write(out);
	    workbook.close();
	    return out.toByteArray();
	}

	
	public byte[] generarExcelResumenMarcas(List<MarcaResumenDTO> resumen) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Resumen por Marca");

	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("Marca");
	    header.createCell(1).setCellValue("Cantidad Vendida Total");
	    header.createCell(2).setCellValue("Ganancia Total");

	    int rowIdx = 1;
	    for (MarcaResumenDTO dto : resumen) {
	        Row row = sheet.createRow(rowIdx++);
	        row.createCell(0).setCellValue(dto.getMarcaNombre());
	        row.createCell(1).setCellValue(dto.getCantidadVendida());
	        row.createCell(2).setCellValue(dto.getGananciaTotal());
	    }

	    sheet.setColumnWidth(0, 6000);
	    sheet.setColumnWidth(1, 4000);
	    sheet.setColumnWidth(2, 4000);

	    workbook.write(out);
	    workbook.close();
	    return out.toByteArray();
	}


}
