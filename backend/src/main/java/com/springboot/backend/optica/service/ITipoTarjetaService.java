package com.springboot.backend.optica.service;

import java.util.List;

import com.springboot.backend.optica.modelo.TipoTarjeta;

public interface ITipoTarjetaService {
	List<TipoTarjeta> listarTodos();

    // CRUD opcional
    TipoTarjeta guardar(TipoTarjeta tipoTarjeta);
    TipoTarjeta buscarPorId(Long id);
    void eliminar(Long id);
}
