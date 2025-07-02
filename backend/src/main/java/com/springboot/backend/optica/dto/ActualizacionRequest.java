
package com.springboot.backend.optica.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizacionRequest {
    private String tipo; // "precio" o "costo"
    private String tipoCambio; // "porcentaje" o "fijo"
    private float valor;

    private Long categoria;
    private Long proveedor;
    private Long material;
    private Long marca;
}
