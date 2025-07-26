package com.springboot.backend.optica.modelo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import java.io.Serializable;


@Entity
@Table(name = "tarjeta_detalle")
@Data
public class TarjetaDetalle implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_tarjeta_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoTarjeta tipoTarjeta;
	
	@Column(length = 100)
	private String nombreOtro;

    @Column(length = 20)
    private String numero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_movimiento_id", nullable = false)
    @JsonIgnoreProperties({"tarjetaDetalle", "metodoPago", "hibernateLazyInitializer", "handler"})
    private CajaMovimiento cajaMovimiento;
    
    private static final long serialVersionUID = 1L;
}
