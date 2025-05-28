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
	            .max(Comparator.comparing(FichaGraduacion::getFecha))
	            .orElse(null);

	    if (ficha == null) {
	        ficha = new FichaGraduacion();
	        ficha.setFecha(LocalDate.now());
	        ficha.setPaciente(paciente);
	        ficha = fichaGraduacionDao.save(ficha); // guardar explícitamente

	        // Crear graduación ojo DERECHO
	        Graduacion gradDerecho = new Graduacion();
	        gradDerecho.setOjo(Graduacion.Ojo.DERECHO);
	        gradDerecho.setEsferico(0f);
	        gradDerecho.setCilindrico(0f);
	        gradDerecho.setEje(0f);
	        gradDerecho.setAdicion(0f);
	        gradDerecho.setCerca(0f);
	        gradDerecho.setFichaGraduacion(ficha);

	        // Crear graduación ojo IZQUIERDO
	        Graduacion gradIzquierdo = new Graduacion();
	        gradIzquierdo.setOjo(Graduacion.Ojo.IZQUIERDO);
	        gradIzquierdo.setEsferico(0f);
	        gradIzquierdo.setCilindrico(0f);
	        gradIzquierdo.setEje(0f);
	        gradIzquierdo.setAdicion(0f);
	        gradIzquierdo.setCerca(0f);
	        gradIzquierdo.setFichaGraduacion(ficha);

	        // Guardar ambas graduaciones
	        graduacionDao.save(gradDerecho);
	        graduacionDao.save(gradIzquierdo);
	    }

	    CristalHistorial nuevoCristal = new CristalHistorial();
	    nuevoCristal.setNombre(nombreCristal);
	    nuevoCristal.setFecha(LocalDate.now());
	    nuevoCristal.setFichaGraduacion(ficha);

	    cristalHistorialDao.save(nuevoCristal);
	}
	

}
