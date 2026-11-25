package com.MalloPalermoGuzman.SaldoCero;

import com.MalloPalermoGuzman.SaldoCero.baseDeDatos.GestorBD;
import com.MalloPalermoGuzman.SaldoCero.controles.ControladorEntrada;
import com.MalloPalermoGuzman.SaldoCero.entidades.Jugador;
import com.MalloPalermoGuzman.SaldoCero.logros.Logro;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorMusica;
import com.MalloPalermoGuzman.SaldoCero.gestores.GestorSonido;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.ConfigNivel;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.ConfiguracionJuego;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.Partida;
import com.MalloPalermoGuzman.SaldoCero.screens.Mapa;
import com.MalloPalermoGuzman.SaldoCero.screens.MainMenuScreen;
import com.MalloPalermoGuzman.SaldoCero.screens.PantallaDialogo;
import com.MalloPalermoGuzman.SaldoCero.screens.PantallaFinal;
import com.MalloPalermoGuzman.SaldoCero.screens.PantallaGuardarNombre;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

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
        GestorBD.inicializarBaseDeDatos();
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

    // se llama al empezar una partida nueva desde el menu: saldo, nivel y tiempo vuelven a cero
    // y se muestra el dialogo de inicio del primer nivel.
    public void nuevaPartida() {
        partida.reiniciar();
        if (jugador != null) jugador.actualizarDia(1);
        mostrarDialogoInicio();
    }

    // flujo de la partida: dialogo de inicio -> nivel (Mapa) -> dialogo de cierre -> siguiente nivel ... -> dialogo final -> pantalla final

    private void mostrarDialogoInicio() {
        ConfigNivel nivel = partida.getNivel();
        // "() -> empezarNivel()" significa: cuando termine el dialogo, ejecuta empezarNivel()
        setScreen(new PantallaDialogo(this, nivel.getNombre(), nivel.getDialogoInicio(), () -> empezarNivel()));
    }

    private void empezarNivel() {
        if (jugador == null) {
            jugador = new Jugador(Jugador.INICIO_X, Jugador.INICIO_Y, nombreJugador, partida.getNumeroNivel());
        } else {
            jugador.actualizarDia(partida.getNumeroNivel());
        }
        jugador.colocarEn(Jugador.INICIO_X, Jugador.INICIO_Y);
        setScreen(new Mapa(this));
    }

    public void terminarNivel() {
        ConfigNivel nivel = partida.getNivel();
        int dia = partida.getNumeroNivel();
        int saldoInicial = partida.getSaldoInicialDia();
        boolean metaCumplida = partida.terminarNivel();

        GestorBD.registrarJornada(idJugador, dia, saldoInicial, partida.getSaldo(),
                partida.getCorduraRedondeada(), metaCumplida ? "EXITOSA" : "FALLIDA", partida.getDeuda());
        registrarLogrosPendientes();

        String[] paginas = nivel.getDialogoCierre();
        if (!metaCumplida) {
            String aviso = configuracion.getTextoSinMeta() + " (-" + nivel.getPenalizacionCordura() + " de cordura)";
            paginas = agregarAlPrincipio(aviso, paginas);
        }
        setScreen(new PantallaDialogo(this, nivel.getNombre(), paginas, () -> pasarAlSiguienteNivel()));
    }

    private void pasarAlSiguienteNivel() {
        if (partida.hayNivelSiguiente()) {
            partida.avanzarNivel();
            if (jugador != null) jugador.actualizarDia(partida.getNumeroNivel());
            mostrarDialogoInicio();
        } else {
            setScreen(new PantallaDialogo(this, "Final", configuracion.getDialogoFinal(), () -> setScreen(new PantallaFinal(this))));
        }
    }

    /** Persiste los logros marcados por Partida. El UNIQUE en BD evita duplicados entre partidas. */
    public void registrarLogrosPendientes() {
        if (partida == null || idJugador < 0) return;
        List<Logro> pendientes = partida.getLogrosPendientes();
        if (pendientes.isEmpty()) return;
        List<Logro> confirmados = new ArrayList<>();
        for (Logro logro : pendientes) {
            if (GestorBD.desbloquearLogro(idJugador, logro.getTag())) {
                System.out.println("[LOGRO] Desbloqueado: " + logro.getTitulo());
            }
            // Se cuenta tambien si ya estaba desbloqueado: es un nuevo desbloqueo en esta partida.
            GestorBD.registrarEventoLogro(idJugador, logro.getTag());
            confirmados.add(logro);
        }
        partida.confirmarLogros(confirmados);
    }

    private String[] agregarAlPrincipio(String texto, String[] paginas) {
        String[] nuevo = new String[paginas.length + 1];
        nuevo[0] = texto;
        System.arraycopy(paginas, 0, nuevo, 1, paginas.length);
        return nuevo;
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
