package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private final JuegoPrincipal game;

    private OrthographicCamera camera;
    private Viewport viewport;

    public GameScreen(JuegoPrincipal game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.05f, 0.05f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, "[ PANTALLA DE JUEGO - CASINO ]", JuegoPrincipal.VIRTUAL_WIDTH / 2 - 120, JuegoPrincipal.VIRTUAL_HEIGHT - 50);
        game.font.draw(game.batch, "Presiona 'M' para volver al Menú", JuegoPrincipal.VIRTUAL_WIDTH / 2 - 130, 100);

        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
