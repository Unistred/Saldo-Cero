package com.MalloPalermoGuzman.SaldoCero.colisiones;
import com.MalloPalermoGuzman.SaldoCero.entidades.ObjetoJuego;
import com.badlogic.gdx.math.Intersector;
public class ManejarColisiones {
    private ManejarColisiones(){}
    public static boolean verificarChoque(ObjetoJuego obj1, ObjetoJuego obj2) {
        if (obj1 == null || obj2 == null) return false;
        return Intersector.overlaps(obj1.getHitbox(), obj2.getHitbox());
}}
