package com.MalloPalermoGuzman.SaldoCero.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Jugador extends ObjetoJuego {
    private String nombre;
    private int saldo = 500;
    private int cordura = 100;
    private float x, y;
    private int velocidad;
    private float anteriorX, anteriorY;
    public Jugador(float x, float y, String nombre) {
        super("texturas/personajes/jugador/jugadorAnimacionAD.png", x, y);
        this.x = x;
        this.y = y;
        this.nombre = nombre;
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
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) this.x += velocidad * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  this.x -= velocidad * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    this.y += velocidad * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  this.y -= velocidad * deltaTime;
        getHitbox().setPosition(this.x, this.y);
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
}

