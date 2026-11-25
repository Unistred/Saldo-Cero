package com.MalloPalermoGuzman.SaldoCero.logros;

/** Logros internos del juego. El tag es lo que se guarda en la base de datos. */
public enum Logro {
    PRIMERA_DEUDA("Primera deuda", "Tomaste prestamo y aparecio la primera deuda"),
    SALDO_CERO("Saldo cero", "Llegaste a $0 o menos");

    private final String titulo;
    private final String descripcion;

    Logro(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTag() { return name(); }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
}
