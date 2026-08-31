package com.MalloPalermoGuzman.SaldoCero.enums;

public enum Simbolo {
    SIETE("777", 10),
    CEREZA("Cereza", 5),
    CAMPANA("Campana", 3),
    DIAMANTE("Diamante", 2),
    BAR("BAR", 2);

    private final String nombre;
    private final int multiplicador;

    Simbolo(String nombre, int multiplicador) {
        this.nombre = nombre;
        this.multiplicador = multiplicador;
    }

    public String getNombre() { return nombre; }
    public int getMultiplicador() { return multiplicador; }
}
