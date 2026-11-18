package com.MalloPalermoGuzman.SaldoCero.entidades;

import com.MalloPalermoGuzman.SaldoCero.enums.Direccion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Jugador extends ObjetoJuego {
    private String nombre;
    private int saldo = 500;
    private int cordura = 100;
    private float x, y;
    private int velocidad = 150;
    private float anteriorX, anteriorY;
    private Animation<TextureRegion> animDerecha, animIzquierda, animArriba, animAbajo;
    private Direccion mirando = Direccion.ABAJO;
    private float tiempoAnimacion = 0f;
    public Jugador(float x, float y, String nombre) {
        super("texturas/personajes/jugador/jugadorAnimacionWASD.png", x, y, 25, 28);
        this.x = x;
        this.y = y;
        this.nombre = nombre;
        cargarAnimaciones();
    }
    public void deshacerMovimiento(){
        this.x = anteriorX;
        this.y = anteriorY;
        getHitbox().setPosition(this.x,this.y);
    }
    // Requerido por ObjetoJuego: sin direccion no hay movimiento.
    @Override
    public void update(float deltaTime) {
        update(deltaTime, Vector2.Zero);
    }

    /** direccion: ya normalizada (largo 1 o 0), la entrega ControladorEntrada. */
    public void update(float deltaTime, Vector2 direccion) {
        anteriorY = this.y;
        anteriorX = this.x;
        this.x += direccion.x * velocidad * deltaTime;
        this.y += direccion.y * velocidad * deltaTime;
        actualizarAnimacion(deltaTime, this.x - anteriorX, this.y - anteriorY);
        getHitbox().setPosition(this.x, this.y);
    }
    private void cargarAnimaciones() {
        TextureRegion[][] celdas = TextureRegion.split(getTextura(), 25, 28);

        TextureRegion[] derecha = celdas[0];
        TextureRegion[] abajo   = celdas[1];
        TextureRegion[] arriba  = celdas[2];
        TextureRegion[] izquierda = new TextureRegion[derecha.length];
        for (int i = 0; i < derecha.length; i++) {
            izquierda[i] = new TextureRegion(derecha[i]);
            izquierda[i].flip(true, false);
        }
        animDerecha   = new Animation<>(0.1f, derecha);
        animIzquierda = new Animation<>(0.1f, izquierda);
        animAbajo     = new Animation<>(0.1f, abajo);
        animArriba    = new Animation<>(0.1f, arriba);
    }
    private void actualizarAnimacion(float delta, float dx, float dy) {
        boolean seMueve = dx != 0 || dy != 0;
        if (seMueve) {
            if (dx > 0)      mirando = Direccion.DERECHA;
            else if (dx < 0) mirando = Direccion.IZQUIERDA;
            else if (dy > 0) mirando = Direccion.ARRIBA;
            else             mirando = Direccion.ABAJO;
            tiempoAnimacion += delta;
        } else {
            tiempoAnimacion = 0f;
        }
    }

    private TextureRegion cuadroActual() {
        Animation<TextureRegion> anim = switch (mirando) {
            case DERECHA   -> animDerecha;
            case IZQUIERDA -> animIzquierda;
            case ARRIBA    -> animArriba;
            case ABAJO     -> animAbajo;
        };
        return anim.getKeyFrame(tiempoAnimacion, true);
    }
    @Override
    public void render(SpriteBatch batch) {
        Rectangle hb = getHitbox();
        batch.draw(cuadroActual(), hb.x, hb.y, hb.width, hb.height);
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
    public void descontarCordura(int descontado){
        this.cordura -= descontado;
    }
}