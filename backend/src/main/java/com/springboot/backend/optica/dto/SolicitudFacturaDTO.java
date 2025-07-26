package com.springboot.backend.optica.dto;

import com.springboot.backend.optica.modelo.Factura.TipoCliente;
import com.springboot.backend.optica.modelo.Factura.TipoComprobante;

import lombok.Data;

@Data
public class SolicitudFacturaDTO {
    private Long movimientoId;
    private TipoComprobante tipoComprobante;
    private TipoCliente tipoCliente;
}