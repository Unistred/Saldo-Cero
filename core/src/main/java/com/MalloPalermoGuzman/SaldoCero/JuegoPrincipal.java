package com.MalloPalermoGuzman.SaldoCero;

import com.MalloPalermoGuzman.SaldoCero.controles.ControladorEntrada;
import com.MalloPalermoGuzman.SaldoCero.entidades.Jugador;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorMusica;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorSonido;
import com.MalloPalermoGuzman.SaldoCero.screens.MainMenuScreen;
import com.MalloPalermoGuzman.SaldoCero.screens.PantallaGuardarNombre;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;

public class JuegoPrincipal extends Game {
    public static final float VIRTUAL_WIDTH = 800;
    public static final float VIRTUAL_HEIGHT = 400;
    public SpriteBatch batch;
    public BitmapFont font;
    public ControladorEntrada controladorEntrada;
    public GestorMusica gestorMusica;
    public GestorSonido gestorSonido;
    public Jugador jugador;
    public String nombreJugador = "";
    public int idJugador = -1;
    public static Texture cargarTextura(String ruta) {
        Texture texture = new Texture(Gdx.files.internal(ruta), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        return texture;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        controladorEntrada = new ControladorEntrada();
        gestorMusica = new GestorMusica();
        gestorSonido = new GestorSonido();
        gestorSonido.cargarSonidos();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        procesarControlesVolumen();
        super.render();
    }
    private void procesarControlesVolumen() {
        // En la pantalla de nombre se escribe texto: la N, el - y el + no deben tocar el volumen.
        if (getScreen() instanceof PantallaGuardarNombre) return;
        if (controladorEntrada.quiereSubirVolumen()) {
            gestorMusica.subirVolumen();
            gestorSonido.subirVolumen();
        }
        if (controladorEntrada.quiereBajarVolumen()) {
            gestorMusica.bajarVolumen();
            gestorSonido.bajarVolumen();
        }
        if (controladorEntrada.quiereSilenciar()) {
            gestorMusica.alternarSilencio();
            gestorSonido.alternarSilencio();
        }
    }

    /** Texto para el HUD: "Vol: 50%" o "MUTE". */
    public String getTextoVolumen() {
        if (gestorMusica.isSilenciado()) return "MUTE";
        return "Vol: " + Math.round(gestorMusica.getVolumen() * 100) + "%";
    }

    @Override
    public void dispose() {
        // Game.dispose() no libera la pantalla actual; si se cierra la ventana estando en ella, se libera aca.
        Screen pantallaActual = getScreen();
        if (pantallaActual != null) {
            pantallaActual.hide();
            pantallaActual.dispose();
        }
        if (jugador != null) jugador.dispose();
        batch.dispose();
        font.dispose();
        gestorSonido.dispose();
        gestorMusica.dispose();
    }
}