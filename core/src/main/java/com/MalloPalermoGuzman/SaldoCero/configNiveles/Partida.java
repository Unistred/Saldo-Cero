package com.MalloPalermoGuzman.SaldoCero.configNiveles;

import com.MalloPalermoGuzman.SaldoCero.logros.Logro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Partida {
    private static final int DEUDA_POR_DIA = 5000;

    private final ConfiguracionJuego config;

    private int indiceNivel;
    private int saldo;
    private int saldoInicialDia;
    private int deuda;
    private boolean deudaAplicada;
    private float cordura;
    private float tiempoRestante;
    private float tiempoTotal;
    private float penalizacionAcumulada;

    // Logros marcados en esta partida: pendientes de persistir y ya notificados en sesion.
    private final Set<Logro> logrosPendientes = new LinkedHashSet<>();
    private final Set<Logro> logrosSesion = new HashSet<>();

    public Partida(ConfiguracionJuego config) {
        this.config = config;
        reiniciar();
    }

    public void reiniciar() {
        indiceNivel = 0;
        saldo = config.getSaldoInicial();
        deuda = 0;
        tiempoTotal = 0f;
        penalizacionAcumulada = 0f;
        logrosPendientes.clear();
        logrosSesion.clear();
        iniciarNivel();
    }

    private void iniciarNivel() {
        ConfigNivel nivel = getNivel();
        saldoInicialDia = saldo;
        deudaAplicada = false;
        cordura = Math.max(0f, nivel.getCorduraInicial() - penalizacionAcumulada);
        tiempoRestante = nivel.getTiempoSegundos();
    }

    public void actualizarTiempo(float delta) {
        float usado = Math.min(delta, tiempoRestante);
        if (usado <= 0f) return;
        tiempoRestante -= usado;
        tiempoTotal += usado;
    }

    public boolean tiempoAgotado() {
        return tiempoRestante <= 0f;
    }

    public void modificarSaldo(int delta) {
        saldo += delta;
        if (saldo < 0) saldo = 0;
        if (saldo == 0) marcarLogro(Logro.SALDO_CERO);
    }

    public void descontarCordura(float cantidad) {
        cordura = Math.max(0f, cordura - cantidad);
    }

    public void descontarCorduraPorApuesta() {
        descontarCordura(getNivel().getCorduraPorApuesta());
    }

    public boolean metaCumplida() {
        return saldo >= getNivel().getMetaSaldo();
    }

    public boolean terminarNivel() {
        boolean cumplida = metaCumplida();
        if (!cumplida) {
            int penalizacion = getNivel().getPenalizacionCordura();
            penalizacionAcumulada += penalizacion;
            descontarCordura(penalizacion);
        }
        // Cada cierre de dia suma 5k a la deuda y entrega ese mismo monto al jugador:
        // tras cerrar el dia 1 -> 5000, dia 2 -> 10000, dia 3 -> 15000. El guardia evita duplicarlo.
        if (!deudaAplicada) {
            deuda += DEUDA_POR_DIA;
            modificarSaldo(DEUDA_POR_DIA);
            deudaAplicada = true;
            marcarLogro(Logro.PRIMERA_DEUDA);
        }
        return cumplida;
    }

    public boolean hayNivelSiguiente() {
        return indiceNivel < config.getCantidadNiveles() - 1;
    }

    public void avanzarNivel() {
        if (!hayNivelSiguiente()) return;
        indiceNivel++;
        iniciarNivel();
    }

    public ConfigNivel getNivel() { return config.getNivel(indiceNivel); }
    public int getIndiceNivel() { return indiceNivel; }
    public int getNumeroNivel() { return indiceNivel + 1; }
    public int getSaldo() { return saldo; }
    public int getSaldoInicialDia() { return saldoInicialDia; }
    public int getDeuda() { return deuda; }
    public float getCordura() { return cordura; }
    public int getCorduraRedondeada() { return Math.round(cordura); }
    public float getTiempoRestante() { return tiempoRestante; }
    public float getTiempoTotal() { return tiempoTotal; }
    public ConfiguracionJuego getConfig() { return config; }

    /** Marca un logro una sola vez por partida y lo deja pendiente de guardar en BD. */
    public void marcarLogro(Logro logro) {
        if (logro != null && logrosSesion.add(logro)) {
            logrosPendientes.add(logro);
        }
    }

    public List<Logro> getLogrosPendientes() { return new ArrayList<>(logrosPendientes); }

    public void confirmarLogros(Collection<Logro> guardados) {
        logrosPendientes.removeAll(guardados);
    }

    public String getTiempoRestanteTexto() { return formatear(tiempoRestante); }
    public String getTiempoTotalTexto() { return formatear(tiempoTotal); }

    private static String formatear(float segundos) {
        int total = (int) segundos;      // descarta los decimales
        int minutos = total / 60;
        int seg = total % 60;

        String texto = "";
        if (minutos < 10) {
            texto = texto + "0";         // para que se vea 05 y no 5
        }
        texto = texto + minutos + ":";
        if (seg < 10) {
            texto = texto + "0";
        }
        texto = texto + seg;
        return texto;
    }
}
