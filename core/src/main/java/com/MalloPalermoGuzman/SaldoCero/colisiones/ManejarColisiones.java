package com.MalloPalermoGuzman.SaldoCero.colisiones;
import com.badlogic.gdx.math.Intersector;
public class ManejarColisiones {
    private ManejarColisiones(){}
    public static boolean verificarChoque(ObjetoJuego obj1, ObjetoJuego obj2) {
    boolean hayChoque = Intersector.overlaps(obj1.getHitbox(), obj2.getHitbox());
        if (hayChoque){
    System.out.println("se esta chocando");
    return true;
    }else {
            System.out.println("no se esta chocando");
            return false;
        }}
}
