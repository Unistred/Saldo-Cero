package com.MalloPalermoGuzman.SaldoCero.juegos;

import com.MalloPalermoGuzman.SaldoCero.enums.Simbolo;
import com.MalloPalermoGuzman.SaldoCero.usuarios.Jugador;
import com.badlogic.gdx.math.MathUtils;

public class Slots {
    private int apuesta = 0;
    private Jugador jugador;
    private final Simbolo[] opciones= Simbolo.values();
    private Simbolo rodillo1 = Simbolo.SIETE;
    private Simbolo rodillo2 = Simbolo.SIETE;
    private Simbolo rodillo3 = Simbolo.SIETE;
    private String mensaje = "Presiona ESPACIO para girar";
    public Slots(int apuestaInicial) {
        this.apuesta = apuestaInicial;
    }
    public boolean intentarGirar() {
        if (jugador.descontarSaldo(apuesta) >= 0) {
            mensaje = "Girando...";
            return true;
        } else {
            mensaje = "Saldo insuficiente";
            return false;
        }
    }
    public void girarRodillos(){
        rodillo1 = opciones[MathUtils.random(0, opciones.length - 1)];
        rodillo2 = opciones[MathUtils.random(0, opciones.length - 1)];
        rodillo3 = opciones[MathUtils.random(0, opciones.length - 1)];
    }
    public void evaluarResultado() {
        if (rodillo1 == Simbolo.SIETE && rodillo2 == Simbolo.SIETE && rodillo3 == Simbolo.SIETE) {
            int premio = apuesta * Simbolo.SIETE.getMultiplicador();
            jugador.setSaldo(premio);
            mensaje = "JACKPOT!! Ganaste $" + premio;
        }
        else if (rodillo1 == rodillo2 && rodillo2 == rodillo3) {
            int premio = apuesta * rodillo1.getMultiplicador();
            jugador.setSaldo(premio);
            mensaje = "3 COINCIDENCIAS! Ganaste $" + premio;
        }
        else if (rodillo1 == rodillo2 || rodillo2 == rodillo3 || rodillo1 == rodillo3) {
            int premio = apuesta * 2;
            jugador.setSaldo(premio);
            mensaje = "Par de coincidencias! Ganaste $" + premio;
        }
        else {
            mensaje = "No hubo ninguna coincidencia, intente de nuevo";
        }
    }

    public Simbolo getRodillo1() { return rodillo1; }
    public Simbolo getRodillo2() { return rodillo2; }
    public Simbolo getRodillo3() { return rodillo3; }
    public int getSaldoJugador() { return jugador.getSaldo(); }
    public int getApuesta() { return apuesta; }
    public String getMensaje() { return mensaje; }
}
