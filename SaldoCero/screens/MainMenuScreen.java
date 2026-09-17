package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {

    private final JuegoPrincipal game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;

    private Texture logoSaldoCero;
    private Texture startMenu;
    private Texture exitMenu;

    private ImageButton botonStart;
    private ImageButton botonExit;

    public MainMenuScreen(JuegoPrincipal game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        logoSaldoCero = JuegoPrincipal.cargarTextura("texturas/menu/logoMenuSaldoCero.png");
        startMenu     = JuegoPrincipal.cargarTextura("texturas/menu/startMenu.png");
        exitMenu      = JuegoPrincipal.cargarTextura("texturas/menu/exitMenu.png");
        TextureRegion regionStart = new TextureRegion(startMenu, 0, 0, 64, 64);
        TextureRegion regionExit = new TextureRegion(exitMenu, 0, 0, 64, 64);
        botonStart = new ImageButton(new TextureRegionDrawable(new TextureRegion(regionStart)));
        botonExit  = new ImageButton(new TextureRegionDrawable(new TextureRegion(regionExit)));

        botonStart.setSize(64, 64);
        botonExit.setSize(64, 64);

        botonStart.setPosition(viewport.getWorldWidth() / 2f - botonStart.getWidth() / 2f, viewport.getWorldHeight() / 2f);
        botonExit.setPosition(viewport.getWorldWidth() / 2f - botonExit.getWidth() / 2f, viewport.getWorldHeight() / 2f - 100);

        stage.addActor(botonStart);
        stage.addActor(botonExit);

        botonStart.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        botonExit.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(logoSaldoCero, (viewport.getWorldWidth() - 128 ) / 2f, viewport.getWorldHeight() - 150, 128, 128);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        logoSaldoCero.dispose();
        startMenu.dispose();
        exitMenu.dispose();
        stage.dispose();
    }
}
