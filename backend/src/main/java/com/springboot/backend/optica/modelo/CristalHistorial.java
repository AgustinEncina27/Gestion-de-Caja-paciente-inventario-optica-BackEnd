package com.springboot.backend.optica.modelo;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "cristales_historial")
@Data
public class CristalHistorial implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_graduacion_id", nullable = false)
    @JsonIgnoreProperties({"graduaciones", "cristales", "hibernateLazyInitializer", "handler"})
    private FichaGraduacion fichaGraduacion;
}
