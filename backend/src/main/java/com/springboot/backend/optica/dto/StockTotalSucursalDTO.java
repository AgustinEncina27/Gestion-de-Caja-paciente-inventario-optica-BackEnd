package com.springboot.backend.optica.dto;

public class StockTotalSucursalDTO {
    private Long localId;
    private String localNombre;
    private int stockTotal;

    public StockTotalSucursalDTO(Long localId, String localNombre, int stockTotal) {
        this.localId = localId;
        this.localNombre = localNombre;
        this.stockTotal = stockTotal;
    }

    // Getters y Setters
    public Long getLocalId() { return localId; }
    public void setLocalId(Long localId) { this.localId = localId; }

    public String getLocalNombre() { return localNombre; }
    public void setLocalNombre(String localNombre) { this.localNombre = localNombre; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }
}
