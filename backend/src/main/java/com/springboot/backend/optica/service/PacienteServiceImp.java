package com.springboot.backend.optica.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;

import com.springboot.backend.optica.dao.FichaGraduacionDao;
import com.springboot.backend.optica.dao.GraduacionDao;
import com.springboot.backend.optica.dao.ICristalHistorialDao;
import com.springboot.backend.optica.dao.IPacienteDao;
import com.springboot.backend.optica.dto.PacientesPorSucursalDTO;
import com.springboot.backend.optica.modelo.CristalHistorial;
import com.springboot.backend.optica.modelo.FichaGraduacion;
import com.springboot.backend.optica.modelo.Graduacion;
import com.springboot.backend.optica.modelo.Paciente;

@Service
public class PacienteServiceImp implements IPacienteService {
	
	@Autowired
	private IPacienteDao pacienteDao;
	
	@Autowired
	private FichaGraduacionDao fichaGraduacionDao;

	@Autowired
	private ICristalHistorialDao cristalHistorialDao;
	
	@Autowired
	private GraduacionDao graduacionDao;
			
	@Override
	@Transactional(readOnly = true)
	public List<Paciente> findAllPaciente() {
		return pacienteDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Paciente> findAllPaciente(Pageable pageable) {
		return pacienteDao.findAllByOrderByFicha(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Paciente findById(Long id) {
		return pacienteDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
    public Optional<Paciente> findByFicha(Long ficha) {
        return pacienteDao.findByFicha(ficha);
    }
	
	@Override
	@Transactional(readOnly = true)
	public Page<Paciente> findByNombre(String nombre,Pageable pageable) {
		return pacienteDao.findByNombreCompleto(nombre,pageable);
	}
		
	@Override
	@Transactional(readOnly = true)
	public Page<Paciente> findByDocumento(String documento, Pageable pageable) {
		return pacienteDao.findByDocumento(documento,pageable);
	}
	
	@Override
	@Transactional
	public Paciente save(Paciente paciente) {
        if (paciente.getFicha() == 0) {
            Long maxFicha = pacienteDao.findMaxFicha();
            paciente.setFicha(maxFicha != null ? maxFicha + 1 : 1);
        }
        return pacienteDao.save(paciente);
    }

	@Override
	@Transactional
	public void deletePaciente(Long id) {
		Paciente paciente = pacienteDao.findById(id).get();
		pacienteDao.delete(paciente);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByDocumento(String documento) {
		return pacienteDao.existsByDocumento(documento);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByCelular(String celular) {
		return pacienteDao.existsByCelular(celular);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<PacientesPorSucursalDTO> obtenerCantidadPacientesPorSucursal() {
        List<Object[]> resultados = pacienteDao.contarPacientesPorSucursal();
        List<PacientesPorSucursalDTO> pacientesPorSucursal = new ArrayList<>();

        for (Object[] fila : resultados) {
            PacientesPorSucursalDTO dto = new PacientesPorSucursalDTO(
                ((Number) fila[0]).longValue(),  // localId
                (String) fila[1],               // localNombre
                ((Number) fila[2]).intValue()    // cantidadPacientes
            );
            pacientesPorSucursal.add(dto);
        }

        return pacientesPorSucursal;
    }

	@Override
	@Transactional
	public void agregarCristal(Long pacienteId, String nombreCristal) {
	    Paciente paciente = pacienteDao.findById(pacienteId)
	            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

	    FichaGraduacion ficha = paciente.getHistorialFichas().stream()
	    		.max(Comparator
	    		        .comparing(FichaGraduacion::getFecha)
	    		        .thenComparing(FichaGraduacion::getId))
	            .orElse(null);
	    	    
	    if (ficha == null) {
	    	ficha = new FichaGraduacion(); // ✅ Crear nueva ficha
	        ficha.setFecha(LocalDate.now());
	        ficha.setPaciente(paciente);
	        fichaGraduacionDao.save(ficha); 
	    	List<Graduacion> graduaciones = new ArrayList<>();

	        // Lejos DERECHO
	        Graduacion gradLejosDerecho = new Graduacion();
	        gradLejosDerecho.setOjo(Graduacion.Ojo.DERECHO);
	        gradLejosDerecho.setEsferico(0f);
	        gradLejosDerecho.setCilindrico(0f);
	        gradLejosDerecho.setEje(0f);
	        gradLejosDerecho.setTipo(Graduacion.TipoGraduacion.LEJOS);
	        gradLejosDerecho.setFichaGraduacion(ficha);
	        graduaciones.add(gradLejosDerecho);

	        // Lejos IZQUIERDO
	        Graduacion gradLejosIzquierdo = new Graduacion();
	        gradLejosIzquierdo.setOjo(Graduacion.Ojo.IZQUIERDO);
	        gradLejosIzquierdo.setEsferico(0f);
	        gradLejosIzquierdo.setCilindrico(0f);
	        gradLejosIzquierdo.setEje(0f);
	        gradLejosIzquierdo.setTipo(Graduacion.TipoGraduacion.LEJOS);
	        gradLejosIzquierdo.setFichaGraduacion(ficha);
	        graduaciones.add(gradLejosIzquierdo);

	        // Cerca DERECHO
	        Graduacion gradCercaDerecho = new Graduacion();
	        gradCercaDerecho.setOjo(Graduacion.Ojo.DERECHO);
	        gradCercaDerecho.setEsferico(0f);
	        gradCercaDerecho.setCilindrico(0f);
	        gradCercaDerecho.setEje(0f);
	        gradCercaDerecho.setTipo(Graduacion.TipoGraduacion.CERCA);
	        gradCercaDerecho.setFichaGraduacion(ficha);
	        graduaciones.add(gradCercaDerecho);

	        // Cerca IZQUIERDO
	        Graduacion gradCercaIzquierdo = new Graduacion();
	        gradCercaIzquierdo.setOjo(Graduacion.Ojo.IZQUIERDO);
	        gradCercaIzquierdo.setEsferico(0f);
	        gradCercaIzquierdo.setCilindrico(0f);
	        gradCercaIzquierdo.setEje(0f);
	        gradCercaIzquierdo.setTipo(Graduacion.TipoGraduacion.CERCA);
	        gradCercaIzquierdo.setFichaGraduacion(ficha);
	        graduaciones.add(gradCercaIzquierdo);

	        // Guardar todas las graduaciones
	        graduacionDao.saveAll(graduaciones);

	        ficha.setGraduaciones(graduaciones);
	        fichaGraduacionDao.save(ficha);
	    }

	    CristalHistorial nuevoCristal = new CristalHistorial();
	    nuevoCristal.setNombre(nombreCristal);
	    nuevoCristal.setFecha(LocalDate.now());
	    nuevoCristal.setFichaGraduacion(ficha);

	    cristalHistorialDao.save(nuevoCristal);
	}
	

}
