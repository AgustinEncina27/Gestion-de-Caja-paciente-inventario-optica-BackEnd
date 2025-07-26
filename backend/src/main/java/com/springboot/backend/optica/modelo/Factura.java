package com.springboot.backend.optica.modelo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "facturas")
@Data
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoComprobante tipoComprobante;

    @Column(nullable = false)
    private Integer puntoVenta;

    @Column(nullable = false)
    private Integer numeroComprobante;

    @Column(length = 14, nullable = false)
    private String cae;

    @Column(nullable = false)
    private LocalDate fechaCaeVencimiento;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCliente tipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movimiento_id", nullable = false)
    @JsonIgnoreProperties({"facturas", "hibernateLazyInitializer", "handler"})
    private Movimiento movimiento;

    private static final long serialVersionUID = 1L;
    
    public enum TipoComprobante {
        FACTURA_A,
        FACTURA_B,
        FACTURA_C,
        NOTA_CREDITO_A,
        NOTA_CREDITO_B,
        NOTA_CREDITO_C,
        NOTA_DEBITO_A,
        NOTA_DEBITO_B,
        NOTA_DEBITO_C
    }
    
    public enum TipoCliente {
        RESPONSABLE_INSCRIPTO,
        MONOTRIBUTISTA,
        CONSUMIDOR_FINAL,
        EXENTO,
        NO_CATEGORIZADO
    }

}

