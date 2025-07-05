package com.springboot.backend.optica.modelo;

public class ProductoResumen {
    public Producto producto;
    public String descripcionCristal;
    public int cantidad;
    public double totalCristal;
    public boolean esCristal;

    // Constructor para productos
    public ProductoResumen(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.esCristal = false;
    }

    // Constructor para cristales adicionales
    public ProductoResumen(String descripcionCristal) {
        this.descripcionCristal = descripcionCristal;
        this.totalCristal = 0.0;
        this.cantidad = 0;
        this.esCristal = true;
    }

    public void agregarCristal(double subtotal) {
        this.totalCristal += subtotal;
        this.cantidad++;
    }
}

