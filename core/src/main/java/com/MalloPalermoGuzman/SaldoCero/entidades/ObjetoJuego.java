package com.MalloPalermoGuzman.SaldoCero.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class ObjetoJuego {
    private Texture textura;
    private Rectangle hitbox;

    public ObjetoJuego(String rutaTextura, float x, float y, float widthCelda, float heightCelda) {
        this.textura = rutaTextura != null ? new Texture(rutaTextura) : null;
        this.hitbox = new Rectangle(x, y, widthCelda, heightCelda);
    }
    public abstract void update(float deltaTime);

    public void render(SpriteBatch batch) {
        if (textura != null) {
            batch.draw(textura, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
    }
    public Rectangle getHitbox() {return hitbox;}
    public Texture getTextura(){
        return textura;
    }
    public void dispose() {
        if (textura != null) textura.dispose();
    }
}