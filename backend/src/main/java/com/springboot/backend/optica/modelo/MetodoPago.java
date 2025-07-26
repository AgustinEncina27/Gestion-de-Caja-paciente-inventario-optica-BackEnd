package com.springboot.backend.optica.modelo;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "metodos_pago")
@Data
public class MetodoPago implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 30, nullable = false)
	private TipoMetodoPago tipo; // Enum: CONTADO, CHEQUE, TARJETA_CREDITO, etc.
			
	private static final long serialVersionUID = 1L;
    
    public enum TipoMetodoPago {
        CONTADO,
        CHEQUE,
        TARJETA_CREDITO,
        TARJETA_DEBITO,
        CUENTA_CORRIENTE,
        OTRA,
        TRANSFERENCIA
    }
}
