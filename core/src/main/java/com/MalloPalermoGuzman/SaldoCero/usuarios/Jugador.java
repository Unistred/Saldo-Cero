package com.MalloPalermoGuzman.SaldoCero.usuarios;

public class Jugador {
    private String nombre;
    private int saldo = 0;
    private int cordura = 100;

    public Jugador(String nombre, int saldo, int cordura) {
        this.nombre = nombre;
    }

    public int getSaldo() {
        return saldo;
    }

    public int getCordura() {
        return cordura;
    }
    public int descontarSaldo(int descontado){
        return saldo - descontado;
    }
    public void setSaldo(int descontado){
        this.saldo = saldo - descontado;
    }
}
