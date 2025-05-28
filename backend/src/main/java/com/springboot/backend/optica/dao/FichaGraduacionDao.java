package com.springboot.backend.optica.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.backend.optica.modelo.FichaGraduacion;


@Repository
public interface FichaGraduacionDao extends JpaRepository<FichaGraduacion, Long> {
	List<FichaGraduacion> findByPacienteIdOrderByFechaDesc(Long pacienteId);
    FichaGraduacion findTopByPacienteIdOrderByFechaDesc(Long pacienteId);
}
