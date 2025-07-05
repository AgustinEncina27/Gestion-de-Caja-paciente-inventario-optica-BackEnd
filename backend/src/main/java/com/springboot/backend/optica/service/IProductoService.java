package com.springboot.backend.optica.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.backend.optica.dto.ActualizacionRequest;
import com.springboot.backend.optica.dto.StockPorMaterialDTO;
import com.springboot.backend.optica.dto.StockTotalSucursalDTO;
import com.springboot.backend.optica.modelo.Producto;



public interface IProductoService {
	
	public List<Producto> findAllProducto();
	
	public Page<Producto> findAllProducto(Pageable pageable);
	
	public Page<Producto> findByGeneroAndMarcaAndCategoria(String genero, Long marca, Long categoria, Pageable pageable);
	
	public Producto findById(Long id);
		
	public Producto save(Producto producto);
	
	public void deleteNote(Long id);
	
	List<Producto> findByModelo(String modelo);
	
	public List<Producto> findByModeloNoEstricto(String modelo) ;
    
    public boolean setStockByLocal(Long productoId, Long localId, Integer stock);
        
    public boolean existsByModeloAndMarca(String modelo, Long marcaId, Long id);
    
    public List<String> obtenerModelosExistentes(List<String> modelos, Long marcaId);
    
    public List<Producto> findByMarcaNoEstricto(String marca);
	
    public List<StockTotalSucursalDTO> obtenerStockTotalPorSucursal();
    
    public List<StockPorMaterialDTO> obtenerStockPorMaterialYSucursal(Long localId);
    
    public Page<Producto> findAllByLocal(Long localId, Pageable pageable);
    
    public List<Producto> findByMarcaAndLocalNoEstricto(String marca, Long localId);
    
    public byte[] exportStockToExcel(Long localId) throws IOException;
    
    public List<Producto> findByModeloAndLocal(String modelo, Long localId);
           
    public void actualizarMasivo(ActualizacionRequest req);

}
