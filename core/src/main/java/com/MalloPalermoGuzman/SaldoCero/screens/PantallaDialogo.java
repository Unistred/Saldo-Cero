package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PantallaDialogo implements Screen {
    private static final float ANCHO_TEXTO = 640f;
    private static final float MARGEN_X = 80f;
    private static final float ESCALA_TEXTO = 1.5f;

    private final JuegoPrincipal game;
    private final String titulo;
    private final String[] paginas;
    private final Runnable alTerminar;

    private int paginaActual = 0;
    private boolean primerFrame = true;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private final GlyphLayout layout = new GlyphLayout();

    public PantallaDialogo(JuegoPrincipal game, String titulo, String[] paginas, Runnable alTerminar) {
        this.game = game;
        this.titulo = titulo;
        this.paginas = paginas;
        this.alTerminar = alTerminar;
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
        if (primerFrame) {
            primerFrame = false;
        } else if (game.controladorEntrada.quiereAvanzar()) {
            paginaActual++;
            if (paginaActual >= paginas.length) {
                alTerminar.run();
                return;
            }
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, titulo, MARGEN_X, 360);

        game.font.getData().setScale(ESCALA_TEXTO);
        layout.setText(game.font, paginas[paginaActual], Color.WHITE, ANCHO_TEXTO, Align.left, true);
        game.font.draw(game.batch, layout, MARGEN_X, 290);
        game.font.getData().setScale(1f);

        game.font.setColor(Color.GRAY);
        game.font.draw(game.batch, "ESPACIO / ENTER: continuar   (" + (paginaActual + 1) + "/" + paginas.length + ")", MARGEN_X, 40);
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
