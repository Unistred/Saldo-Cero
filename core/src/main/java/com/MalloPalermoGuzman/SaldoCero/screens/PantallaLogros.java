package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.MalloPalermoGuzman.SaldoCero.baseDeDatos.GestorBD;
import com.MalloPalermoGuzman.SaldoCero.logros.Logro;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;
import java.util.Set;

/** Vista de logros desbloqueados e historial acumulado, leidos desde SQLite con SELECT. */
public class PantallaLogros implements Screen {

    private final JuegoPrincipal game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private boolean primerFrame = true;

    private Set<String> desbloqueados;
    private List<String> historial;
    private String resumenApuestas;

    public PantallaLogros(JuegoPrincipal game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, camera);
        camera.position.set(JuegoPrincipal.VIRTUAL_WIDTH / 2f, JuegoPrincipal.VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        // El menu usa un Stage: se libera el procesador para que los ENTER del menu no se repitan aca.
        Gdx.input.setInputProcessor(null);

        desbloqueados = GestorBD.obtenerLogrosDesbloqueados(game.idJugador);
        historial = GestorBD.obtenerHistorialLogros(game.idJugador);
        resumenApuestas = GestorBD.obtenerResumenApuestas(game.idJugador);
    }

    @Override
    public void render(float delta) {
        if (primerFrame) {
            primerFrame = false;
        } else if (game.controladorEntrada.quiereVolver() || game.controladorEntrada.quiereAvanzar()) {
            volverAlMenu();
            return;
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        float escalaAnterior = game.font.getData().scaleX;
        game.font.getData().setScale(0.9f);

        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, "LOGROS", 60, 360);

        float y = 330f;
        for (Logro logro : Logro.values()) {
            boolean desbloqueado = desbloqueados.contains(logro.getTag());
            game.font.setColor(desbloqueado ? Color.GREEN : Color.LIGHT_GRAY);
            game.font.draw(game.batch, (desbloqueado ? "[X] " : "[ ] ") +
                    logro.getTitulo() + " - " + logro.getDescripcion(), 60, y);
            y -= 26f;
        }

        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Historial acumulado:", 60, 240);
        game.font.setColor(Color.LIGHT_GRAY);
        if (historial.isEmpty()) {
            game.font.draw(game.batch, "Sin registros todavia", 80, 214);
        } else {
            y = 214f;
            for (String fila : historial) {
                game.font.draw(game.batch, fila, 80, y);
                y -= 22f;
                if (y < 150f) break;
            }
        }

        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, resumenApuestas, 60, 120);
        if (game.idJugador < 0) {
            game.font.setColor(Color.SALMON);
            game.font.draw(game.batch, "Guarda tu nombre para registrar logros y apuestas", 60, 90);
        }
        game.font.setColor(Color.GRAY);
        game.font.draw(game.batch, "ESC / ENTER: volver al menu", 60, 40);

        game.font.getData().setScale(escalaAnterior);
        game.batch.end();
    }

    private void volverAlMenu() {
        game.setScreen(new MainMenuScreen(game));
        dispose();
    }

    @Override public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }
}
