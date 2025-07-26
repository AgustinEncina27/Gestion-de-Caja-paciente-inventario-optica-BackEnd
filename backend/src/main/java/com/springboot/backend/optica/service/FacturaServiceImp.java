package com.springboot.backend.optica.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.backend.optica.afip.wsaa.LoginTicketResponse;
import com.springboot.backend.optica.afip.wsaa.WsaaService;
import com.springboot.backend.optica.afip.wsfe.ArrayOfFECAEDetRequest;
import com.springboot.backend.optica.afip.wsfe.FEAuthRequest;
import com.springboot.backend.optica.afip.wsfe.FECAECabRequest;
import com.springboot.backend.optica.afip.wsfe.FECAEDetRequest;
import com.springboot.backend.optica.afip.wsfe.FECAERequest;
import com.springboot.backend.optica.afip.wsfe.FECAEResponse;
import com.springboot.backend.optica.afip.wsfe.ServiceSoap;
import com.springboot.backend.optica.dao.FacturaDao;
import com.springboot.backend.optica.dao.IMovimientoDao;
import com.springboot.backend.optica.dto.SolicitudFacturaDTO;
import com.springboot.backend.optica.modelo.Factura;
import com.springboot.backend.optica.modelo.Movimiento;


@Service
public class FacturaServiceImp implements IFacturaService {
	
	 	@Autowired
	    private IMovimientoDao movimientoRepository;

	    @Autowired
	    private FacturaDao facturaRepository;
	    
	    @Autowired
	    private WsaaService wsaaService;
	    
	    public Factura emitirFactura(SolicitudFacturaDTO solicitud) throws Exception {
	        Movimiento movimiento = movimientoRepository.findById(solicitud.getMovimientoId())
	                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
	        
	        com.springboot.backend.optica.afip.wsfe.Service service = new com.springboot.backend.optica.afip.wsfe.Service();
	        ServiceSoap port = service.getServiceSoap();
	        
	        // 1. Obtener token y sign
	        LoginTicketResponse login = wsaaService.obtenerLoginTicket();

	        // 2. Crear autenticación
	        FEAuthRequest auth = new FEAuthRequest();
	        
	        long ultimoNro = port.feCompUltimoAutorizado(auth, 1, 6).getCbteNro(); // 1 = punto de venta, 6 = tipo Factura B
	        long nuevoNro = ultimoNro + 1;
	        
	        auth.setToken(login.getToken());
	        auth.setSign(login.getSign());
	        auth.setCuit(20304050607L); // Reemplazá con tu CUIT

	        // 3. 
	        // Crear la cabecera
	        FECAECabRequest cabecera = new FECAECabRequest();
	        cabecera.setCantReg(1);         // Cantidad de comprobantes
	        cabecera.setPtoVta(1);          // Punto de venta
	        cabecera.setCbteTipo(6);        // Tipo de comprobante (ej: 6 = Factura B)

	        // Crear el detalle
	        FECAEDetRequest detalle = new FECAEDetRequest();
	        detalle.setConcepto(1);                             // 1 = Productos
	        detalle.setDocTipo(96);                             // 96 = DNI
	        detalle.setDocNro(11222333L);
	        detalle.setCbteDesde(nuevoNro);
	        detalle.setCbteHasta(nuevoNro);
	        detalle.setCbteFch(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
	        detalle.setImpTotal(100.00);
	        detalle.setImpNeto(100.00);
	        detalle.setImpIVA(0.00);
	        detalle.setMonId("PES");
	        detalle.setMonCotiz(1.0);

	        // Crear el array de detalles
	        ArrayOfFECAEDetRequest arrayDetalle = new ArrayOfFECAEDetRequest();
	        arrayDetalle.getFECAEDetRequest().add(detalle);

	        // Crear el request final
	        FECAERequest request = new FECAERequest();
	        request.setFeCabReq(cabecera);
	        request.setFeDetReq(arrayDetalle);

	        // 4. Enviar a AFIP
	        
	        FECAEResponse response = port.fecaeSolicitar(auth, request);
	        
	        if (response.getErrors() != null) {
	            throw new RuntimeException("Error AFIP: " + response.getErrors().getErr().get(0).getMsg());
	        }

	        if (response.getFeDetResp() == null || response.getFeDetResp().getFECAEDetResponse().isEmpty()) {
	            throw new RuntimeException("Respuesta vacía de AFIP");
	        }
	        
	        // 5. Manejar respuesta
	        String cae = response.getFeDetResp().getFECAEDetResponse().get(0).getCAE();
	        String vencimiento = response.getFeDetResp().getFECAEDetResponse().get(0).getCAEFchVto();
	        
	        
	        
	        // 6. Guardar factura
	        Factura factura = new Factura();
	        factura.setTipoComprobante(solicitud.getTipoComprobante());
	        factura.setTipoCliente(solicitud.getTipoCliente());
	        factura.setMovimiento(movimiento);
	        factura.setNumeroComprobante((int) nuevoNro);
	        factura.setPuntoVenta(1);
	        factura.setCae(cae);
	        factura.setFechaCaeVencimiento(LocalDate.parse(vencimiento, DateTimeFormatter.BASIC_ISO_DATE));
	        factura.setFechaEmision(LocalDate.now());

	        return facturaRepository.save(factura);
	    }
}
