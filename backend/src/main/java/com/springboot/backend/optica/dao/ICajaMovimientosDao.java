package com.springboot.backend.optica.dao;

import org.springframework.data.domain.Pageable; // ✅ BIEN
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.backend.optica.modelo.CajaMovimiento;

@Repository
public interface ICajaMovimientosDao extends JpaRepository<CajaMovimiento, Long> {

	@Query(
		    "SELECT p FROM CajaMovimiento p " +
		    "WHERE (:idLocal IS NULL OR p.movimiento.local.id = :idLocal) " +
		    "AND (:tipoMovimiento IS NULL OR p.movimiento.tipoMovimiento = :tipoMovimiento) " +
		    "AND (:nroFicha IS NULL OR EXISTS ( " +
		    "    SELECT 1 FROM Paciente pac " +
		    "    WHERE pac = p.movimiento.paciente AND pac.ficha = :nroFicha " +
		    ")) " +
		    "AND (:metodoPago IS NULL OR p.metodoPago.nombre = :metodoPago) " +
		    "AND p.fecha >= :fechaInicio AND p.fecha < :fechaFin " +
		    "ORDER BY p.fecha DESC"
		)
		Page<CajaMovimiento> buscarPagosConMovimiento(
		    @Param("idLocal") Long idLocal,
		    @Param("tipoMovimiento") String tipoMovimiento,
		    @Param("nroFicha") Long nroFicha,
		    @Param("fechaInicio") LocalDateTime fechaInicio,
		    @Param("fechaFin") LocalDateTime fechaFin,
		    @Param("metodoPago") String metodoPago,
		    Pageable pageable
		);


}
