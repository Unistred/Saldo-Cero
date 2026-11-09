package com.MalloPalermoGuzman.SaldoCero.entidades;

import com.MalloPalermoGuzman.SaldoCero.enums.Direccion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

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
    @Override
    public void update(float deltaTime) {
        anteriorY = this.y;
        anteriorX = this.x;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) this.x += velocidad * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))  this.x -= velocidad * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))    this.y += velocidad * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))  this.y -= velocidad * deltaTime;
        actualizarAnimacion(deltaTime, this.x - anteriorX, this.y - anteriorY);
        getHitbox().setPosition(this.x, this.y);
    }
    private void cargarAnimaciones() {
        // Corta la hoja en celdas: celdas[fila][columna]
        TextureRegion[][] celdas = TextureRegion.split(getTextura(), 25, 28);

        TextureRegion[] derecha = celdas[0];
        TextureRegion[] abajo   = celdas[1];
        TextureRegion[] arriba  = celdas[2];

        // Izquierda: COPIAS de los cuadros de derecha, espejadas una sola vez
        TextureRegion[] izquierda = new TextureRegion[derecha.length];
        for (int i = 0; i < derecha.length; i++) {
            izquierda[i] = new TextureRegion(derecha[i]); // copia
            izquierda[i].flip(true, false);               // espejo horizontal
        }

        animDerecha   = new Animation<>(0.1f, derecha);
        animIzquierda = new Animation<>(0.1f, izquierda);
        animAbajo     = new Animation<>(0.1f, abajo);
        animArriba    = new Animation<>(0.1f, arriba);
    }
    // Se llama desde update(), con el movimiento (dx, dy) que ya calculás ahí
    private void actualizarAnimacion(float delta, float dx, float dy) {
        boolean seMueve = dx != 0 || dy != 0;
        if (seMueve) {
            // En diagonal gana la dirección horizontal
            if (dx > 0)      mirando = Direccion.DERECHA;
            else if (dx < 0) mirando = Direccion.IZQUIERDA;
            else if (dy > 0) mirando = Direccion.ARRIBA;
            else             mirando = Direccion.ABAJO;
            tiempoAnimacion += delta;
        } else {
            tiempoAnimacion = 0f; // quieto: se queda en el primer cuadro de la última dirección
        }
    }

    private TextureRegion cuadroActual() {
        Animation<TextureRegion> anim = switch (mirando) {
            case DERECHA   -> animDerecha;
            case IZQUIERDA -> animIzquierda;
            case ARRIBA    -> animArriba;
            case ABAJO     -> animAbajo;
        };
        return anim.getKeyFrame(tiempoAnimacion, true); // true = en bucle
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


