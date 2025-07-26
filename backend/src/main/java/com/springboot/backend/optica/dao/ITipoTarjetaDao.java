package com.springboot.backend.optica.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.backend.optica.modelo.TipoTarjeta;

public interface ITipoTarjetaDao extends JpaRepository<TipoTarjeta, Long> {

}

