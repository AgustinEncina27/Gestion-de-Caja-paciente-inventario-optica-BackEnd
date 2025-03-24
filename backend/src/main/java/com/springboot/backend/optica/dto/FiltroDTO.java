package com.springboot.backend.optica.dto;

import java.time.LocalDate;

public class FiltroDTO {
    private Long local;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Constructor vacío (necesario para Spring Boot)
    public FiltroDTO() {}

    // Constructor con parámetros
    public FiltroDTO(Long local, String tipoMovimiento, LocalDate fechaInicio, LocalDate fechaFin, String metodoPago) {
        this.local = local;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public Long getLocal() {
        return local;
    }

    public void setLocal(Long local) {
        this.local = local;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

}