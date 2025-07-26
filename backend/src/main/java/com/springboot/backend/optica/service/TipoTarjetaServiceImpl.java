package com.springboot.backend.optica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.optica.dao.ITipoTarjetaDao;
import com.springboot.backend.optica.modelo.TipoTarjeta;

@Service
public class TipoTarjetaServiceImpl implements ITipoTarjetaService {
	
	@Autowired
	private ITipoTarjetaDao tipoTarjetaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<TipoTarjeta> listarTodos() {
		return tipoTarjetaDao.findAll();
	}

	@Override
    public TipoTarjeta guardar(TipoTarjeta tipoTarjeta) {
        return tipoTarjetaDao.save(tipoTarjeta);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoTarjeta buscarPorId(Long id) {
        return tipoTarjetaDao.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
    	tipoTarjetaDao.deleteById(id);
    }

}
