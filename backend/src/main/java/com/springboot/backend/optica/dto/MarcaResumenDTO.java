package com.springboot.backend.optica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarcaResumenDTO {
    private String marcaNombre;
    private int cantidadVendida;
    private double gananciaTotal;

}