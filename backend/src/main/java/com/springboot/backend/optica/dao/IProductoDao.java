package com.springboot.backend.optica.dao;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.backend.optica.modelo.Producto;

public interface IProductoDao extends JpaRepository<Producto, Long> {
	
	@Query("SELECT pl.producto FROM ProductoLocal pl WHERE pl.local.id = :localId  ORDER BY pl.producto.marca.nombre ASC")
	Page<Producto> findProductosByLocalId(@Param("localId") Long localId, Pageable pageable);
	
	@Query("SELECT pl.producto FROM ProductoLocal pl WHERE pl.local.id = :localId AND LOWER(pl.producto.marca.nombre) LIKE LOWER(CONCAT('%', :marca, '%'))")
	List<Producto> findByMarcaAndLocalNoEstricto(@Param("marca") String marca, @Param("localId") Long localId);

	@Query("SELECT pl.producto FROM ProductoLocal pl " +
		       "WHERE LOWER(pl.producto.modelo) LIKE LOWER(CONCAT('%', :modelo, '%')) " +
		       "AND pl.local.id = :localId")
		List<Producto> findByModeloAndLocal(@Param("modelo") String modelo, @Param("localId") Long localId);
	
	@Query("SELECT p FROM Producto p WHERE LOWER(p.modelo) = LOWER(:modelo)")
	List<Producto> findByModelo(@Param("modelo") String modelo);
	
	@Query("SELECT p FROM Producto p WHERE LOWER(p.modelo) LIKE LOWER(:modelo)")
	List<Producto> findByModeloNoEstricto(@Param("modelo") String modelo);
	
	@Query("SELECT p FROM Producto p WHERE LOWER(p.marca.nombre) LIKE LOWER(:marca)")
	List<Producto> findByMarcaNoEstricto(@Param("marca") String marca);

    
    @Query("SELECT p FROM Producto p ORDER BY p.marca.nombre ASC")
    Page<Producto> findAllProducto(Pageable pageable);
	
	@Query("SELECT DISTINCT p FROM Producto p WHERE p.genero LIKE :generoSeleccionado")
	public Page<Producto> findByGenero(String generoSeleccionado, Pageable pageable);
	
	@Query("SELECT DISTINCT p FROM Producto p " +
		       "LEFT JOIN p.categorias c " +
		       "LEFT JOIN p.proveedores prov " +
		       "WHERE (:categoriaId IS NULL OR c.id = :categoriaId) " +
		       "AND (:proveedorId IS NULL OR prov.id = :proveedorId) " +
		       "AND (:materialId IS NULL OR p.material.id = :materialId) " +
		       "AND (:marcaId IS NULL OR p.marca.id = :marcaId)")
		List<Producto> findByFiltros(
		    @Param("categoriaId") Long categoriaId,
		    @Param("proveedorId") Long proveedorId,
		    @Param("materialId") Long materialId,
		    @Param("marcaId") Long marcaId
		);
	
	@Query("SELECT DISTINCT p FROM Producto p " +
		       "LEFT JOIN p.categorias c " +
		       "WHERE (:genero IS NULL OR p.genero = :genero) " +
		       "AND (:marca IS NULL OR p.marca.id = :marca) " +
		       "AND (:categoria IS NULL OR c.id = :categoria) ORDER BY p.marca.nombre ASC")
	Page<Producto> findByGeneroAndMarcaAndCategoria(String genero, Long marca, Long categoria, Pageable pageable);
	
	@Query("SELECT COUNT(p) > 0 FROM Producto p " +
	       "WHERE LOWER(p.modelo) = LOWER(:modelo) " + 
	       "AND p.marca.id = :marcaId " +
	       "AND p.id != :productoId")
	boolean existsByModeloAndMarca(@Param("modelo") String modelo, @Param("marcaId") Long marcaId, @Param("productoId") Long productoId);

	@Query("SELECT LOWER(p.modelo) FROM Producto p WHERE LOWER(p.modelo) IN :modelos AND p.marca.id = :marcaId")
	List<String> findModelosExistentes(@Param("modelos") List<String> modelos, @Param("marcaId") Long marcaId);

	
}
