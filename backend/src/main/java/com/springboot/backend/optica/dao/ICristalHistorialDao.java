package com.springboot.backend.optica.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.backend.optica.modelo.CristalHistorial;


@Repository
public interface ICristalHistorialDao extends JpaRepository<CristalHistorial, Long> {

}
