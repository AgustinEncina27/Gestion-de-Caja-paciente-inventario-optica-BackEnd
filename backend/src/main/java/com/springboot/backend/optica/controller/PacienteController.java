package com.springboot.backend.optica.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
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

import com.springboot.backend.optica.dto.PacientesPorSucursalDTO;
import com.springboot.backend.optica.modelo.Paciente;
import com.springboot.backend.optica.service.IPacienteService;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PacienteController {
	
	@Autowired
	private IPacienteService pacienteService;
			
	// Obtener todos los pacientes
	@GetMapping("/pacientes")
	public List<Paciente> getAllPacientes() {
	    return pacienteService.findAllPaciente();
	}

	// Obtener pacientes paginados
	@GetMapping("/pacientes/paginado/{page}")
	public Page<Paciente> getAllPacientesPaginados(@PathVariable Integer page) {
	    Pageable pageable = PageRequest.of(page, 12);
	    return pacienteService.findAllPaciente(pageable);
	}

	// Buscar pacientes por nombre con paginación
	@GetMapping("/pacientes/buscar/nombre")
	public Page<Paciente> buscarPacientesPorNombre(
	        @RequestParam String nombre,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "12") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    return pacienteService.findByNombre(nombre, pageable);
	}

	// Buscar pacientes por documento con paginación
	@GetMapping("/pacientes/buscar/documento/{documento}/page/{page}")
	public Page<Paciente> buscarPacientesPorDocumento(
	        @PathVariable String documento,
	        @PathVariable Integer page) {

	    Pageable pageable = PageRequest.of(page, 12);
	    return pacienteService.findByDocumento(documento, pageable);
	}
	
	@GetMapping("/pacientes/buscar-por-ficha/{ficha}")
	public ResponseEntity<Paciente> getPacientePorFicha(@PathVariable Long ficha) {
	    Optional<Paciente> paciente = pacienteService.findByFicha(ficha);
	    return paciente.map(ResponseEntity::ok)
	                   .orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/pacientes/id/{id}")
	public ResponseEntity<?> getPacientePorId(@PathVariable Long id) {
	    Paciente paciente = null;
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        paciente = pacienteService.findById(id);
	    } catch(DataAccessException e) {
	        response.put("mensaje", "Error al consultar la base de datos");
	        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    if(paciente == null) {
	        response.put("mensaje", "El paciente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	    return new ResponseEntity<>(paciente, HttpStatus.OK);
	}
	
	
	@GetMapping("/pacientes/cantidad-por-sucursal")
    public ResponseEntity<List<PacientesPorSucursalDTO>> obtenerCantidadPacientesPorSucursal() {
        List<PacientesPorSucursalDTO> pacientes = pacienteService.obtenerCantidadPacientesPorSucursal();
        return ResponseEntity.ok(pacientes);
    }
	
	@Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
	@PostMapping("/pacientes/cristales")
    public ResponseEntity<?> agregarCristal(@RequestBody Map<String, Object> body) {
        try {
            Long pacienteId = ((Number) body.get("pacienteId")).longValue();
            String nombre = (String) body.get("nombre");

            pacienteService.agregarCristal(pacienteId, nombre);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al agregar cristal: " + e.getMessage());
        }
    }
	
	@Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
	@PostMapping("/pacientes")
	public ResponseEntity<?> create(@Valid @RequestBody Paciente paciente, BindingResult result) {
	    Map<String, Object> response = new HashMap<>();

	    if (result.hasErrors()) {
	        List<String> errors = result.getFieldErrors()
	                .stream()
	                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
	                .collect(Collectors.toList());
	        response.put("errors", errors);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    // Validaciones por documento o celular
	    if (paciente.getDocumento() != null && !paciente.getDocumento().isEmpty()) {
	        if (pacienteService.existsByDocumento(paciente.getDocumento())) {
	            response.put("mensaje", "Ya existe un paciente con el mismo documento");
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	    } else if (paciente.getCelular() != null && !paciente.getCelular().isEmpty()) {
	        if (pacienteService.existsByCelular(paciente.getCelular())) {
	            response.put("mensaje", "Ya existe un paciente con el mismo celular y sin documento");
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	    }

	    try {
	        // Relacionar cada ficha con el paciente, y a su vez las graduaciones y cristales con la ficha
	        if (paciente.getHistorialFichas() != null) {
	            paciente.getHistorialFichas().forEach(ficha -> {
	                ficha.setPaciente(paciente);

	                if (ficha.getGraduaciones() != null) {
	                    ficha.getGraduaciones().forEach(graduacion -> graduacion.setFichaGraduacion(ficha));
	                }
	                if (ficha.getCristales() != null) {
	                    ficha.getCristales().forEach(cristal -> cristal.setFichaGraduacion(ficha));
	                }
	            });
	        }

	        Paciente pacienteNuevo = pacienteService.save(paciente);

	        response.put("mensaje", "El paciente ha sido creado exitosamente.");
	        response.put("paciente", pacienteNuevo);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);

	    } catch (DataAccessException e) {
	        response.put("mensaje", "Error al realizar la inserción en la base de datos");
	        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

		
	@Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
	@PutMapping("/pacientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Paciente paciente, BindingResult result, @PathVariable Long id) {

	    Map<String, Object> response = new HashMap<>();

	    if (result.hasErrors()) {
	        List<String> errors = result.getFieldErrors()
	                .stream()
	                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
	                .collect(Collectors.toList());

	        response.put("errors", errors);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    Paciente currentPaciente = pacienteService.findById(id);
	    if (currentPaciente == null) {
	        response.put("mensaje", "Error: No se pudo editar el paciente ID: "
	                + id + " porque no existe en la base de datos.");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    try {
	        // 🔁 Vincular las fichas al paciente
	        if (paciente.getHistorialFichas() != null) {
	            paciente.getHistorialFichas().forEach(ficha -> {
	                ficha.setPaciente(currentPaciente); // usar el paciente actual (no el que viene del JSON)

	                if (ficha.getGraduaciones() != null) {
	                    ficha.getGraduaciones().forEach(graduacion -> graduacion.setFichaGraduacion(ficha));
	                }

	                if (ficha.getCristales() != null) {
	                    ficha.getCristales().forEach(c -> c.setFichaGraduacion(ficha));
	                }
	            });
	        }

	        // 🔄 Actualizar campos básicos
	        currentPaciente.setNombreCompleto(paciente.getNombreCompleto());
	        currentPaciente.setDireccion(paciente.getDireccion());
	        currentPaciente.setObraSocial(paciente.getObraSocial());
	        currentPaciente.setCelular(paciente.getCelular());
	        currentPaciente.setObservaciones(paciente.getObservaciones());
	        currentPaciente.setGenero(paciente.getGenero());
	        currentPaciente.setLocal(paciente.getLocal());
	        currentPaciente.setDocumento(paciente.getDocumento());
	        currentPaciente.setCorreo(paciente.getCorreo());
	        currentPaciente.setMedico(paciente.getMedico());
	        currentPaciente.setCreadoEn(paciente.getCreadoEn());
	        currentPaciente.setUltimaActualizacion(paciente.getUltimaActualizacion());

	        // 🔁 Actualizar historial de fichas (opcional: reemplaza todo el historial)
	        currentPaciente.getHistorialFichas().clear();
	        if (paciente.getHistorialFichas() != null) {
	            currentPaciente.getHistorialFichas().addAll(paciente.getHistorialFichas());
	        }

	        Paciente pacienteActualizado = pacienteService.save(currentPaciente);

	        response.put("mensaje", "El paciente ha sido actualizado correctamente.");
	        response.put("paciente", pacienteActualizado);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);

	    } catch (DataAccessException e) {
	        response.put("mensaje", "Error al actualizar el paciente en la base de datos.");
	        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@Secured({ "ROLE_ADMIN", "ROLE_VENDEDOR" })
	@DeleteMapping("/pacientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			pacienteService.deletePaciente(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error: no se pudo eliminar el paciente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se elimino el paciente con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
