package com.springboot.backend.optica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.springboot.backend.optica.dto.FiltroDTO;
import com.springboot.backend.optica.service.IMovimientoService;
import com.springboot.backend.optica.service.IProductoService;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin
public class ExcelController {

    @Autowired
    private IProductoService productoService;
    
    @Autowired
    private IMovimientoService movimientoService;

    @GetMapping("/stock/{localId}")
    public ResponseEntity<byte[]> exportStockToExcel(@PathVariable Long localId) throws IOException {
        // Llama al servicio para generar el Excel
        byte[] excelData = productoService.exportStockToExcel(localId);

        // Configurar la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stock_local.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    
    @PostMapping("/ventas")
    public ResponseEntity<byte[]> exportarProductosVendidos(@RequestBody FiltroDTO filtros) {
        try {
            byte[] excel = movimientoService.exportarExcelProductosVendidos(filtros);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos_vendidos.xlsx");

            return new ResponseEntity<>(excel, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/ventas/marcas")
    public ResponseEntity<byte[]> exportarResumenPorMarcas(@RequestBody FiltroDTO filtros) {
        try {
            byte[] excel = movimientoService.exportarExcelMarcasVendidas(filtros);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=marcas_vendidas.xlsx");

            return new ResponseEntity<>(excel, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}