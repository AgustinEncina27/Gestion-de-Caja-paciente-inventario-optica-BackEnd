package com.springboot.backend.optica.dto;

public class StockPorMaterialDTO {
    private String materialNombre;
    private int stockTotal;

    public StockPorMaterialDTO(String materialNombre, int stockTotal) {
        this.materialNombre = materialNombre;
        this.stockTotal = stockTotal;
    }

    // Getters y Setters
    public String getMaterialNombre() { return materialNombre; }
    public void setMaterialNombre(String materialNombre) { this.materialNombre = materialNombre; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }
}

