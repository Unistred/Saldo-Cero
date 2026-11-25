package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PantallaFinal implements Screen {
    private static final float ANCHO_TEXTO = 640f;
    private static final float MARGEN_X = 80f;
    private static final float ESCALA_TEXTO = 1.5f;

    private final JuegoPrincipal game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private final GlyphLayout layout = new GlyphLayout();
    private boolean primerFrame = true;

    public PantallaFinal(JuegoPrincipal game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
        camera.position.set(JuegoPrincipal.VIRTUAL_WIDTH / 2f, JuegoPrincipal.VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        // Evita que el ENTER que cerro el ultimo dialogo salte esta pantalla en el primer frame.
        if (primerFrame) {
            primerFrame = false;
        } else if (game.controladorEntrada.quiereAvanzar()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
            return;
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.font.getData().setScale(ESCALA_TEXTO);
        layout.setText(game.font, game.configuracion.getFraseFinal(), Color.SALMON, ANCHO_TEXTO, Align.left, true);
        game.font.draw(game.batch, layout, MARGEN_X, 290);
        game.font.getData().setScale(1f);

        game.font.setColor(Color.LIGHT_GRAY);
        game.font.draw(game.batch, "Tiempo total de la partida: " + game.partida.getTiempoTotalTexto(), MARGEN_X, 120);
        game.font.setColor(Color.GRAY);
        game.font.draw(game.batch, "ENTER: volver al menu", MARGEN_X, 40);
        game.font.setColor(Color.WHITE);
        game.batch.end();
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
