package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {

    private final JuegoPrincipal game;

    private OrthographicCamera camera;
    private Viewport viewport;

    public MainMenuScreen(JuegoPrincipal game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
        camera.position.set(JuegoPrincipal.VIRTUAL_WIDTH / 2f, JuegoPrincipal.VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(Color.RED);
        game.font.draw(game.batch, "=== SALDO CERO ===", JuegoPrincipal.VIRTUAL_WIDTH / 2 - 80, JuegoPrincipal.VIRTUAL_HEIGHT - 80);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Presiona ENTER para iniciar nueva partida", JuegoPrincipal.VIRTUAL_WIDTH / 2 - 160, JuegoPrincipal.VIRTUAL_HEIGHT / 2);
        game.font.draw(game.batch, "Presiona ESC para salir", JuegoPrincipal.VIRTUAL_WIDTH / 2 - 90, JuegoPrincipal.VIRTUAL_HEIGHT / 2 - 50);

        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
