package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    private final JuegoPrincipal game;

    public MainMenuScreen(JuegoPrincipal game) {
        this.game = game;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        game.batch.begin();
        game.font.setColor(Color.RED);
        game.font.draw(game.batch, "=== SALDO CERO ===", 250, 400);

        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Presiona ENTER para iniciar nueva partida", 200, 300);
        game.font.draw(game.batch, "Presiona ESC para salir", 200, 250);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override public void resize(int width, int height) { if(width <= 0 || height <= 0) return; }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
