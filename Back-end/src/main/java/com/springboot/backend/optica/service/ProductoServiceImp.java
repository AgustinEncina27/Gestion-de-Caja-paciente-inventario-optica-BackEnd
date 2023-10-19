package com.springboot.backend.optica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.optica.dao.IProductoDao;
import com.springboot.backend.optica.modelo.Producto;

@Service
public class ProductoServiceImp implements IProductoService {
	
	@Autowired
	private IProductoDao productoDao;
		
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAllProducto() {
		return productoDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAllProducto(Pageable pageable) {
		return productoDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findByGeneroAndMarcaAndCategoria(String genero, Long marca, Long categoria, Pageable pageable) {
		return productoDao.findByGeneroAndMarcaAndCategoria(genero,marca,categoria,pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return productoDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Producto findBySimbolo(String simbolo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Producto save(Producto producto) {
		return productoDao.save(producto);
	}

	@Override
	@Transactional
	public void deleteNote(Long id) {
		Producto note = productoDao.findById(id).get();
		productoDao.delete(note);
	}

}
