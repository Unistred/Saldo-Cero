package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.MalloPalermoGuzman.SaldoCero.usuarios.Jugador;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private final JuegoPrincipal game;
    private OrthographicCamera camera;
    private Viewport viewport;

    // Nueva clase para renderizar los slots
    private RenderSlots renderSlots;
    private Jugador jugador;

    public GameScreen(JuegoPrincipal game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
        camera.position.set(JuegoPrincipal.VIRTUAL_WIDTH / 2f, JuegoPrincipal.VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        // Crear jugador y renderizador de slots
        jugador = new Jugador("Fran");
        renderSlots = new RenderSlots(game, jugador);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.05f, 0.05f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        // Delegar el renderizado al sistema de slots
        renderSlots.render(delta);

        // Volver al menú
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

    @Override
    public void dispose() {
        renderSlots.dispose();
    }
}
