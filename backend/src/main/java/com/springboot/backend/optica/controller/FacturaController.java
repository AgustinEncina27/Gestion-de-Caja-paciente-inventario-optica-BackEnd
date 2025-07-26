package com.springboot.backend.optica.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.backend.optica.dto.SolicitudFacturaDTO;
import com.springboot.backend.optica.modelo.Factura;
import com.springboot.backend.optica.service.IFacturaService;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = {"*"})
public class FacturaController {

    @Autowired
    private IFacturaService facturaService;

    @PostMapping("/emitir")
    public ResponseEntity<?> emitirFactura(@RequestBody SolicitudFacturaDTO solicitud) {
        try {
            Factura factura = facturaService.emitirFactura(solicitud);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al emitir factura", "error", e.getMessage()));
        }
    }
}
