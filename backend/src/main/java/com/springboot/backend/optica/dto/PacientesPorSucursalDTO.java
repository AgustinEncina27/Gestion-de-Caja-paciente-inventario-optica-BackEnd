package com.springboot.backend.optica.dto;

public class PacientesPorSucursalDTO {
    private Long localId;
    private String localNombre;
    private int cantidadPacientes;

    public PacientesPorSucursalDTO(Long localId, String localNombre, int cantidadPacientes) {
        this.localId = localId;
        this.localNombre = localNombre;
        this.cantidadPacientes = cantidadPacientes;
    }

    // Getters y Setters
    public Long getLocalId() { return localId; }
    public void setLocalId(Long localId) { this.localId = localId; }

    public String getLocalNombre() { return localNombre; }
    public void setLocalNombre(String localNombre) { this.localNombre = localNombre; }

    public int getCantidadPacientes() { return cantidadPacientes; }
    public void setCantidadPacientes(int cantidadPacientes) { this.cantidadPacientes = cantidadPacientes; }
}

