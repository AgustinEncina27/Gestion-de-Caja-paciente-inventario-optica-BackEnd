package com.springboot.backend.optica.service;

import com.springboot.backend.optica.dto.SolicitudFacturaDTO;
import com.springboot.backend.optica.modelo.Factura;

public interface IFacturaService {
	public Factura emitirFactura(SolicitudFacturaDTO solicitud) throws Exception;
}
