package com.MalloPalermoGuzman.SaldoCero.configNiveles;

public class Partida {
    private final ConfiguracionJuego config;

    private int indiceNivel;
    private int saldo;
    private float cordura;
    private float tiempoRestante;
    private float tiempoTotal;
    private float penalizacionAcumulada;

    public Partida(ConfiguracionJuego config) {
        this.config = config;
        reiniciar();
    }

    // partida nueva: primer nivel, saldo inicial, tiempo total en cero.
    public void reiniciar() {
        indiceNivel = 0;
        saldo = config.getSaldoInicial();
        tiempoTotal = 0f;
        penalizacionAcumulada = 0f;
        iniciarNivel();
    }

    private void iniciarNivel() {
        ConfigNivel nivel = getNivel();
        cordura = Math.max(0f, nivel.getCorduraInicial() - penalizacionAcumulada);
        tiempoRestante = nivel.getTiempoSegundos();
    }

    public void actualizarTiempo(float delta) {
        tiempoRestante = Math.max(0f, tiempoRestante - delta);
        tiempoTotal += delta;
    }

    public boolean tiempoAgotado() {
        return tiempoRestante <= 0f;
    }

    public void modificarSaldo(int delta) {
        saldo += delta;
        if (saldo < 0) saldo = 0;
    }

    public void descontarCordura(float cantidad) {
        cordura = Math.max(0f, cordura - cantidad);
    }

    // descuenta la cordura fija que cuesta cada apuesta en este nivel.
    public void descontarCorduraPorApuesta() {
        descontarCordura(getNivel().getCorduraPorApuesta());
    }

    public boolean metaCumplida() {
        return saldo >= getNivel().getMetaSaldo();
    }

    /*
     * se llama al terminar el nivel (con x o por tiempo).
     * si no se llego al objetivo, aplica la penalizacion de cordura (y la arrastra a los siguientes niveles).
     * @return true si se cumplio la meta
     */
    public boolean terminarNivel() {
        boolean cumplida = metaCumplida();
        if (!cumplida) {
            int penalizacion = getNivel().getPenalizacionCordura();
            penalizacionAcumulada += penalizacion;
            descontarCordura(penalizacion);
        }
        return cumplida;
    }

    public boolean hayNivelSiguiente() {
        return indiceNivel < config.getCantidadNiveles() - 1;
    }

    // pasa al siguiente nivel: el saldo se conserva, tiempo y cordura se reinician.
    public void avanzarNivel() {
        if (!hayNivelSiguiente()) return;
        indiceNivel++;
        iniciarNivel();
    }

    public ConfigNivel getNivel() { return config.getNivel(indiceNivel); }
    public int getIndiceNivel() { return indiceNivel; }
    public int getNumeroNivel() { return indiceNivel + 1; }
    public int getSaldo() { return saldo; }
    public float getCordura() { return cordura; }
    public int getCorduraRedondeada() { return Math.round(cordura); }
    public float getTiempoRestante() { return tiempoRestante; }
    public float getTiempoTotal() { return tiempoTotal; }
    public ConfiguracionJuego getConfig() { return config; }

    // "minutos:segundos" para el hud y la pantalla final
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
