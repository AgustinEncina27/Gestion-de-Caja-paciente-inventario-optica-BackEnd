package com.springboot.backend.optica.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.optica.dao.ILocalDao;
import com.springboot.backend.optica.dao.IProductoDao;
import com.springboot.backend.optica.dao.IProductoLocalDao;
import com.springboot.backend.optica.dto.StockPorMaterialDTO;
import com.springboot.backend.optica.dto.StockTotalSucursalDTO;
import com.springboot.backend.optica.modelo.Local;
import com.springboot.backend.optica.modelo.Producto;
import com.springboot.backend.optica.modelo.ProductoLocal;

import java.io.IOException;

import com.springboot.backend.optica.service.ProductoServiceImp;

@Service
public class ProductoServiceImp implements IProductoService {
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IProductoLocalDao productoLocalRepository; 
	
	@Autowired
	private ILocalDao localRepository; 
	
	@Autowired
	private ExcelServiceImp excelService;
		
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAllProducto() {
		return productoDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAllProducto(Pageable pageable) {
		Page<Producto> productos = productoDao.findAllProducto(pageable);
	    // Cargar relaciones de productoLocales manualmente
	    productos.getContent().forEach(producto -> producto.getProductoLocales().size());
	    return productos;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAllByLocal(Long localId, Pageable pageable) {
		return productoDao.findProductosByLocalId(localId, pageable);
	}
	
	@Transactional(readOnly = true)
	public List<ProductoLocal> getStockByLocal(Long localId) {
        return productoLocalRepository.findByLocalId(localId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByMarcaAndLocalNoEstricto(String marca, Long localId) {
	    return productoDao.findByMarcaAndLocalNoEstricto(marca, localId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StockPorMaterialDTO> obtenerStockPorMaterialYSucursal(Long localId) {
        List<Object[]> resultados = productoLocalRepository.obtenerStockPorMaterialYSucursal(localId);
        List<StockPorMaterialDTO> stockPorMaterial = new ArrayList<>();

        for (Object[] fila : resultados) {
            StockPorMaterialDTO dto = new StockPorMaterialDTO(
                (String) fila[0],               // materialNombre
                ((Number) fila[1]).intValue()    // stockTotal
            );
            stockPorMaterial.add(dto);
        }

        return stockPorMaterial;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByModelo(String modelo) {
	    return productoDao.findByModelo(modelo);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByModeloNoEstricto(String modelo) {
		modelo= "%"+modelo+"%";
	    return productoDao.findByModeloNoEstricto(modelo);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByMarcaNoEstricto(String marca) {
		marca= "%"+marca+"%";
	    return productoDao.findByMarcaNoEstricto(marca);
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
	public byte[] exportStockToExcel(Long localId) throws IOException {
	    List<ProductoLocal> productoLocales = productoLocalRepository.findByLocalId(localId);
	    return excelService.generarExcelStockPorLocal(productoLocales);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByModeloAndMarca(String modelo, Long marcaId, Long id) {
	    return productoDao.existsByModeloAndMarca(modelo, marcaId,id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StockTotalSucursalDTO> obtenerStockTotalPorSucursal() {
        List<Object[]> resultados = productoLocalRepository.obtenerStockTotalPorSucursal();
        List<StockTotalSucursalDTO> stockPorSucursal = new ArrayList<>();

        for (Object[] fila : resultados) {
            StockTotalSucursalDTO dto = new StockTotalSucursalDTO(
                ((Number) fila[0]).longValue(),  // localId
                (String) fila[1],               // localNombre
                ((Number) fila[2]).intValue()    // stockTotal
            );
            stockPorSucursal.add(dto);
        }

        return stockPorSucursal;
    }

	@Override
	@Transactional
	public boolean setStockByLocal(Long productoId, Long localId, Integer stock) {
	    Optional<ProductoLocal> productoLocal = productoLocalRepository.findByProductoIdAndLocalId(productoId, localId);
	    if (productoLocal.isPresent()) {
	        ProductoLocal existing = productoLocal.get();
	        existing.setStock(stock);
	        productoLocalRepository.save(existing);
	        return true;
	    } else {
	        Optional<Producto> producto = productoDao.findById(productoId);
	        Optional<Local> local = localRepository.findById(localId);
	        if (producto.isPresent() && local.isPresent()) {
	            ProductoLocal newProductoLocal = new ProductoLocal();
	            newProductoLocal.setProducto(producto.get());
	            newProductoLocal.setLocal(local.get());
	            newProductoLocal.setStock(stock);
	            productoLocalRepository.save(newProductoLocal);
	            return true;
	        }
	    }
	    return false;
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
