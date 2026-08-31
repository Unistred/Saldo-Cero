package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private final JuegoPrincipal game;

    public GameScreen(JuegoPrincipal game) {
        this.game = game;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.05f, 0.05f, 1f);

        game.batch.begin();
        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, "[ PANTALLA DE JUEGO - CASINO ]", 250, 450);
        game.font.draw(game.batch, "Presiona 'M' para volver al Menú", 250, 200);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) { if(width <= 0 || height <= 0) return; }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
