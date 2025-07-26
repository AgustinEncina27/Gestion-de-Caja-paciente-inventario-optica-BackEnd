package com.springboot.backend.optica.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.backend.optica.modelo.Movimiento;

@Repository
public interface IMovimientoDao extends JpaRepository<Movimiento, Long> {
	
	Page<Movimiento> findByTipoMovimiento(String tipo, Pageable pageable);
    
	Page<Movimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
	
	@Query("SELECT m FROM Movimiento m WHERE m.local.id = :idLocal")
	List<Movimiento> findByLocalId(Long idLocal);
	
	@Query("SELECT m FROM Movimiento m WHERE m.local.id = :idLocal")
	Page<Movimiento> findByLocalId(Long idLocal, Pageable pageable);
		
	@Query("SELECT m FROM Movimiento m " +
		       "LEFT JOIN m.cajaMovimientos p " +
		       "LEFT JOIN m.paciente paciente " +
		       "WHERE p IS NULL AND " +
		       "(:idLocal IS NULL OR m.local.id = :idLocal) AND " +
		       "(:tipoMovimiento IS NULL OR m.tipoMovimiento = :tipoMovimiento) AND " +
		       "(:nroFicha IS NULL OR paciente.ficha = :nroFicha) AND " +
		       "m.fecha >= CAST(:fechaInicio AS timestamp)AND m.fecha < CAST(:fechaFin AS timestamp)")
		List<Movimiento> buscarMovimientosSinPagos(
		    @Param("idLocal") Long idLocal,
		    @Param("tipoMovimiento") String tipoMovimiento,
		    @Param("nroFicha") Long nroFicha,
		    @Param("fechaInicio") LocalDateTime fechaInicio,
		    @Param("fechaFin") LocalDateTime fechaFin
		);
	
	@Query("SELECT DISTINCT m FROM Movimiento m " +
			   "LEFT JOIN m.cajaMovimientos p " +
		       "WHERE (:local IS NULL OR m.local.id = :local) " +
		       "AND (cast(:fechaInicio as date) IS NULL OR m.fecha >= :fechaInicio) " +
		       "AND (cast(:fechaFin as date) IS NULL OR m.fecha <= :fechaFin)")
		List<Movimiento> filtrarMovimientos(
		    @Param("local") Long local,
		    @Param("fechaInicio") LocalDateTime fechaInicio,
		    @Param("fechaFin") LocalDateTime fechaFin
		);
	
}
