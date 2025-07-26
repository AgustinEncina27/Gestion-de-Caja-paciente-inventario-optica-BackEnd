package com.springboot.backend.optica.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.backend.optica.modelo.TarjetaDetalle;

public interface ITarjetaDetalleDao extends JpaRepository<TarjetaDetalle, Long> {

}
