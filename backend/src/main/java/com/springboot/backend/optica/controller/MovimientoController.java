package com.springboot.backend.optica.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;


import com.springboot.backend.optica.dto.FiltroDTO;
import com.springboot.backend.optica.modelo.Movimiento;
import com.springboot.backend.optica.service.IMovimientoService;
import com.springboot.backend.optica.util.AuthUtils;

@CrossOrigin
@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

	@Autowired
    private IMovimientoService movimientoService;
	@Autowired
	private AuthUtils authUtils;

    @GetMapping
    public List<Movimiento> getAllMovimientos() {
        return movimientoService.findAll();
    }
    
 // Obtener pacientes paginados
 	@GetMapping("/paginado/{page}")
 	public Page<Movimiento> getAllMovimientosPaginados(@PathVariable Integer page) {
 	    Pageable pageable = PageRequest.of(page, 12);
 	    return movimientoService.findAllMovimiento(pageable);
 	}
 	
 	@GetMapping("/filtrar")
 	public ResponseEntity<Page<Movimiento>> filtrarMovimientos(
 			Authentication authentication,
 	        @RequestParam(required = false) Long idLocal,
 	        @RequestParam(required = false) String tipoMovimiento,
 	        @RequestParam(required = false) String nombrePaciente,
 	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
 	        @RequestParam(required = false) String metodoPago,
 	        @RequestParam(defaultValue = "0") int page,
 	        @RequestParam(defaultValue = "10") int size) {
 		 		
 		Long idLocalUser = authUtils.obtenerLocalIdDesdeToken(authentication);
 	    String rol = authUtils.obtenerRolDesdeToken(authentication);
 		
 	    Pageable pageable = PageRequest.of(page, size);
 	    Page<Movimiento> movimientos;

 	    if ("ROLE_ADMIN".equals(rol)) {
 	        movimientos = movimientoService.filtrarMovimientos(idLocal, tipoMovimiento, nombrePaciente, fecha, metodoPago, pageable);

 	    } else {
 	        movimientos = movimientoService.filtrarMovimientos(idLocalUser, tipoMovimiento, nombrePaciente, fecha, metodoPago, pageable);

 	    }

 	    return ResponseEntity.ok(movimientos);
 	}
 	
    @GetMapping("/filtrar-completo")
    public ResponseEntity<List<Movimiento>> filtrarMovimientosCompleto(
            Authentication authentication,
            @RequestParam(required = false) Long idLocal,
            @RequestParam(required = false) String tipoMovimiento,
            @RequestParam(required = false) String nombrePaciente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String metodoPago) {
        
        Long idLocalUser = authUtils.obtenerLocalIdDesdeToken(authentication);
        String rol = authUtils.obtenerRolDesdeToken(authentication);

        List<Movimiento> movimientos;

        if ("ROLE_ADMIN".equals(rol)) {
            movimientos = movimientoService.filtrarMovimientosCompleto(idLocal, tipoMovimiento, nombrePaciente, fecha, metodoPago);
        } else {
            movimientos = movimientoService.filtrarMovimientosCompleto(idLocalUser, tipoMovimiento, nombrePaciente, fecha, metodoPago);
        }

        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> getMovimientoById(@PathVariable Long id) {
        Movimiento movimiento = movimientoService.findById(id);
        return movimiento != null ? ResponseEntity.ok(movimiento) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/totales")
    public Map<String, Double> calcularTotales(@RequestParam(required = false) Long idLocal) {
        return movimientoService.calcularTotales(idLocal);
    }
    
    @GetMapping("/local/{idLocal}/page/{page}")
    public ResponseEntity<?> getMovimientosPorLocalPaginados(
            @PathVariable Long idLocal,
            @PathVariable int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movimiento> movimientos = movimientoService.findByLocalIdPaginated(idLocal, pageable);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/buscar/entre-fechas")
    public ResponseEntity<Page<Movimiento>> getMovimientosEntreFechas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam int page,
            @RequestParam int size) {
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        PageRequest pageable = PageRequest.of(page, size);
        Page<Movimiento> movimientos = movimientoService.findByFechaBetween(inicio, fin, pageable);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/buscar/tipo")
    public ResponseEntity<Page<Movimiento>> getMovimientosPorTipo(
            @RequestParam String tipo,
            @RequestParam int page,
            @RequestParam int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Movimiento> movimientos = movimientoService.findByTipoMovimiento(tipo, pageable);
        return ResponseEntity.ok(movimientos);
    }
    

    
    @PostMapping("/total-vendido")
    public ResponseEntity<Map<String, Integer>> obtenerCantidadTotalVendida(@RequestBody FiltroDTO filtros) {
        int total = movimientoService.obtenerCantidadTotalVendida(filtros);
        Map<String, Integer> response = new HashMap<>();
        response.put("total", total);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/total-ganado")
    public ResponseEntity<Map<String, Map<String, Double>>> obtenerTotalGanado(@RequestBody FiltroDTO filtros) {
        Map<String, Map<String, Double>> totales = movimientoService.calcularTotales(filtros);
        return ResponseEntity.ok(totales);
    }
    
    @Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
    @PostMapping
    public ResponseEntity<?> createMovimiento(@RequestBody Movimiento movimiento) {
        Map<String, Object> response = new HashMap<>();

        try {
            Movimiento newMovimiento = movimientoService.create(movimiento);
            response.put("mensaje", "El movimiento ha sido creado!");
            response.put("movimiento", newMovimiento);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error al crear el movimiento");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al crear el movimiento");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovimiento(@PathVariable Long id, @RequestBody Movimiento movimiento) {
        Map<String, Object> response = new HashMap<>();

        Movimiento movimientoActual = movimientoService.findById(id);
        if (movimientoActual == null) {
            response.put("mensaje", "Error: No se pudo editar el movimiento ID: "
                    .concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            Movimiento movimientoActualizado = movimientoService.update(id, movimiento);
            response.put("mensaje", "El movimiento ha sido actualizado!");
            response.put("movimiento", movimientoActualizado);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error al actualizar el movimiento");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al actualizar el movimiento");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
