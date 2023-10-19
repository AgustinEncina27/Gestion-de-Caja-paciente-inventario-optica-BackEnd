package com.springboot.backend.optica.dao;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.backend.optica.modelo.Producto;

public interface IProductoDao extends JpaRepository<Producto, Long> {
	
	public Producto findByModelo (String simbolo);
	
	@Query("SELECT p FROM Producto p WHERE p.genero LIKE :generoSeleccionado")
	public Page<Producto> findByGenero(String generoSeleccionado, Pageable pageable);
	
	@Query("SELECT p FROM Producto p " +
		       "LEFT JOIN p.categorias c " +
		       "WHERE (:genero IS NULL OR p.genero = :genero) " +
		       "AND (:marca IS NULL OR p.marca.id = :marca) " +
		       "AND (:categoria IS NULL OR c.id = :categoria)")
	Page<Producto> findByGeneroAndMarcaAndCategoria(String genero, Long marca, Long categoria, Pageable pageable);

}
