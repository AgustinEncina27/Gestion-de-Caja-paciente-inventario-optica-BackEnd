package com.springboot.backend.optica.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.backend.optica.modelo.ProductoLocal;

public interface IProductoLocalDao extends JpaRepository<ProductoLocal, Long> {
    Optional<ProductoLocal> findByProductoIdAndLocalId(Long productoId, Long localId);
    
    List<ProductoLocal> findByLocalId(Long localId);
    
    @Query("SELECT pl.local.id, pl.local.nombre, SUM(pl.stock) " +
            "FROM ProductoLocal pl " +
            "GROUP BY pl.local.id, pl.local.nombre " +
            "ORDER BY pl.local.id")
     List<Object[]> obtenerStockTotalPorSucursal();
     
     @Query("SELECT pl.producto.material.nombre, SUM(pl.stock) " +
             "FROM ProductoLocal pl " +
             "WHERE pl.local.id = :localId " +
             "GROUP BY pl.producto.material.nombre " +
             "ORDER BY pl.producto.material.nombre")
      List<Object[]> obtenerStockPorMaterialYSucursal(@Param("localId") Long localId);
}
