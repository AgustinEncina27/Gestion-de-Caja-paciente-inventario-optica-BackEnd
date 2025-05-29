package com.springboot.backend.optica.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.backend.optica.modelo.Graduacion;

@Repository
public interface GraduacionDao extends JpaRepository<Graduacion, Long> {

}
