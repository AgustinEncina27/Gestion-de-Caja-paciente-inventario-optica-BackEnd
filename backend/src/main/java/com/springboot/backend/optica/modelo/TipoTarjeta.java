package com.springboot.backend.optica.modelo;

import javax.persistence.*;

import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "tipo_tarjeta")
@Data
public class TipoTarjeta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nombre; // Ej: VISA, MASTERCARD, NARANJA, CABAL

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Tipo tipo; // CREDITO o DEBITO

    public enum Tipo {
        CREDITO,
        DEBITO
    }

    private static final long serialVersionUID = 1L;
}
