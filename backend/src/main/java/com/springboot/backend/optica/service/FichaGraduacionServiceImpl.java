package com.springboot.backend.optica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.backend.optica.dao.FichaGraduacionDao;
import com.springboot.backend.optica.modelo.FichaGraduacion;

@Service
public class FichaGraduacionServiceImpl implements IFichaGraduacionService {

    @Autowired
    private FichaGraduacionDao fichaDao;

    @Override
    public FichaGraduacion save(FichaGraduacion ficha) {
        return fichaDao.save(ficha);
    }

    @Override
    public List<FichaGraduacion> findByPacienteId(Long pacienteId) {
        return fichaDao.findByPacienteIdOrderByFechaDesc(pacienteId);
    }

    @Override
    public FichaGraduacion obtenerUltimaFicha(Long pacienteId) {
        return fichaDao.findTopByPacienteIdOrderByFechaDesc(pacienteId);
    }

}
