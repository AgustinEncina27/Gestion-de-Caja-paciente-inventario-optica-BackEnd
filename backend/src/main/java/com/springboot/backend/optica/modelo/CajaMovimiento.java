package com.springboot.backend.optica.modelo;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "caja_movimientos")
@Data
public class CajaMovimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private double montoImpuesto;
    
    @Column
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodo_pago_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MetodoPago metodoPago;
    
	@Column(length = 100)
	private String descripcionOtras; // Por ejemplo, nombre del banco, observaciones, etc.
	
	// Relación uno a uno opcional con detalles de tarjeta
	@OneToOne(mappedBy = "cajaMovimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"cajaMovimiento", "hibernateLazyInitializer", "handler"})
	private TarjetaDetalle tarjetaDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movimiento_id", nullable = false)
    @JsonIgnoreProperties({"detalles", "cajaMovimientos", "hibernateLazyInitializer", "handler"})
    private Movimiento movimiento;

    private static final long serialVersionUID = 1L;
}
