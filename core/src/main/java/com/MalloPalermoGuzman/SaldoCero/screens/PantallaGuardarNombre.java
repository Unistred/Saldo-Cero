package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.MalloPalermoGuzman.SaldoCero.baseDeDatos.GestorBD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PantallaGuardarNombre implements Screen {

    private static final int MAX_CARACTERES = 15;

    private final JuegoPrincipal game;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private final StringBuilder nombre = new StringBuilder();
    private String mensaje = "";
    private float tiempo = 0f;

    public PantallaGuardarNombre(JuegoPrincipal game) {
        this.game = game;
        if (game.nombreJugador != null) {
            nombre.append(game.nombreJugador);
        }
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
        camera.position.set(JuegoPrincipal.VIRTUAL_WIDTH / 2f, JuegoPrincipal.VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char c) {
                if (c == '\b') {
                    if (nombre.length() > 0) nombre.deleteCharAt(nombre.length() - 1);
                } else if (c == '\r' || c == '\n') {
                    return false;
                } else if (Character.isLetterOrDigit(c) || c == ' ' || c == '_') {
                    if (nombre.length() < MAX_CARACTERES) nombre.append(c);
                }
                mensaje = "";
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    guardar();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    irAlMenu();
                    return true;
                }
                return false;
            }
        });
    }

    private void guardar() {
        String limpio = nombre.toString().trim();
        if (limpio.isEmpty()) {
            mensaje = "Escribi un nombre";
            return;
        }
        int id = GestorBD.guardarNombreJugador(limpio);
        if (id < 0) {
            mensaje = "No se pudo guardar el nombre";
            return;
        }
        game.nombreJugador = limpio;
        game.idJugador = id;
        irAlMapa();
    }

    private void irAlMapa() {
        Gdx.input.setInputProcessor(null);
        game.setScreen(new Mapa(game));
        dispose();
    }

    private void irAlMenu() {
        game.setScreen(new MainMenuScreen(game));
        dispose();
    }

    @Override
    public void render(float delta) {
        tiempo += delta;
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        float centroX = viewport.getWorldWidth() / 2f;
        String cursor = ((int) (tiempo * 2) % 2 == 0) ? "_" : " ";

        game.batch.begin();
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Guarda tu nombre antes de salir", centroX - 100, 280);
        game.font.draw(game.batch, "Nombre: " + nombre + cursor, centroX - 100, 200);
        game.font.setColor(Color.LIGHT_GRAY);
        game.font.draw(game.batch, "ENTER: guardar y jugar", centroX - 100, 120);
        game.font.draw(game.batch, "ESC: volver al menu sin guardar", centroX - 100, 95);
        if (!mensaje.isEmpty()) {
            game.font.setColor(Color.SALMON);
            game.font.draw(game.batch, mensaje, centroX - 100, 160);
        }
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