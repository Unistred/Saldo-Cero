package com.MalloPalermoGuzman.SaldoCero.controles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class ControladorEntrada {
    private final Vector2 direccion = new Vector2();

    public Vector2 getDireccionMovimiento() {
        float dx = 0;
        float dy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1;
        direccion.set(dx, dy);
        if (!direccion.isZero()) direccion.nor();
        return direccion;
    }

    public boolean quiereInteractuar() {
        return Gdx.input.isKeyJustPressed(Input.Keys.E);
    }

    public boolean quiereGirarRodillosSlot() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }

    public boolean quiereVolver() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    // avanzar pagina de dialogo / continuar en pantallas de texto.
    public boolean quiereAvanzar() {
        return quiereGirarRodillosSlot()
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.E);
    }

    // confirmar una pregunta (por ejemplo terminar el dia). No incluye la E para no confirmar sin querer.
    public boolean quiereConfirmar() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }

    // terminar el nivel antes de tiempo (salida anticipada).
    public boolean quiereSalirDelNivel() {
        return Gdx.input.isKeyJustPressed(Input.Keys.X);
    }

    public boolean quiereSubirApuesta() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W);
    }

    public boolean quiereBajarApuesta() {
        return Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S);
    }

    public boolean quiereSubirVolumen() {
        return Gdx.input.isKeyJustPressed(Input.Keys.PLUS)
                || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)
                || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET);
    }

    public boolean quiereBajarVolumen() {
        return Gdx.input.isKeyJustPressed(Input.Keys.MINUS)
                || Gdx.input.isKeyJustPressed(Input.Keys.SLASH);
    }

    public boolean quiereSilenciar() {
        return Gdx.input.isKeyJustPressed(Input.Keys.N);
    }
}
