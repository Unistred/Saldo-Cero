package com.MalloPalermoGuzman.SaldoCero.configNiveles;

/** Datos de un nivel (viernes, sabado o domingo), leidos de assets/datos/niveles.json. */
public class ConfigNivel {
    private final String nombre;
    private final float tiempoSegundos;
    private final int corduraInicial;
    private final int corduraPorApuesta;
    private final int metaSaldo;
    private final int penalizacionCordura;
    private final float probabilidadPerder; // 0.0 a 1.0
    private final int[] apuestas;
    private final String[] dialogoInicio;
    private final String[] dialogoCierre;

    public ConfigNivel(String nombre, float tiempoSegundos, int corduraInicial, int corduraPorApuesta,
                       int metaSaldo, int penalizacionCordura, float probabilidadPerder,
                       int[] apuestas, String[] dialogoInicio, String[] dialogoCierre) {
        this.nombre = nombre;
        this.tiempoSegundos = tiempoSegundos;
        this.corduraInicial = corduraInicial;
        this.corduraPorApuesta = corduraPorApuesta;
        this.metaSaldo = metaSaldo;
        this.penalizacionCordura = penalizacionCordura;
        this.probabilidadPerder = probabilidadPerder;
        this.apuestas = apuestas;
        this.dialogoInicio = dialogoInicio;
        this.dialogoCierre = dialogoCierre;
    }

    public String getNombre() { return nombre; }
    public float getTiempoSegundos() { return tiempoSegundos; }
    public int getCorduraInicial() { return corduraInicial; }
    public int getCorduraPorApuesta() { return corduraPorApuesta; }
    public int getMetaSaldo() { return metaSaldo; }
    public int getPenalizacionCordura() { return penalizacionCordura; }
    public float getProbabilidadPerder() { return probabilidadPerder; }
    public int[] getApuestas() { return apuestas; }
    public String[] getDialogoInicio() { return dialogoInicio; }
    public String[] getDialogoCierre() { return dialogoCierre; }
}
