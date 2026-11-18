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
    public boolean quiereSubirVolumen() {
        return Gdx.input.isKeyJustPressed(Input.Keys.PLUS);
    }
    public boolean quiereBajarVolumen() {
        return Gdx.input.isKeyJustPressed(Input.Keys.MINUS);
    }
    public boolean quiereSilenciar() {
        return Gdx.input.isKeyJustPressed(Input.Keys.N);
    }
}