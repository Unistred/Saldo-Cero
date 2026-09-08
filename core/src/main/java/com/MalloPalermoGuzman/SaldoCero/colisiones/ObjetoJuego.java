package com.MalloPalermoGuzman.SaldoCero.colisiones;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class ObjetoJuego {
    private Texture textura;
    private Rectangle hitbox;

    public ObjetoJuego(String rutaTextura, float x, float y) {
    this.textura = new Texture(rutaTextura);
    this.hitbox = new Rectangle(x, y, textura.getWidth(), textura.getHeight());
    }
    public abstract void update(float deltaTime);

     public void render(SpriteBatch batch) {
    batch.draw(textura, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
}
public Rectangle getHitbox() {return hitbox;}
public Texture getTextura(){
         return textura;
}
    public void dispose() {
    textura.dispose();
    }
}