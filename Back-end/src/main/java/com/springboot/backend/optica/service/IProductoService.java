package com.springboot.backend.optica.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.backend.optica.modelo.Producto;



public interface IProductoService {
	
	public List<Producto> findAllProducto();
	
	public Page<Producto> findAllProducto(Pageable pageable);
	
	Page<Producto> findByGeneroAndMarcaAndCategoria(String genero, Long marca, Long categoria, Pageable pageable);
	
	public Producto findById(Long id);
	
	public Producto findBySimbolo(String simbolo);	
	
	public Producto save(Producto producto);
	
	public void deleteNote(Long id);
		
}
