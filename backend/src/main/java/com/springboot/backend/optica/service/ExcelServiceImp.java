package com.springboot.backend.optica.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;

import com.springboot.backend.optica.dto.MarcaResumenDTO;
import com.springboot.backend.optica.modelo.Producto;
import com.springboot.backend.optica.modelo.ProductoLocal;

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
	
	public byte[] generarExcelProductosVendidos(Map<Producto, Integer> resumen) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Productos Vendidos");

	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("Modelo");
	    header.createCell(1).setCellValue("Marca");
	    header.createCell(2).setCellValue("Cantidad Vendida");
	    header.createCell(3).setCellValue("Costo");
	    header.createCell(4).setCellValue("Precio");
	    header.createCell(5).setCellValue("Ganancia");

	    int rowIdx = 1;
	    for (Map.Entry<Producto, Integer> entry : resumen.entrySet()) {
	        Producto p = entry.getKey();
	        int cantidad = entry.getValue();

	        Row row = sheet.createRow(rowIdx++);

	        double costo = p.getCosto() != null ? p.getCosto() : 0.0;
	        double precio = p.getPrecio();
	        double ganancia = (precio - costo) * cantidad;

	        row.createCell(0).setCellValue(p.getModelo());
	        row.createCell(1).setCellValue(p.getMarca().getNombre());
	        row.createCell(2).setCellValue(cantidad);
	        row.createCell(3).setCellValue(costo);
	        row.createCell(4).setCellValue(precio);
	        row.createCell(5).setCellValue(ganancia);
	    }

	    sheet.setColumnWidth(0, 6000);
	    sheet.setColumnWidth(1, 4000);
	    sheet.setColumnWidth(2, 6000);
	    sheet.setColumnWidth(3, 4000);
	    sheet.setColumnWidth(4, 4000);
	    sheet.setColumnWidth(5, 4000);

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
