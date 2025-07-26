package com.springboot.backend.optica.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.backend.optica.dto.FiltroDTO;
import com.springboot.backend.optica.modelo.MetodoPago;
import com.springboot.backend.optica.modelo.Movimiento;

public interface IMovimientoService {
	List<Movimiento> findAll();
    Movimiento findById(Long id);
    Movimiento create(Movimiento movimiento);
    Movimiento update(Long id,Movimiento movimiento);
    void delete(Long id);
    public int obtenerCantidadTotalVendida(FiltroDTO filtros);
    Page<Movimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    Page<Movimiento> findByTipoMovimiento(String tipo, Pageable pageable);
	Page<Movimiento> findAllMovimiento(Pageable pageable);
	public Map<MetodoPago.TipoMetodoPago, Double> calcularTotales(Long idLocal);
	Page<Movimiento> findByLocalIdPaginated(Long idLocal, Pageable pageable);
	Page<Movimiento> filtrarMovimientos(Long idLocal, String tipoMovimiento, String nombrePaciente, LocalDate fecha, String metodoPago, Pageable pageable);
	byte[] generarReporteMovimientoCliente(Long idMovimiento)throws IOException;
	byte[] generarReporteMovimientoOptica(Long idMovimiento)throws IOException;
	public Map<String, Map<String, Double>> calcularTotales(FiltroDTO filtros);
	public byte[] exportarExcelProductosVendidos(FiltroDTO filtros) throws IOException;
	byte[] exportarExcelMarcasVendidas(FiltroDTO filtros) throws IOException;
}
