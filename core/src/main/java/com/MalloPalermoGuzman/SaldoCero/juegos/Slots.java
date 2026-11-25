package com.MalloPalermoGuzman.SaldoCero.juegos;

import com.MalloPalermoGuzman.SaldoCero.configNiveles.ConfigNivel;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.Partida;
import com.MalloPalermoGuzman.SaldoCero.enums.Simbolo;
import com.badlogic.gdx.math.MathUtils;

public class Slots {
    private final Partida partida;
    private final int[] apuestas;
    private int indiceApuesta = 0;
    private final float probabilidadPerder;
    private final Simbolo[] opciones= Simbolo.values();
    private Simbolo rodillo1 = Simbolo.SIETE;
    private Simbolo rodillo2 = Simbolo.SIETE;
    private Simbolo rodillo3 = Simbolo.SIETE;
    private String mensaje = "Presiona ESPACIO para girar";

    // Datos de la ultima tirada, para registrar la apuesta al terminar el giro o al salir.
    private int apuestaGirada;
    private int ganadoGirado;
    private boolean registroPendiente;

    public Slots(Partida partida) {
        this.partida = partida;
        ConfigNivel nivel = partida.getNivel();
        this.apuestas = nivel.getApuestas();
        this.probabilidadPerder = nivel.getProbabilidadPerder();
        ajustarApuesta();
    }

    public int getApuesta() { return apuestas[indiceApuesta]; }

    public void subirApuesta() {
        if (indiceApuesta + 1 < apuestas.length && apuestas[indiceApuesta + 1] <= partida.getSaldo()) {
            indiceApuesta++;
        }
    }

    public void bajarApuesta() {
        if (indiceApuesta > 0) {
            indiceApuesta--;
        }
    }

    private void ajustarApuesta() {
        while (indiceApuesta > 0 && apuestas[indiceApuesta] > partida.getSaldo()) {
            indiceApuesta--;
        }
    }

    public boolean intentarGirar() {
        int apuesta = getApuesta();
        if (partida.getSaldo() >= apuesta) {
            partida.modificarSaldo(-apuesta);
            partida.descontarCorduraPorApuesta();
            apuestaGirada = apuesta;
            ganadoGirado = 0;
            registroPendiente = true;
            mensaje = "Girando...";
            return true;
        } else {
            mensaje = "Saldo insuficiente";
            return false;
        }
    }

    public void girarRodillos(){
        boolean debePerder = MathUtils.random() < probabilidadPerder;
        do {
            sortearRodillos();
        } while (hayCoincidencia() == debePerder);
    }

    private void sortearRodillos() {
        rodillo1 = opciones[MathUtils.random(0, opciones.length - 1)];
        rodillo2 = opciones[MathUtils.random(0, opciones.length - 1)];
        rodillo3 = opciones[MathUtils.random(0, opciones.length - 1)];
    }

    private boolean hayCoincidencia() {
        return rodillo1 == rodillo2 || rodillo2 == rodillo3 || rodillo1 == rodillo3;
    }

    public void evaluarResultado() {
        // Se paga sobre lo que realmente se aposto al girar, no sobre la apuesta vigente del panel.
        int apuesta = apuestaGirada;
        int ganado = 0;
        if (rodillo1 == Simbolo.SIETE && rodillo2 == Simbolo.SIETE && rodillo3 == Simbolo.SIETE) {
            ganado = apuesta * Simbolo.SIETE.getMultiplicador();
            partida.modificarSaldo(ganado);
            mensaje = "JACKPOT!! Ganaste $" + ganado;
        }
        else if (rodillo1 == rodillo2 && rodillo2 == rodillo3) {
            ganado = apuesta * rodillo1.getMultiplicador();
            partida.modificarSaldo(ganado);
            mensaje = "3 COINCIDENCIAS! Ganaste $" + ganado;
        }
        else if (hayCoincidencia()) {
            ganado = apuesta * 2;
            partida.modificarSaldo(ganado);
            mensaje = "Par de coincidencias! Ganaste $" + ganado;
        }
        else {
            mensaje = "No hubo ninguna coincidencia, intente de nuevo";
        }
        ganadoGirado = ganado;
        ajustarApuesta();
    }

    public Simbolo getRodillo1() { return rodillo1; }
    public Simbolo getRodillo2() { return rodillo2; }
    public Simbolo getRodillo3() { return rodillo3; }
    public int getSaldoJugador() { return partida.getSaldo(); }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) {this.mensaje = mensaje;}

    public int getUltimaApuesta() { return apuestaGirada; }
    public int getUltimoGanado() { return ganadoGirado; }
    public boolean isRegistroPendiente() { return registroPendiente; }
    public void marcarRegistrado() { registroPendiente = false; }
}
