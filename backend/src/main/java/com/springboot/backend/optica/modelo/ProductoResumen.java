package com.springboot.backend.optica.modelo;

public class ProductoResumen {
	  public Producto producto;
	    public String descripcionCristal;
	    public int cantidad;
	    public double total; // ✅ nuevo campo para acumular subtotales reales
	    public double totalCristal;
	    public boolean esCristal;

	    public ProductoResumen(Producto producto, int cantidad) {
	        this.producto = producto;
	        this.cantidad = cantidad;
	        this.total = 0.0;
	        this.esCristal = false;
	    }

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

	    public void agregarSubtotal(double subtotal, int cantidad) {
	        this.total += subtotal;
	        this.cantidad += cantidad;
	    }
}

