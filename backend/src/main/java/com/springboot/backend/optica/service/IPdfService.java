package com.springboot.backend.optica.service;

import java.io.IOException;

import com.springboot.backend.optica.modelo.Movimiento;

public interface IPdfService {
    byte[] generarReporteMovimiento(Movimiento movimiento) throws IOException;
    byte[] generarReporteMovimientoCliente(Movimiento movimiento) throws IOException;
    byte[] generarReporteMovimientoOptica(Movimiento movimiento) throws IOException;
}
