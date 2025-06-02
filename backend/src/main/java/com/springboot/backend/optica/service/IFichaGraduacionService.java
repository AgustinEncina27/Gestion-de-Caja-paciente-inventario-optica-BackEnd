package com.springboot.backend.optica.service;

import java.util.List;

import com.springboot.backend.optica.modelo.FichaGraduacion;

public interface IFichaGraduacionService {
	FichaGraduacion save(FichaGraduacion ficha);
    List<FichaGraduacion> findByPacienteId(Long pacienteId);
    FichaGraduacion obtenerUltimaFicha(Long pacienteId);
}
