package com.MalloPalermoGuzman.SaldoCero;

import com.MalloPalermoGuzman.SaldoCero.gestores.GestorMusica;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorSonido;
import com.MalloPalermoGuzman.SaldoCero.screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;

public class JuegoPrincipal extends Game {
    public static final float VIRTUAL_WIDTH = 800;
    public static final float VIRTUAL_HEIGHT = 400;
    public SpriteBatch batch;
    public BitmapFont font;
    public GestorMusica gestorMusica;
    public GestorSonido gestorSonido;
    public static Texture cargarTextura(String ruta) {
        Texture texture = new Texture(Gdx.files.internal(ruta), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        return texture;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gestorMusica = new GestorMusica();
        gestorSonido = new GestorSonido();
        gestorSonido.cargarSonidos();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
