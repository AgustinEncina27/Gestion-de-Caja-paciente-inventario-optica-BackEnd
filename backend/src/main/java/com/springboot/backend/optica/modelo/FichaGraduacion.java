package com.springboot.backend.optica.modelo;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "fichas_graduacion")
@Data
public class FichaGraduacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    // 📏 Medidas del paciente
    private Float dnpDerecho;
    private Float dnpIzquierdo;
    private Float alturaPupilarDerecho;
    private Float alturaPupilarIzquierdo;
    private Float alturaPelicula;

    // 🕶️ Medidas del armazón
    private Float puente;
    private Float diagonalMayor;
    private Float largo;
    private Float alturaArmazon;

    // 🔗 Relación con paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonIgnoreProperties({"historialFichas", "hibernateLazyInitializer", "handler"})
    private Paciente paciente;

    // 👁️ Lista de graduaciones (por ojo)
    @OneToMany(mappedBy = "fichaGraduacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"fichaGraduacion", "hibernateLazyInitializer", "handler"})
    private List<Graduacion> graduaciones;

    // 🧾 Lista de cristales vendidos asociados a esta ficha
    @OneToMany(mappedBy = "fichaGraduacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"fichaGraduacion", "hibernateLazyInitializer", "handler"})
    private List<CristalHistorial> cristales;

    private static final long serialVersionUID = 1L;
}
