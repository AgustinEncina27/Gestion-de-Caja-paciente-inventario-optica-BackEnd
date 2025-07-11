package com.springboot.backend.optica.modelo;

import java.io.Serializable;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;

@Entity
@Table(name = "graduaciones")
@Data
public class Graduacion implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Ojo ojo; // DERECHO o IZQUIERDO
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_graduacion")
    private TipoGraduacion tipo;

    private Float esferico;
    private Float cilindrico;
    private Float eje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_graduacion_id", nullable = false)
    @JsonIgnoreProperties({"graduaciones", "cristales", "hibernateLazyInitializer", "handler"})
    private FichaGraduacion fichaGraduacion;
    
    private static final long serialVersionUID = 1L;
    
    public enum Ojo {
        DERECHO,
        IZQUIERDO
    }
    
    public enum TipoGraduacion {
        LEJOS,
        CERCA
    }
}
