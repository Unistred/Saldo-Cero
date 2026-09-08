package com.MalloPalermoGuzman.SaldoCero.usuarios;

public class Jugador {
    private String nombre;
    private int saldo = 500;
    private int cordura = 100;

    public Jugador(String nombre) {
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
    public void setSaldo(int num){
        this.saldo = num;
    }
}
