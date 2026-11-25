package com.MalloPalermoGuzman.SaldoCero.entidades;

import com.MalloPalermoGuzman.SaldoCero.enums.Direccion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Jugador extends ObjetoJuego {
    // punto donde aparece el jugador al empezar cada nivel
    public static final float INICIO_X = 512;
    public static final float INICIO_Y = 640;

    /** Un sprite por dia de la partida (filas 0/1/2: derecha, abajo/S, arriba/W). */
    private static final String[] SPRITES_POR_DIA = {
        "texturas/personajes/jugador/jugadorAnimacionWASD.png",
        "texturas/personajes/jugador/jugadorAnimacionWASD_cansado.png",
        "texturas/personajes/jugador/jugadorAnimacionWASD_muy_cansado.png"
    };

    private String nombre;
    private String rutaTexturaActual;
    private float x, y;
    private int velocidad = 150;
    private float anteriorX, anteriorY;
    private Animation<TextureRegion> animDerecha, animIzquierda, animArriba, animAbajo;
    private Direccion mirando = Direccion.ABAJO;
    private float tiempoAnimacion = 0f;
    public Jugador(float x, float y, String nombre) {
        this(x, y, nombre, 1);
    }

    public Jugador(float x, float y, String nombre, int numeroDia) {
        super(rutaParaDia(numeroDia), x, y, 25, 28);
        this.rutaTexturaActual = rutaParaDia(numeroDia);
        this.x = x;
        this.y = y;
        this.nombre = nombre;
        cargarAnimaciones();
    }

    private static String rutaParaDia(int numeroDia) {
        int dia = Math.max(1, Math.min(SPRITES_POR_DIA.length, numeroDia));
        return SPRITES_POR_DIA[dia - 1];
    }

    /** Degrada la apariencia segun el dia: 1 normal, 2 cansado, 3 muy cansado. */
    public void actualizarDia(int numeroDia) {
        String ruta = rutaParaDia(numeroDia);
        if (ruta.equals(rutaTexturaActual)) return;
        cambiarTextura(ruta);
        rutaTexturaActual = ruta;
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
    // pone al jugador en una posicion (por ejemplo al empezar un nivel) y lo deja mirando hacia abajo
    public void colocarEn(float nuevoX, float nuevoY){
        this.x = nuevoX;
        this.y = nuevoY;
        this.anteriorX = nuevoX;
        this.anteriorY = nuevoY;
        this.mirando = Direccion.ABAJO;
        this.tiempoAnimacion = 0f;
        getHitbox().setPosition(this.x, this.y);
    }
}