package com.MalloPalermoGuzman.SaldoCero;

import com.MalloPalermoGuzman.SaldoCero.controles.ControladorEntrada;
import com.MalloPalermoGuzman.SaldoCero.entidades.Jugador;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorMusica;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorSonido;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.ConfiguracionJuego;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.Partida;
import com.MalloPalermoGuzman.SaldoCero.screens.MainMenuScreen;
import com.MalloPalermoGuzman.SaldoCero.screens.PantallaGuardarNombre;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public ConfiguracionJuego configuracion;
    public Partida partida;

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

        configuracion = ConfiguracionJuego.crear();
        partida = new Partida(configuracion);

        setScreen(new MainMenuScreen(this));
    }

    // se llama al empezar una partida nueva desde el menu: saldo, nivel y tiempo vuelven a cero.
    public void nuevaPartida() {
        partida.reiniciar();
    }

    @Override
    public void render() {
        procesarControlesVolumen();
        super.render();
    }

    private void procesarControlesVolumen() {
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

    public String getTextoVolumen() {
        if (gestorMusica.isSilenciado()) return "MUTE";
        return "Vol: " + Math.round(gestorMusica.getVolumen() * 100) + "%";
    }

    @Override
    public void dispose() {
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