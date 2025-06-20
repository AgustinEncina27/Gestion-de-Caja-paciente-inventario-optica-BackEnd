package com.springboot.backend.optica.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageImpl;

import com.springboot.backend.optica.dao.ICajaMovimientosDao;
import com.springboot.backend.optica.dao.IMetodoPagoDao;
import com.springboot.backend.optica.dao.IMovimientoDao;
import com.springboot.backend.optica.dao.IProductoLocalDao;
import com.springboot.backend.optica.dto.FiltroDTO;
import com.springboot.backend.optica.dto.MarcaResumenDTO;
import com.springboot.backend.optica.modelo.MetodoPago;
import com.springboot.backend.optica.modelo.Movimiento;
import com.springboot.backend.optica.modelo.Producto;
import com.springboot.backend.optica.modelo.ProductoLocal;
import com.springboot.backend.optica.modelo.CajaMovimiento;
import com.springboot.backend.optica.modelo.DetalleAdicional;
import com.springboot.backend.optica.modelo.DetalleMovimiento;
import com.springboot.backend.optica.modelo.Marca;

@Service
public class MovimientoServiceImpl implements IMovimientoService {

    @Autowired
    private IMovimientoDao movimientoRepository;
    
    @Autowired
    private IMetodoPagoDao metodoPagoRepository;
    
    @Autowired
    private ICajaMovimientosDao cajaMovimientoRepository;
    
    @Autowired
    private IPdfService pdfService;
    
    @Autowired
    private IProductoLocalDao  productoLocalRepository;
    
    @Autowired
    private ExcelServiceImp excelService;

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findAll() {
        return movimientoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Movimiento> filtrarMovimientos(Long idLocal, String tipoMovimiento, Long nroFicha, LocalDate fecha, String metodoPago, Pageable pageable) {
        if (idLocal != null && idLocal == 0) idLocal = null;

        LocalDateTime fechaInicio = (fecha != null) ? fecha.atStartOfDay() : LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime fechaFin = (fecha != null) ? fecha.plusDays(1).atStartOfDay() : LocalDateTime.of(2100, 1, 1, 0, 0);

        Page<CajaMovimiento> pagos = cajaMovimientoRepository.buscarPagosConMovimiento(
            idLocal, 
            tipoMovimiento, 
            nroFicha, 
            fechaInicio, 
            fechaFin, 
            metodoPago, 
            pageable
        );

        List<Movimiento> movimientos = pagos.getContent().stream()
        	    .map(pago -> {
        	        Movimiento movimiento = clonarMovimiento(pago.getMovimiento());
        	        movimiento.setFecha(pago.getFecha());
        	        return movimiento;
        	    })
        	    .collect(Collectors.toList());

        return new PageImpl<>(movimientos, pageable, pagos.getTotalElements());
    }
    
    private Movimiento clonarMovimiento(Movimiento original) {
        Movimiento nuevo = new Movimiento();
        nuevo.setId(original.getId());
        nuevo.setTipoMovimiento(original.getTipoMovimiento());
        nuevo.setTotal(original.getTotal());
        nuevo.setDescuento(original.getDescuento());
        nuevo.setTotalImpuesto(original.getTotalImpuesto());
        nuevo.setDescripcion(original.getDescripcion());
        nuevo.setPaciente(original.getPaciente());
        nuevo.setCajaMovimientos(original.getCajaMovimientos());
        nuevo.setDetalles(original.getDetalles());
        nuevo.setDetallesAdicionales(original.getDetallesAdicionales());
        nuevo.setLocal(original.getLocal());
        nuevo.setEstadoMovimiento(original.getEstadoMovimiento());
        return nuevo;
    }
    
    @Override
	@Transactional(readOnly = true)
	public Page<Movimiento> findAllMovimiento(Pageable pageable) {
		return movimientoRepository.findAll(pageable);
	}
    
    @Override
    @Transactional(readOnly = true)
    public Page<Movimiento> findByLocalIdPaginated(Long idLocal, Pageable pageable) {
        return movimientoRepository.findByLocalId(idLocal, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> calcularTotales(Long idLocal) {
        // Obtener todos los métodos de pago
        List<MetodoPago> metodosPago = metodoPagoRepository.findAll();
        Map<String, Double> totales = new HashMap<>();

        // Inicializar los totales en 0 para cada método de pago
        for (MetodoPago metodoPago : metodosPago) {
            totales.put(metodoPago.getNombre(), 0.0);
        }

        // Obtener movimientos según el local seleccionado
        List<Movimiento> movimientos;
        if (idLocal == 0) {
            movimientos = movimientoRepository.findAll();
        } else {
            movimientos = movimientoRepository.findByLocalId(idLocal);
        }

        // Calcular los totales para cada método de pago teniendo en cuenta el tipo de movimiento
        for (Movimiento movimiento : movimientos) {
            // Iterar sobre los pagos del movimiento
            for (CajaMovimiento movimientoCaja : movimiento.getCajaMovimientos()) {
                String metodoPagoNombre = movimientoCaja.getMetodoPago().getNombre();
                if (totales.containsKey(metodoPagoNombre)) {
                    double totalActual = totales.get(metodoPagoNombre);

                    // Sumar o restar según el tipo de movimiento
                    if ("ENTRADA".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
                        totalActual += movimientoCaja.getMontoImpuesto();
                    } else if ("SALIDA".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
                        totalActual -= movimientoCaja.getMontoImpuesto();
                    }

                    totales.put(metodoPagoNombre, totalActual);
                }
            }
        }

        return totales;
    }
    
	@Override
    @Transactional(readOnly = true)
	public Map<String, Map<String, Double>> calcularTotales(FiltroDTO filtros) {
	    // Obtener todos los métodos de pago
	    List<MetodoPago> metodosPago = metodoPagoRepository.findAll();
	    Map<String, Map<String, Double>> totales = new HashMap<>();
	    Long localid = filtros.getLocal() == 0 ? null : filtros.getLocal();

	    // Inicializar las entradas y salidas en 0.0 para cada método de pago
	    for (MetodoPago metodoPago : metodosPago) {
	        Map<String, Double> entradaSalida = new HashMap<>();
	        entradaSalida.put("entrada", 0.0);
	        entradaSalida.put("salida", 0.0);
	        totales.put(metodoPago.getNombre(), entradaSalida);
	    }

	    LocalDateTime fechaInicio = filtros.getFechaInicio().atStartOfDay();
	    LocalDateTime fechaFin = filtros.getFechaFin().atTime(23, 59, 59);

	    // Obtener movimientos filtrados
	    List<Movimiento> movimientos = movimientoRepository.filtrarMovimientos(
	            localid, fechaInicio, fechaFin
	    );

	    // Calcular las entradas y salidas por método de pago
	    for (Movimiento movimiento : movimientos) {
	        for (CajaMovimiento movimientoCaja : movimiento.getCajaMovimientos()) {
	            String metodoPagoNombre = movimientoCaja.getMetodoPago().getNombre();
	            if (totales.containsKey(metodoPagoNombre)) {
	                Map<String, Double> entradaSalida = totales.get(metodoPagoNombre);
	                double monto = movimientoCaja.getMontoImpuesto();

	                if ("ENTRADA".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
	                    entradaSalida.put("entrada", entradaSalida.get("entrada") + monto);
	                } else if ("SALIDA".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
	                    entradaSalida.put("salida", entradaSalida.get("salida") + monto);
	                }
	            }
	        }
	    }

	    return totales;
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] exportarExcelProductosVendidos(FiltroDTO filtros) throws IOException {
	    Long localId = filtros.getLocal() == 0 ? null : filtros.getLocal();
	    LocalDateTime fechaInicio = filtros.getFechaInicio().atStartOfDay();
	    LocalDateTime fechaFin = filtros.getFechaFin().atTime(23, 59, 59);

	    List<Movimiento> movimientos = movimientoRepository.filtrarMovimientos(localId, fechaInicio, fechaFin);

	    List<DetalleMovimiento> detalles = movimientos.stream()
	        .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipoMovimiento()))
	        .flatMap(m -> m.getDetalles().stream())
	        .collect(Collectors.toList());

	    // Paso 1: Agrupar cantidades
	    Map<Producto, Integer> resumen = new HashMap<>();
	    for (DetalleMovimiento detalle : detalles) {
	        Producto producto = detalle.getProducto();
	        resumen.put(producto, resumen.getOrDefault(producto, 0) + detalle.getCantidad());
	    }

	    // Paso 2: Ordenar por cantidad descendente y enviar al ExcelService
	    LinkedHashMap<Producto, Integer> resumenOrdenado = resumen.entrySet().stream()
	        .sorted(Map.Entry.<Producto, Integer>comparingByValue().reversed())
	        .collect(Collectors.toMap(
	            Map.Entry::getKey,
	            Map.Entry::getValue,
	            (e1, e2) -> e1,
	            LinkedHashMap::new
	        ));

	    return excelService.generarExcelProductosVendidos(resumenOrdenado);
	}
	
	@Override
	@Transactional(readOnly = true)
	public int obtenerCantidadTotalVendida(FiltroDTO filtros) {
	    Long localId = filtros.getLocal() == 0 ? null : filtros.getLocal();

	    LocalDateTime fechaInicio = filtros.getFechaInicio().atStartOfDay();
	    LocalDateTime fechaFin = filtros.getFechaFin().atTime(23, 59, 59);

	    // Reutilizamos el método que ya tenés
	    List<Movimiento> movimientos = movimientoRepository.filtrarMovimientos(
	            localId, fechaInicio, fechaFin
	    );

	    // Filtrar solo los movimientos de tipo ENTRADA
	    return movimientos.stream()
	            .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipoMovimiento()))
	            .flatMap(m -> m.getDetalles().stream())
	            .mapToInt(detalle -> detalle.getCantidad())
	            .sum();
	}

    @Override
    @Transactional(readOnly = true)
    public Movimiento findById(Long id) {
        Optional<Movimiento> result = movimientoRepository.findById(id);
        return result.orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Movimiento> findByTipoMovimiento(String tipo, Pageable pageable) {
        return movimientoRepository.findByTipoMovimiento(tipo, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Movimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin, pageable);
    }
        
    @Override
	@Transactional
    public Movimiento create(Movimiento movimiento) {
    	// Asignar el movimiento en cada DetalleMovimiento
        if (movimiento.getDetalles() != null) {
            for (DetalleMovimiento detalle : movimiento.getDetalles()) {
                detalle.setMovimiento(movimiento);
            }
        }
        
        // Asignar el movimiento en cada DetalleMovimiento
        if (movimiento.getDetallesAdicionales() != null) {
            for (DetalleAdicional detalleAdicional : movimiento.getDetallesAdicionales()) {
            	detalleAdicional.setMovimiento(movimiento);
            }
        }

        // Asignar el movimiento en cada CajaMovimientos
        if (movimiento.getCajaMovimientos() != null) {
            for (CajaMovimiento pago : movimiento.getCajaMovimientos()) {
                pago.setMovimiento(movimiento);
            }
        }
        
     // Verificar el tipo de movimiento y descontar stock si es SALIDA
        if ("ENTRADA".equalsIgnoreCase(movimiento.getTipoMovimiento()) && movimiento.getDetalles() != null) {
            for (DetalleMovimiento detalle : movimiento.getDetalles()) {
                ProductoLocal productoLocal = productoLocalRepository.findByProductoIdAndLocalId(
                        detalle.getProducto().getId(), movimiento.getLocal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en el local."));

                // Verificar si hay suficiente stock
                if (productoLocal.getStock() < detalle.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para el producto: " + detalle.getProducto().getModelo());
                }

                // Descontar el stock
                productoLocal.setStock(productoLocal.getStock() - detalle.getCantidad());
                productoLocalRepository.save(productoLocal);
            }
        }
        
        return movimientoRepository.save(movimiento);
    }
    
    @Transactional
    public Movimiento update(Long id, Movimiento movimiento) {
        Movimiento currentMovimiento = movimientoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"));

        // Restaurar stock si el movimiento original es de tipo "SALIDA"
        if ("ENTRADA".equalsIgnoreCase(currentMovimiento.getTipoMovimiento())) {
            for (DetalleMovimiento detalle : currentMovimiento.getDetalles()) {
                ProductoLocal productoLocal = productoLocalRepository.findByProductoIdAndLocalId(
                        detalle.getProducto().getId(), currentMovimiento.getLocal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en el local."));

                productoLocal.setStock(productoLocal.getStock() + detalle.getCantidad()); // Restaurar stock
                productoLocalRepository.save(productoLocal);
            }
        }
        
        // Asignar el movimiento en cada DetalleMovimiento
        if (movimiento.getDetalles() != null) {
            for (DetalleMovimiento detalle : movimiento.getDetalles()) {
                detalle.setMovimiento(movimiento);
            }
        }
        
        // Asignar el movimiento en cada DetalleMovimiento
        if (movimiento.getDetallesAdicionales() != null) {
            for (DetalleAdicional detalle : movimiento.getDetallesAdicionales()) {
                detalle.setMovimiento(movimiento);
            }
        }

        // Asignar el movimiento en cada CajaMovimientos
        if (movimiento.getCajaMovimientos() != null) {
            for (CajaMovimiento pago : movimiento.getCajaMovimientos()) {
                pago.setMovimiento(movimiento);
            }
        }
        
     
        // Ajustar el stock si el nuevo movimiento es de tipo "SALIDA"
        if ("ENTRADA".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            for (DetalleMovimiento detalle : movimiento.getDetalles()) {
                ProductoLocal productoLocal = productoLocalRepository.findByProductoIdAndLocalId(
                        detalle.getProducto().getId(), movimiento.getLocal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en el local."));

                // Verificar si hay suficiente stock
                if (productoLocal.getStock() < detalle.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para el producto: " + detalle.getProducto().getModelo());
                }

                // Descontar el stock
                productoLocal.setStock(productoLocal.getStock() - detalle.getCantidad());
                productoLocalRepository.save(productoLocal);
            }
        }

        // Guardar el movimiento actualizado
        return movimientoRepository.save(movimiento);
    }


    @Override
	@Transactional
    public void delete(Long id) {
    	Movimiento currentMovimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"));

        // Restaurar stock si el movimiento original es de tipo "SALIDA"
        if ("ENTRADA".equalsIgnoreCase(currentMovimiento.getTipoMovimiento())) {
            for (DetalleMovimiento detalle : currentMovimiento.getDetalles()) {
                ProductoLocal productoLocal = productoLocalRepository.findByProductoIdAndLocalId(
                        detalle.getProducto().getId(), currentMovimiento.getLocal().getId())
                		.orElseGet(() -> {
                            // Crear el producto en el local si no existe
                            ProductoLocal nuevoProductoLocal = new ProductoLocal();
                            nuevoProductoLocal.setProducto(detalle.getProducto());
                            nuevoProductoLocal.setLocal(currentMovimiento.getLocal());
                            nuevoProductoLocal.setStock(0); 
                            return productoLocalRepository.save(nuevoProductoLocal);
                        });

                productoLocal.setStock(productoLocal.getStock() + detalle.getCantidad()); // Restaurar stock
                productoLocalRepository.save(productoLocal);
            }
        }
        
        movimientoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] generarReporteMovimientoCliente(Long idMovimiento) throws IOException {
        Movimiento movimiento = movimientoRepository.findById(idMovimiento)
            .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"));

		return pdfService.generarReporteMovimientoCliente(movimiento);
		
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] generarReporteMovimientoOptica(Long idMovimiento) throws IOException {
        Movimiento movimiento = movimientoRepository.findById(idMovimiento)
            .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"));

		return pdfService.generarReporteMovimientoOptica(movimiento);
		
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] exportarExcelMarcasVendidas(FiltroDTO filtros) throws IOException {
        Long localId = filtros.getLocal() == 0 ? null : filtros.getLocal();
        LocalDateTime fechaInicio = filtros.getFechaInicio().atStartOfDay();
        LocalDateTime fechaFin = filtros.getFechaFin().atTime(23, 59, 59);

        List<Movimiento> movimientos = movimientoRepository.filtrarMovimientos(localId, fechaInicio, fechaFin);

        List<DetalleMovimiento> detalles = movimientos.stream()
            .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipoMovimiento()))
            .flatMap(m -> m.getDetalles().stream())
            .collect(Collectors.toList());

        // Agrupar por marca
        Map<Marca, MarcaResumenDTO> resumenPorMarca = new HashMap<>();
        for (DetalleMovimiento detalle : detalles) {
            Producto p = detalle.getProducto();
            Marca marca = p.getMarca();
            int cantidad = detalle.getCantidad();
            double costo = p.getCosto() != null ? p.getCosto() : 0;
            double precio = p.getPrecio();
            double ganancia = (precio - costo) * cantidad;

            resumenPorMarca.computeIfAbsent(marca, m -> new MarcaResumenDTO(m.getNombre(), 0, 0.0));
            MarcaResumenDTO resumen = resumenPorMarca.get(marca);
            resumen.setCantidadVendida(resumen.getCantidadVendida() + cantidad);
            resumen.setGananciaTotal(resumen.getGananciaTotal() + ganancia);
        }

        // Convertir y ordenar
        List<MarcaResumenDTO> resumenList = new ArrayList<>(resumenPorMarca.values());
        resumenList.sort(Comparator.comparingDouble(MarcaResumenDTO::getGananciaTotal).reversed());

        return excelService.generarExcelResumenMarcas(resumenList);
    }




}
